package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
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
	 * @see WauzPlayerData#setMana(Integer)
	 * @see WauzPlayerData#getMaxMana()
	 * @see WauzPlayerActionBar#update(Player)
	 */
	public static void regenerateMana(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		if(playerData.getMaxMana() > playerData.getMana() + amount) {
			playerData.setMana(playerData.getMana() + amount);
		}
		else {
			playerData.setMana(playerData.getMaxMana());
		}
		WauzPlayerActionBar.update(player);
	}
	
	/**
	 * Determines if a player has enough mana for something.
	 * If true, the mana is used up.
	 * If false, a message is shown to the player.
	 * 
	 * @param player The player that tries to spedn mana.
	 * @param amount The needed amount of mana.
	 * 
	 * @return If the player had enough mana.
	 * 
	 * @see WauzPlayerData#getMana()
	 * @see WauzPlayerActionBar#update(Player)
	 */
	public static boolean useMana(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || (playerData.getMana() - amount) < 0) {
			player.sendMessage(ChatColor.RED + "Not enough Mana! " + amount + " Points are needed!");
			return false;
		}
		playerData.setMana(playerData.getMana() - amount);
		AchievementTracker.addProgress(player, WauzAchievementType.USE_MANA, amount);
		WauzPlayerActionBar.update(player);
		return true;
	}
	
}
