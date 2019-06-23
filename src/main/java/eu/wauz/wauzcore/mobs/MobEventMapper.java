package eu.wauz.wauzcore.mobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.metadata.FixedMetadataValue;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.system.WauzCommandExecutor;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.DropTable;
import io.lumine.xikage.mythicmobs.drops.LootBag;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import net.md_5.bungee.api.ChatColor;

public class MobEventMapper {
	
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
	public static void deathEvent(MythicMobDeathEvent event) {
		Entity entity = event.getEntity();
		String mobId = entity.getUniqueId().toString();
		Player mobOwner = PetOverviewMenu.getOwner(entity);
		
		if(mobOwner != null)
			PetOverviewMenu.unregPet(mobOwner, mobId);
		if(StringUtils.contains(event.getEntity().getCustomName(), "Strongbox"))
			MobEventMapper.openStrongbox(event);
		if(WauzCommandExecutor.eventTravelMap.containsKey(mobId))
			WauzCommandExecutor.eventTravelMap.remove(mobId);
		if(entity.hasMetadata("wzModExplosive"))
			explodeMob(event.getMobType(), entity, entity.getLocation());
		if(entity.hasMetadata("wzModSplitting"))
			splitMob(event.getMobType(), entity.getLocation());
	}
	
	private static Map<String, ArmorStand> activeStrongboxMap = new HashMap<>();
	
	private static void openStrongbox(MythicMobDeathEvent event) {
		Entity entity = event.getEntity();
		if(entity.getMetadata("wzStrongbox").isEmpty()) {
			Location location = entity.getLocation();
			location.setY(location.getY() + 1);
			
			ArmorStand strongbox = (ArmorStand) location.getWorld().spawn(location, ArmorStand.class);
			activeStrongboxMap.put(strongbox.getUniqueId().toString(), strongbox);
			
			strongbox.setGravity(false);	
			strongbox.setInvulnerable(true);
			strongbox.setCollidable(false);
			strongbox.setVisible(false);
			
			int enemyAmount = new Random().nextInt(5) + 3;
			strongbox.setCustomName(ChatColor.GOLD + "Strongbox Guards Left: ["
					+ ChatColor.DARK_RED + enemyAmount + ChatColor.GOLD + "]");
			strongbox.setCustomNameVisible(true);
			
			int strongboxTier = Integer.parseInt(StringUtils.substringAfterLast(event.getMobType().getInternalName(), "T"));
			strongbox.setMetadata("wzStrongboxTier", new FixedMetadataValue(WauzCore.getInstance(), strongboxTier));
			
			try {
				for(int iterator = 1; iterator <= enemyAmount; iterator++) {
					Location offsetLocation = entity.getLocation().clone();
					offsetLocation.setX(offsetLocation.getX() + Chance.negativePositive(1.2f));
					offsetLocation.setZ(offsetLocation.getZ() + Chance.negativePositive(1.2f));
					
					String mobSuffix = Chance.percent(40) ? "-Elite" : "";
					Entity mob = mythicMobs.spawnMythicMob("StrongboxGuardT" + strongboxTier + mobSuffix, offsetLocation);
					mob.setMetadata("wzStrongbox", new FixedMetadataValue(WauzCore.getInstance(), strongbox.getUniqueId().toString()));
				}
				
				for(Player player : strongbox.getWorld().getPlayers())
					player.sendMessage(ChatColor.GOLD + "Remaining Strongbox Guards: " + ChatColor.DARK_RED + enemyAmount);
				
			}
			catch (InvalidMobTypeException e) {
				e.printStackTrace();
			}
		}
		else {
			String uuidString = entity.getMetadata("wzStrongbox").get(0).asString();
			ArmorStand strongbox = activeStrongboxMap.get(uuidString);
			int enemyAmount = Integer.parseInt(StringUtils.substringBetween(
					strongbox.getCustomName(), "[" + ChatColor.DARK_RED, ChatColor.GOLD + "]"));
			enemyAmount--;
			
			if(enemyAmount < 1) {
				activeStrongboxMap.remove(uuidString);
				
				String strongboxTierString = strongbox.getMetadata("wzStrongboxTier").get(0).asString();
				DropTable dropTable = MythicMobs.inst().getDropManager().getDropTable("StrongboxDropsT" + strongboxTierString).get();
				LootBag lootBag = dropTable.generate(new DropMetadata(event.getMob(), BukkitAdapter.adapt(event.getKiller())));
				lootBag.drop(BukkitAdapter.adapt(strongbox.getLocation()));
				
				strongbox.getWorld().playEffect(strongbox.getLocation(), Effect.DRAGON_BREATH, 0);
				strongbox.remove();
			}
			else {
				strongbox.setCustomName(ChatColor.GOLD + "Strongbox Guards Left: ["
						+ ChatColor.DARK_RED + enemyAmount + ChatColor.GOLD + "]");
			}
			for(Player player : strongbox.getWorld().getPlayers())
				player.sendMessage(ChatColor.GOLD + "Remaining Strongbox Guards: " + ChatColor.DARK_RED + enemyAmount);
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
