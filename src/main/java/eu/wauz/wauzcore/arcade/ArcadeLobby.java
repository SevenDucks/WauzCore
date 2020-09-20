package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * The static lobby for the arcade mode.
 * 
 * @author Wauzmons
 */
public class ArcadeLobby {
	
	/**
	 * All players in the arcade world.
	 */
	private static List<Player> allPlayers = new ArrayList<>();
	
	/**
	 * All players waiting for a game to start.
	 */
	private static List<Player> waitingPlayers = new ArrayList<>();
	
	/**
	 * Adds a player to the arcade lobby.
	 * 
	 * @param player The player to add.
	 */
	public static void addPlayer(Player player) {
		allPlayers.add(player);
		waitingPlayers.add(player);
	}
	
	/**
	 * Removes a player from the arcade lobby.
	 * 
	 * @param player The player to remove.
	 */
	public static void removePlayer(Player player) {
		allPlayers.remove(player);
		waitingPlayers.remove(player);
	}

}
