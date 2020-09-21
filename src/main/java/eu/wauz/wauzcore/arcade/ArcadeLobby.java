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
	 * All players waiting for a game to start.
	 */
	private static List<Player> waitingPlayers = new ArrayList<>();
	
	/**
	 * Adds a player to the arcade lobby.
	 * 
	 * @param player The player to add.
	 */
	public static void addPlayer(Player player) {
		waitingPlayers.add(player);
	}
	
	/**
	 * Removes a player from the arcade lobby.
	 * 
	 * @param player The player to remove.
	 */
	public static void removePlayer(Player player) {
		waitingPlayers.remove(player);
	}
	
	/**
	 * Checks if a player is waiting for a game to start.
	 * 
	 * @param player The player to check.
	 * 
	 * @return If the player is waiting.
	 */
	public static boolean isWaiting(Player player) {
		return waitingPlayers.contains(player);
	}
	
	/**
	 * Gets the count of players waiting for a game to start.
	 * 
	 * @return The count of waiting players.
	 */
	public static int getWaitingCount() {
		return waitingPlayers.size();
	}

}
