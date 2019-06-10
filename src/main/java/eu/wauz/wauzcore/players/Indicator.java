package eu.wauz.wauzcore.players;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.Chance;
import net.md_5.bungee.api.ChatColor;

public class Indicator {
	
	public static void spawnDamageIndicator(Entity entity, Integer damage) {
		spawnDamageIndicator(entity, damage, false);
	}

	public static void spawnDamageIndicator(Entity entity, Integer damage, boolean isCritical) {
		if(damage <= 0 || entity instanceof ArmorStand)
			return;
		
		ChatColor color = (entity instanceof Player) ? ChatColor.RED : (isCritical ? ChatColor.GOLD : ChatColor.YELLOW);
		spawnIndicator(entity.getLocation(), color + "" + damage + (isCritical ? " CRIT" : ""));
	}
	
	public static void spawnEvadeIndicator(Player player) {
		spawnIndicator(player.getLocation(), ChatColor.AQUA + "EVADED");
	}
	
	public static void spawnHealIndicator(Location location, int heal) {
		if(heal <= 0)
			return;
		
		spawnIndicator(location, ChatColor.GREEN + "+" + heal);
	}
	
	public static void spawnExpIndicator(Location location, int exp) {
		if(exp <= 0)
			return;
		
		spawnIndicator(location, ChatColor.LIGHT_PURPLE + "" + exp + "EXP");
	}
	
	public static void spawnTestIndicator(Location location) {
		spawnIndicator(location, ChatColor.GRAY + "" + ChatColor.MAGIC + "TESTERINO");
	}
	
	private static void spawnIndicator(Location location, String display) {		
		Entity indicator = WauzNmsClient.nmsCustomEntityHologram(location, display);
		indicator.setVelocity(new Vector(Chance.negativePositive(0.1f), 0.5f, Chance.negativePositive(0.1f)));	
		
		queueIndicatorDespawn(indicator);
	}
	
	private static void queueIndicatorDespawn(final Entity dmgIndicator) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
            	dmgIndicator.remove();
            }
		}, 10);
	}
	
}
