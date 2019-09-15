package eu.wauz.wauzcore.mobs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.system.commands.WauzCommandExecutor;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class MobEventMapper {
	
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
	public static void death(MythicMobDeathEvent event) {
		Entity entity = event.getEntity();
		String mobId = entity.getUniqueId().toString();
		Player mobOwner = PetOverviewMenu.getOwner(entity);
		
		if(mobOwner != null) {
			PetOverviewMenu.unregPet(mobOwner, mobId);
		}
		if(StringUtils.contains(event.getEntity().getCustomName(), "Strongbox")) {
			Strongbox.destroy(event);
		}
		if(WauzCommandExecutor.getEventTravelMap().containsKey(mobId)) {
			WauzCommandExecutor.getEventTravelMap().remove(mobId);
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
	
	private static void explodeMob(MythicMob mythicMob, Entity entity, Location location) {
		List<Player> players = SkillUtils.getPlayersInRadius(location, 4);
		SkillUtils.createExplosion(location, 8);
		for(Player player : players) {
			WauzDebugger.log(player, "Hit by Mob Explosion!");
			int damage = (int) ((double) mythicMob.getBaseDamage() * (double) 4);
			EntityDamageEvent event = new EntityDamageByEntityEvent(entity, player, DamageCause.ENTITY_EXPLOSION, damage);
			DamageCalculator.defend(event);
		}
	}
	
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
