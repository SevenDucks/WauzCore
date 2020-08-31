package eu.wauz.wauzcore.players;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
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
	 * @param showMessages If info messages should be shown to the player.
	 * 
	 * @return If sitting is possible.
	 */
	public static boolean canSitDown(Player player, boolean showMessages) {
		String message = null;
		Material blockType = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
		
		if(player.isInsideVehicle()) {
			message = ChatColor.RED + "You are already sitting.";
		}
		else if(player.getGameMode().equals(GameMode.SPECTATOR)) {
			message = ChatColor.RED + "You cannot sit while in spectator mode!";
		}
		else if(!WauzMode.getMode(player).equals(WauzMode.MMORPG) || WauzMode.isInstance(player.getWorld().getName())) {
			message = ChatColor.RED + "You cannot sit in this world!";
		}
		else if(blockType.equals(Material.AIR)) {
			message = ChatColor.RED + "You cannot sit while in mid-air!";
		}
		else if(blockType.equals(Material.WATER) || blockType.equals(Material.LAVA)) {
			message = ChatColor.RED + "You cannot sit while in liquid!";
		}
		
		if(message != null) {
			if(showMessages) {
				player.sendMessage(message);
			}
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Makes the player sit down at their current location, by spawning an arrow to sit on.
	 * 
	 * @param player The player that should sit down.
	 * 
	 * @see WauzPlayerSit#canSitDown(Player)
	 */
	public static void sit(Player player) {
		if(!canSitDown(player, true)) {
			return;
		}
		Location arrowLocation = player.getLocation().clone().add(0, -0.55, 0);
		Entity arrow = arrowLocation.getWorld().spawnEntity(arrowLocation, EntityType.ARROW);
		arrow.addPassenger(player);
		player.sendMessage(ChatColor.YELLOW + "You are now sitting.");
	}
	
	/**
	 * Makes the player sit down on the given STAIRS block, by spawning an arrow to sit on.
	 * 
	 * @param player The player that should sit down.
	 * @param block The block to sit on.
	 * 
	 * @see WauzPlayerSit#canSitDown(Player)
	 */
	public static void sit(Player player, Block block) {
		Stairs stairs = ((Stairs) block.getState().getBlockData());
		if(!canSitDown(player, false) || !stairs.getHalf().equals(Half.BOTTOM)) {
			return;
		}
		Location arrowLocation = block.getLocation().clone().add(0.5, -0.05, 0.5);
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
