package eu.wauz.wauzcore.mobs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.admins.CmdWzTravelEvent;
import eu.wauz.wauzcore.mobs.bestiary.ObservationTracker;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.MythicUtils;
import eu.wauz.wauzcore.worlds.instances.InstanceMobArena;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobDespawnEvent;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;

/**
 * A mapper class, that decides what to do, when a (mythic) mob spawns or dies.
 * This goes from loading healthbars and dropping exp to frickin' exploding.
 * 
 * @author Wauzmons
 */
public class MobEventMapper {
	
	/**
	 * When a mob spawns, modifiers, loot and boss bars are initialized.
	 * Also following cases are possible:
	 * If it is part of a mob arena, it gets added to the wave.
	 * 
	 * @param event The spawn event.
	 * 
	 * @see MenacingMobsSpawner#addMenacingMob(Entity, MythicMob)
	 * @see InstanceMobArena#increaseMobCount()
	 */
	public static void spawn(MythicMobSpawnEvent event) {
		Entity entity = event.getEntity();
		MenacingMobsSpawner.addMenacingMob(event.getEntity(), event.getMobType());
		
		if(WauzActivePet.getOwner(entity) == null) {
			InstanceMobArena.tryToIncreaseMobCount(entity);
		}
	}
	
	/**
	 * When a mob despawns, following cases are possible:
	 * It gets unregistered from its owner, if it was a pet.
	 * If it is part of a mob arena, it gets removed from the wave.
	 * 
	 * @param event The despawn event.
	 * 
	 * @see WauzActivePet#removeOwner(String, Player)
	 * @see InstanceMobArena#tryToDecreaseMobCount(Entity)
	 */
	public static void despawn(MythicMobDespawnEvent event) {
		Entity entity = event.getEntity();
		String mobId = entity.getUniqueId().toString();
		Player mobOwner = WauzActivePet.getOwner(entity);
		
		if(mobOwner != null) {
			WauzActivePet.removeOwner(mobId, mobOwner);
		}
		else {
			InstanceMobArena.tryToDecreaseMobCount(entity);
		}
	}
	
	/**
	 * When a mob dies, following cases are possible:
	 * It gets unregistered from its owner, if it was a pet.
	 * If it is part of a mob arena, it gets removed from the wave.
	 * The killer, gets achievement progress, if it was a player, killing a valid target.
	 * It spawns guards or loot, if it was a strongbox.
	 * It gets removed from the travel map, if it was a worldboss.
	 * It explodes or splits, if it has fitting menacing modifiers (prefixes).
	 * It drops exp or a key, depending on mob type.
	 * 
	 * @param event The death event.
	 * 
	 * @see WauzActivePet#removeOwner(String, Player)
	 * @see InstanceMobArena#tryToDecreaseMobCount(Entity)
	 * @see ObservationTracker#tryToAddProgress(Player, Entity)
	 * @see AchievementTracker#addProgress(Player, WauzAchievementType, double)
	 * @see Strongbox#destroy(MythicMobDeathEvent)
	 * @see CmdWzTravelEvent#getEventTravelMap()
	 * @see MobEventMapper#explodeMob(MythicMob, Entity, Location)
	 * @see MobEventMapper#splitMob(MythicMob, Location)
	 * @see MenacingMobsLoot#dropExp(Entity, Entity)
	 * @see MenacingMobsLoot#dropKey(Entity)
	 */
	public static void death(MythicMobDeathEvent event) {
		Entity entity = event.getEntity();
		String mobId = entity.getUniqueId().toString();
		Player mobOwner = WauzActivePet.getOwner(entity);
		
		if(mobOwner != null) {
			WauzActivePet.removeOwner(mobId, mobOwner);
		}
		else {
			InstanceMobArena.tryToDecreaseMobCount(entity);
		}
		if(SkillUtils.isValidAttackTarget(entity) && event.getKiller() instanceof Player) {
			Player player = (Player) event.getKiller();
			ObservationTracker.tryToAddProgress(player, entity);
			AchievementTracker.addProgress(player, WauzAchievementType.KILL_ENEMIES, 1);
			AchievementTracker.checkForAchievement(player, WauzAchievementType.DEFEAT_BOSSES, Components.customName(entity));
		}
		if(StringUtils.contains(Components.customName(entity), "Strongbox")) {
			Strongbox.destroy(event);
		}
		if(CmdWzTravelEvent.getEventTravelMap().containsKey(mobId)) {
			CmdWzTravelEvent.getEventTravelMap().remove(mobId);
		}
		if(MobMetadataUtils.hasMenacingModifier(entity, MenacingModifier.EXPLOSIVE)) {
			explodeMob(event.getMobType(), entity, entity.getLocation());
		}
		if(MobMetadataUtils.hasMenacingModifier(entity, MenacingModifier.SPLITTING)) {
			splitMob(event.getMobType(), entity.getLocation());
		}
		if(MobMetadataUtils.hasExpDrop(entity)) {
			MenacingMobsLoot.dropExp(entity, event.getKiller());
		}
		if(MobMetadataUtils.hasKeyDrop(entity)) {
			MenacingMobsLoot.dropKey(entity);
		}
	}
	
	/**
	 * Creates an explosion and deals 500% damage to everyone within a radius of 4 blocks.
	 * Caused by the "Explosive" meanacing modifier on death.
	 * 
	 * @param mythicMob The type of the mob.
	 * @param entity The entity that is exploding.
	 * @param location The location the explosion takes place.
	 */
	private static void explodeMob(MythicMob mythicMob, Entity entity, Location location) {
		List<Player> players = SkillUtils.getPlayersInRadius(location, 4);
		SkillUtils.createExplosion(location, 8);
		for(Player player : players) {
			WauzDebugger.log(player, "Hit by Mob Explosion!");
			int damage = (int) ((double) mythicMob.getDamage().get() * (double) 5);
			player.damage(damage, entity);
		}
	}
	
	/**
	 * Spawns 4 mobs of the same type and knocks them away from their spawn.
	 * Caused by the "Splitting" meanacing modifier on death.
	 * 
	 * @param mythicMob The type of the mob.
	 * @param location The location the splitting takes place.
	 */
	private static void splitMob(MythicMob mythicMob, Location location) {
		for(int iterator = 1; iterator <= 4; iterator++) {
			Location offsetLocation = location.clone();
			offsetLocation.setX(offsetLocation.getX() + Chance.negativePositive(2.4f));
			offsetLocation.setZ(offsetLocation.getZ() + Chance.negativePositive(2.4f));
			Entity entity = MythicUtils.spawnMob(mythicMob.getInternalName(), location, "Split");
			if(entity == null) {
				return;
			}
			SkillUtils.throwBackEntity(entity, offsetLocation, 0.5);
		}
	}

}
