package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionStats;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * Used to calculate the amount of mana a player has.
 * Can also check if a player has enough mana for a skill.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData#getMana()
 */
public class ManaCalculator {
	
	/**
	 * The default maximum amount of rage for a player.
	 */
	public static final int MAX_MANA = 10;
	
	/**
	 * Adds 1-2 mana points to the given player.
	 * Capped at their current maximum.
	 * 
	 * @param player The player who should receive mana.
	 * 
	 * @see ManaCalculator#regenerateMana(Player, int)
	 */
	public static void regenerateMana(Player player) {
		regenerateMana(player, Chance.minMax(1, 2));
	}
	
	/**
	 * Adds multiple mana points to the given player.
	 * Capped at their current maximum.
	 * 
	 * @param player The player who should receive mana.
	 * @param amount The amount of mana to give.
	 * 
	 * @see WauzPlayerData#setMana(int)
	 * @see WauzPlayerData#getMaxMana()
	 * @see WauzPlayerActionBar#update(Player)
	 */
	public static void regenerateMana(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		WauzPlayerDataSectionStats stats = playerData.getStats();
		
		if(stats.getMaxMana() > stats.getMana() + amount) {
			stats.setMana(stats.getMana() + amount);
		}
		else {
			stats.setMana(stats.getMaxMana());
		}
		WauzPlayerActionBar.update(player);
	}
	
	/**
	 * Determines if a player has enough mana for something.
	 * If true, the mana is used up.
	 * If false, a message is shown to the player.
	 * 
	 * @param player The player that tries to spend mana.
	 * @param amount The needed amount of mana.
	 * 
	 * @return If the player had enough mana.
	 * 
	 * @see WauzPlayerData#getMana()
	 * @see WauzPlayerActionBar#update(Player)
	 */
	public static boolean useMana(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || (playerData.getStats().getMana() - amount) < 0) {
			player.sendMessage(ChatColor.RED + "Not enough Mana! " + amount + " Points are needed!");
			return false;
		}
		playerData.getStats().setMana(playerData.getStats().getMana() - amount);
		AchievementTracker.addProgress(player, WauzAchievementType.USE_MANA, amount);
		WauzPlayerActionBar.update(player);
		return true;
	}
	
}
