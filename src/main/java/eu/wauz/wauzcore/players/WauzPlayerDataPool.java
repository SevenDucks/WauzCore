package eu.wauz.wauzcore.players;

import java.util.HashMap;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A static cache of player datas.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData
 */
public class WauzPlayerDataPool {
	
	/**
	 * A map of cached player datas by player.
	 */
	private static HashMap<Player, WauzPlayerData> storage = new HashMap<>();

	/**
	 * Fetches a cached player data.
	 * 
	 * @param player The player that owns the player data.
	 * 
	 * @return The requested player data.
	 */
	public static WauzPlayerData getPlayer(Player player) {
		return storage.get(player);
	}

	/**
	 * Registers a player, by creating a new player data.
	 * 
	 * @param player The player to register.
	 * 
	 * @return The created player data.
	 */
	public static WauzPlayerData regPlayer(Player player) {
		WauzPlayerData playerData = new WauzPlayerData(player, storage.size() + 1);
		storage.put(player, playerData);
		DamageCalculator.setHealth(player, 20);
		return playerData;
	}
	
	/**
	 * Removes a player data for a player that left the game.
	 * 
	 * @param player The player to remove.
	 */
	public static void unregPlayer(Player player) {
		storage.remove(player);
	}
	
	/**
	 * Finds out if a player has a character selected.
	 * 
	 * @param player The player for checking the selection.
	 * 
	 * @return If a character is selected.
	 * 
	 * @see WauzPlayerData#isCharacterSelected()
	 */
	public static boolean isCharacterSelected(Player player) {
		WauzPlayerData playerData = getPlayer(player);
		return !WauzMode.inHub(player) && playerData != null && playerData.getSelections().isCharacterSelected();
	}

}
