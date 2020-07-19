package eu.wauz.wauzcore.players;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.listeners.PlayerInteractionListener;




public class WauzPlayerSit {
	/**
	 * Player sit action
	 * @param player
	 */
	
	/**
	 * TO DO:
	 * 1. When player request sit command, check if the player is flying an if true then return "You can't sit while flying" else player is allowed to sit.
	 * 2. fix player TP location when dismounting.
	 */
	public static void sit(Player player) {
		player.sendMessage(ChatColor.GOLD + "You are now sitting.");
		PlayerInteractionListener.player = player;
		PlayerInteractionListener.loc = player.getLocation();
		Location playerLocation = player.getLocation().clone().add(0, -0.58, 0);
		World world = player.getWorld();
		Entity arrow = world.spawnEntity(playerLocation, EntityType.ARROW);
		arrow.addPassenger(player);
	}
}
