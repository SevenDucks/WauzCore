package eu.wauz.wauzcore.players;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.spigotmc.event.entity.EntityDismountEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A class for making players sit and stand up again.
 * 
 * @author Eddshine
 */
public class WauzPlayerSit {
	
	/**
	 * Checks if a player can sit down.
	 * 
	 * @param player The player who wants to sit.
	 * 
	 * @return If sitting is possible.
	 */
	public static boolean canSitDown(Player player) {
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You are already sitting.");
			return false;
		}
		if(player.getGameMode().equals(GameMode.SPECTATOR)) {
			player.sendMessage(ChatColor.RED + "You cannot sit while in spectator mode!");
			return false;
		}
		if(!WauzMode.getMode(player).equals(WauzMode.MMORPG)) {
			player.sendMessage(ChatColor.RED + "You cannot sit in this world!");
			return false;
		}
		Material blockType = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
		if(blockType.equals(Material.AIR)) {
			player.sendMessage(ChatColor.RED + "You cannot sit while in mid-air!");
			return false;
		}
		if(blockType.equals(Material.WATER) || blockType.equals(Material.LAVA)) {
			player.sendMessage(ChatColor.RED + "You cannot sit on liquid!");
			return false;
		}
		return true;
	}

	/**
	 * Makes the player sit down at their current location, by spawning an arrow to sit on.
	 * 
	 * @param player The player that should sit down.
	 * 
	 * @see WauzPlayerSit#canSitDown(Player)
	 */
	public static void sit(Player player) {
		if(!canSitDown(player)) {
			return;
		}
		Location arrowLocation = player.getLocation().clone().add(0, -0.55, 0);
		Entity arrow = arrowLocation.getWorld().spawnEntity(arrowLocation, EntityType.ARROW);
		arrow.addPassenger(player);
		player.sendMessage(ChatColor.YELLOW + "You are now sitting.");
	}
	
	/**
	 * Lets the player stand up and makes the arrow they were sitting on despawn.
	 * 
	 * @param event The dismount event.
	 */
	public static void standUp(EntityDismountEvent event) {
		Entity player = event.getEntity();
		Location playerLocation = player.getLocation().clone().add(0, 0.55, 0);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
            public void run() {
            	try {
            		event.getDismounted().remove();
            		player.teleport(playerLocation);
            	}
            	catch (NullPointerException e) {
            		WauzDebugger.catchException(getClass(), e);
            	}
            }
            
		}, 1);
	}
	
}
