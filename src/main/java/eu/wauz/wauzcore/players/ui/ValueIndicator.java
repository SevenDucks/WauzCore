package eu.wauz.wauzcore.players.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.nms.NmsEntityHologram;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * An UI class to spawn hovering numbers to indicate things like received damage.
 * 
 * @author Wauzmons
 */
public class ValueIndicator {
	
	/**
	 * Spawns a damage indicator.
	 * 
	 * @param entity The entity who owns the indicator.
	 * @param damage The damage value.
	 * 
	 * @see ValueIndicator#spawnIndicator(Location, String)
	 */
	public static void spawnDamageIndicator(Entity entity, Integer damage) {
		spawnDamageIndicator(entity, damage, false);
	}

	/**
	 * Spawns a damage indicator.
	 * 
	 * @param entity The entity who owns the indicator.
	 * @param damage The damage value.
	 * @param isCritical If it is critical damage. (Shown with prefix and different color)
	 * 
	 * @see ValueIndicator#spawnIndicator(Location, String)
	 */
	public static void spawnDamageIndicator(Entity entity, Integer damage, boolean isCritical) {
		if(damage <= 0 || entity instanceof ArmorStand) {
			return;
		}
		
		ChatColor color = (entity instanceof Player) ? ChatColor.RED : (isCritical ? ChatColor.GOLD : ChatColor.YELLOW);
		spawnIndicator(entity.getLocation(), color + "" + damage + (isCritical ? " CRIT" : ""));
	}
	
	/**
	 * Spawns an indicator that spells "EVADED".
	 * 
	 * @param entity The entity who owns the indicator.
	 * 
	 * @see ValueIndicator#spawnIndicator(Location, String)
	 */
	public static void spawnEvadedIndicator(Entity entity) {
		spawnIndicator(entity.getLocation(), ChatColor.AQUA + "EVADED");
	}
	
	/**
	 * Spawns an indicator that spells "MISSED".
	 * 
	 * @param entity The entity who owns the indicator.
	 * 
	 * @see ValueIndicator#spawnIndicator(Location, String)
	 */
	public static void spawnMissedIndicator(Entity entity) {
		spawnIndicator(entity.getLocation(), ChatColor.DARK_RED + "MISSED");
	}
	
	public static void spawnHealIndicator(Location location, int heal) {
		if(heal <= 0) {
			return;
		}
		
		spawnIndicator(location, ChatColor.GREEN + "+" + heal);
	}
	
	/**
	 * Spawns an gained experience indicator
	 * 
	 * @param location The location to spawn the indicator.
	 * @param exp The experience amount.
	 * 
	 * @see ValueIndicator#spawnIndicator(Location, String)
	 */
	public static void spawnExpIndicator(Location location, int exp) {
		if(exp <= 0) {
			return;
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
            public void run() {
            	spawnIndicator(location, ChatColor.LIGHT_PURPLE + "" + exp + " EXP");
            }
            
		}, 5);
	}
	
	/**
	 * Spawns an indicator for debugging purposes.
	 * 
	 * @param location The location to spawn the indicator.
	 * 
	 * @see ValueIndicator#spawnIndicator(Location, String)
	 */
	public static void spawnTestIndicator(Location location) {
		spawnIndicator(location, ChatColor.GRAY + "" + ChatColor.MAGIC + "TESTERINO");
	}
	
	/**
	 * Spawns a generic indicator and queues its despawn.
	 * 
	 * @param location The location to spawn the indicator.
	 * @param display The text to display.
	 * 
	 * @see ValueIndicator#queueIndicatorDespawn(Entity)
	 */
	private static void spawnIndicator(Location location, String display) {		
		Entity indicator = NmsEntityHologram.create(location, display);
		indicator.setVelocity(new Vector(Chance.negativePositive(0.1f), 0.5f, Chance.negativePositive(0.1f)));	
		
		queueIndicatorDespawn(indicator);
	}
	
	/**
	 * Queues the despawn of an indicator in 0.5 seconds.
	 * 
	 * @param dmgIndicator The indicator entity.
	 */
	private static void queueIndicatorDespawn(final Entity dmgIndicator) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            
			public void run() {
            	dmgIndicator.remove();
            }
            
		}, 10);
	}
	
}
