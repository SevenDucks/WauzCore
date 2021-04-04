package eu.wauz.wauzcore.mobs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.util.Chance;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.DropTable;
import io.lumine.xikage.mythicmobs.drops.LootBag;

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
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
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
			strongbox.setCustomName(ChatColor.GOLD + "Strongbox Guards Left: ["
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
					Entity mob = mythicMobs.spawnMythicMob("StrongboxGuardT" + strongboxTier + mobSuffix, offsetLocation);
					MobMetadataUtils.setStrongboxMob(mob, strongbox);
				}
				
				for(Player player : strongbox.getWorld().getPlayers()) {
					player.sendMessage(ChatColor.GOLD + "Remaining Strongbox Guards: " + ChatColor.DARK_RED + enemyAmount);
				}
			}
			catch (InvalidMobTypeException e) {
				e.printStackTrace();
			}
		}
		else {
			String uuidString = MobMetadataUtils.getStronboxMob(entity);
			ArmorStand strongbox = activeStrongboxMap.get(uuidString);
			int enemyAmount = Integer.parseInt(StringUtils.substringBetween(
					strongbox.getCustomName(), "[" + ChatColor.DARK_RED, ChatColor.GOLD + "]"));
			enemyAmount--;
			
			if(enemyAmount < 1) {
				activeStrongboxMap.remove(uuidString);
				
				int strongboxTier = MobMetadataUtils.getStrongboxTier(strongbox);
				DropTable dropTable = MythicMobs.inst().getDropManager().getDropTable("StrongboxDropsT" + strongboxTier).get();
				LootBag lootBag = dropTable.generate(new DropMetadata(event.getMob(), BukkitAdapter.adapt(event.getKiller())));
				lootBag.drop(BukkitAdapter.adapt(strongbox.getLocation()));
				
				strongbox.getWorld().playEffect(strongbox.getLocation(), Effect.DRAGON_BREATH, 0);
				strongbox.remove();
			}
			else {
				strongbox.setCustomName(ChatColor.GOLD + "Strongbox Guards Left: ["
						+ ChatColor.DARK_RED + enemyAmount + ChatColor.GOLD + "]");
			}
			for(Player player : strongbox.getWorld().getPlayers()) {
				player.sendMessage(ChatColor.GOLD + "Remaining Strongbox Guards: " + ChatColor.DARK_RED + enemyAmount);
			}
		}
	}

}
