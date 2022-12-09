package eu.wauz.wauzcore.mobs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.MythicUtils;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;

/**
 * Strongboxes are special mobs, that when destroyed spawn strongbox guards.
 * If all the guards are defeated, the player will receive a reward.
 * The Boxes are spawned in dungeons via MythicMobs.
 * This class handles all the stuff that occurs when a box or guard is destroyed.
 * 
 * @author Wauzmons
 */
public class Strongbox {
	
	/**
	 * Maps all stronbox UUIDs to their displays.
	 */
	private static Map<String, ArmorStand> activeStrongboxMap = new HashMap<>();
	
	/**
	 * When a strongbox is destroyed, it spawns a display of how many guards are left.
	 * Guards come in two variants and their numbers are random.
	 * They have the UUID of the display in their metadata,
	 * so the number can be reduced if they die, to finally drop loot, if it reaches zero.
	 * 
	 * @param event The death event.
	 * 
	 * @see MobMetadataUtils#setStrongboxTier(ArmorStand, int)
	 * @see MobMetadataUtils#setStrongboxMob(Entity, ArmorStand)
	 */
	public static void destroy(MythicMobDeathEvent event) {
		Entity entity = event.getEntity();
		if(!MobMetadataUtils.hasStrongboxMob(entity)) {
			Location location = entity.getLocation();
			location.setY(location.getY() + 1);
			
			ArmorStand strongbox = (ArmorStand) location.getWorld().spawn(location, ArmorStand.class);
			activeStrongboxMap.put(strongbox.getUniqueId().toString(), strongbox);
			
			strongbox.setGravity(false);	
			strongbox.setInvulnerable(true);
			strongbox.setCollidable(false);
			strongbox.setVisible(false);
			
			int enemyAmount = Chance.randomInt(5) + 3;
			Components.customName(strongbox, ChatColor.GOLD + "Strongbox Guards Left: ["
					+ ChatColor.DARK_RED + enemyAmount + ChatColor.GOLD + "]");
			strongbox.setCustomNameVisible(true);
			
			int strongboxTier = Integer.parseInt(StringUtils.substringAfterLast(event.getMobType().getInternalName(), "T"));
			MobMetadataUtils.setStrongboxTier(strongbox, strongboxTier);
			
			try {
				for(int iterator = 1; iterator <= enemyAmount; iterator++) {
					Location offsetLocation = entity.getLocation().clone();
					offsetLocation.setX(offsetLocation.getX() + Chance.negativePositive(1.2f));
					offsetLocation.setZ(offsetLocation.getZ() + Chance.negativePositive(1.2f));
					
					String mobSuffix = Chance.percent(40) ? "-Elite" : "";
					Entity mob = MythicUtils.spawnMob("StrongboxGuardT" + strongboxTier + mobSuffix, offsetLocation, "Strongbox");
					if(mob == null) {
						return;
					}
					MobMetadataUtils.setStrongboxMob(mob, strongbox);
				}
				
				for(Player player : strongbox.getWorld().getPlayers()) {
					player.sendMessage(ChatColor.GOLD + "Remaining Strongbox Guards: " + ChatColor.DARK_RED + enemyAmount);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			String uuidString = MobMetadataUtils.getStronboxMob(entity);
			ArmorStand strongbox = activeStrongboxMap.get(uuidString);
			int enemyAmount = Integer.parseInt(StringUtils.substringBetween(
					Components.customName(strongbox), "[" + ChatColor.DARK_RED, ChatColor.GOLD + "]"));
			enemyAmount--;
			
			if(enemyAmount < 1) {
				activeStrongboxMap.remove(uuidString);
				
				int strongboxTier = MobMetadataUtils.getStrongboxTier(strongbox);
				MythicUtils.drop("StrongboxDropsT" + strongboxTier,
						strongbox.getLocation(),
						event.getMob(),
						event.getKiller(),
						"Strongbox");
				strongbox.getWorld().playEffect(strongbox.getLocation(), Effect.DRAGON_BREATH, 0);
				strongbox.remove();
			}
			else {
				Components.customName(strongbox, ChatColor.GOLD + "Strongbox Guards Left: ["
						+ ChatColor.DARK_RED + enemyAmount + ChatColor.GOLD + "]");
			}
			for(Player player : strongbox.getWorld().getPlayers()) {
				player.sendMessage(ChatColor.GOLD + "Remaining Strongbox Guards: " + ChatColor.DARK_RED + enemyAmount);
			}
		}
	}

}
