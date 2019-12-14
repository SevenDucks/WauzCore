package eu.wauz.wauzcore.mobs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import eu.wauz.wauzcore.commands.administrative.CmdWzTravelEvent;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

/**
 * A mapper class, that decides what to do, when a (mythic) mob dies.
 * This goes from dropping exp to frickin' exploding.
 * 
 * @author Wauzmons
 */
public class MobEventMapper {
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
	/**
	 * When a mob dies, following cases are possible:
	 * It gets unregistered from its owner, if it was a pet.
	 * It spawns guards or loot, if it was a strongbox.
	 * It gets removed from the travel map, if it was a worldboss.
	 * It explodes or splits, if it has fitting menacing modifiers (prefixes).
	 * It drops exp or a key, depending on mob type.
	 * 
	 * @param event The death event.
	 */
	public static void death(MythicMobDeathEvent event) {
		Entity entity = event.getEntity();
		String mobId = entity.getUniqueId().toString();
		Player mobOwner = PetOverviewMenu.getOwner(entity);
		
		if(mobOwner != null) {
			PetOverviewMenu.removeOwner(mobId, mobOwner);
		}
		if(StringUtils.contains(event.getEntity().getCustomName(), "Strongbox")) {
			Strongbox.destroy(event);
		}
		if(CmdWzTravelEvent.getEventTravelMap().containsKey(mobId)) {
			CmdWzTravelEvent.getEventTravelMap().remove(mobId);
		}
		if(entity.hasMetadata("wzModExplosive")) {
			explodeMob(event.getMobType(), entity, entity.getLocation());
		}
		if(entity.hasMetadata("wzModSplitting")) {
			splitMob(event.getMobType(), entity.getLocation());
		}
		if(entity.hasMetadata("wzExpAmount")) {
			MenacingMobsLoot.dropExp(entity, event.getKiller());
		}
		if(entity.hasMetadata("wzKeyId")) {
			MenacingMobsLoot.dropKey(entity);
		}
	}
	
	/**
	 * Creates an explosion and deals 500% damage to everyone within a radius of 4 blocks.
	 * Caused by the "Explosive" meanacing modifier on death.
	 * 
	 * @param mythicMob
	 * @param entity
	 * @param location
	 */
	private static void explodeMob(MythicMob mythicMob, Entity entity, Location location) {
		List<Player> players = SkillUtils.getPlayersInRadius(location, 4);
		SkillUtils.createExplosion(location, 8);
		for(Player player : players) {
			WauzDebugger.log(player, "Hit by Mob Explosion!");
			int damage = (int) ((double) mythicMob.getBaseDamage() * (double) 5);
			EntityDamageEvent event = new EntityDamageByEntityEvent(entity, player, DamageCause.ENTITY_EXPLOSION, damage);
			DamageCalculator.defend(event);
		}
	}
	
	/**
	 * Spawns 4 mobs of the same type and knocks them away from their spawn.
	 * Caused by the "Splitting" meanacing modifier on death.
	 * 
	 * @param mythicMob
	 * @param location
	 */
	private static void splitMob(MythicMob mythicMob, Location location) {
		try {
			for(int iterator = 1; iterator <= 4; iterator++) {
				Location offsetLocation = location.clone();
				offsetLocation.setX(offsetLocation.getX() + Chance.negativePositive(2.4f));
				offsetLocation.setZ(offsetLocation.getZ() + Chance.negativePositive(2.4f));
				Entity entity = mythicMobs.spawnMythicMob(mythicMob, location, 1);
				SkillUtils.throwBackEntity(entity, offsetLocation, 0.5);
			}
		}
		catch (InvalidMobTypeException e) {
			e.printStackTrace();
		}
	}

}
