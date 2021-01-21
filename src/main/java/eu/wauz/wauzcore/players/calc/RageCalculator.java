package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionStats;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;

/**
 * Used to calculate the amount of rage a player has.
 * Can also check if a player has enough rage for a skill.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData#getRage()
 */
public class RageCalculator {
	
	/**
	 * The default maximum amount of rage for a player.
	 */
	public static final int MAX_RAGE = 30;
	
	/**
	 * Adds a rage point to the given player.
	 * Capped at their current maximum.
	 * 
	 * @param player The player who should receive rage.
	 * 
	 * @see RageCalculator#generateRage(Player, int)
	 */
	public static void generateRage(Player player) {
		generateRage(player, 1);
	}
	
	/**
	 * Adds multiple rage points to the given player.
	 * Capped at their current maximum.
	 * 
	 * @param player The player who should receive rage.
	 * @param amount The amount of rage to give.
	 * 
	 * @see WauzPlayerData#setRage(int)
	 * @see WauzPlayerData#getMaxRage()
	 * @see WauzPlayerActionBar#update(Player)
	 */
	public static void generateRage(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		WauzPlayerDataSectionStats stats = playerData.getStats();
		
		if(stats.getMaxRage() > stats.getRage() + amount) {
			stats.setRage(stats.getRage() + amount);
		}
		else {
			stats.setRage(stats.getMaxRage());
		}
		WauzPlayerActionBar.update(player);
	}
	
	/**
	 * Decreases the player's rage by one point.
	 * Capped at 0 points.
	 * 
	 * @param player The player who should lose rage.
	 * 
	 * @see WauzPlayerData#setRage(int)
	 * @see WauzPlayerActionBar#update(Player)
	 */
	public static void degenerateRage(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		if(playerData.getStats().getRage() - 1 >= 0) {
			playerData.getStats().setRage(playerData.getStats().getRage() - 1);
		}
		else {
			playerData.getStats().setRage(0);
		}
		WauzPlayerActionBar.update(player);
	}
	
	/**
	 * Determines if a player has enough rage for something.
	 * If true, the rage is used up.
	 * If false, a message is shown to the player.
	 * 
	 * @param player The player that tries to spend rage.
	 * @param amount The needed amount of rage.
	 * 
	 * @return If the player had enough rage.
	 * 
	 * @see WauzPlayerData#getRage()
	 * @see WauzPlayerActionBar#update(Player)
	 */
	public static boolean useRage(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || (playerData.getStats().getRage() - amount) < 0) {
			player.sendMessage(ChatColor.RED + "Not enough Rage! " + amount + " Points are needed!");
			return false;
		}
		playerData.getStats().setRage(playerData.getStats().getRage() - amount);
		WauzPlayerActionBar.update(player);
		return true;
	}

}
