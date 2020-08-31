package eu.wauz.wauzcore.items;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * A collection of static methods to pay out currency rewards to players.
 * 
 * @author Wauzmons
 */
public class WauzRewards {

	/**
	 * Gives a player coins, based on their rank, if the cooldown is ready.
	 * 
	 * @param player The player that should claim their daily reward.
	 * 
	 * @see Cooldown#characterDailyReward(Player)
	 * @see PlayerConfigurator#getRank(org.bukkit.OfflinePlayer)
	 * @see PlayerConfigurator#setCharacterCoins(Player, long)
	 */
	public static void earnDailyReward(final Player player) {
		if(!Cooldown.characterDailyReward(player)) {
			return;
		}
    	WauzRank rank = WauzRank.getRank(player);
    	int coins = rank.getDailyCoins();
    	int souls = rank.getDailySoulstones();

    	long currentCoins = PlayerCollectionConfigurator.getCharacterCoins(player);
    	PlayerCollectionConfigurator.setCharacterCoins(player, currentCoins + coins);
    	AchievementTracker.addProgress(player, WauzAchievementType.EARN_COINS, coins);
    	player.sendMessage(ChatColor.GOLD + rank.getRankName() + " Reward: " + "You claimed your daily " + coins + " coins!");
    	
    	if(souls > 0) {
    		long currentSouls = PlayerCollectionConfigurator.getCharacterSoulstones(player);
    		PlayerCollectionConfigurator.setCharacterSoulstones(player, currentSouls + souls);
    		player.sendMessage(ChatColor.GOLD + "You also received " + souls + " additional soulstones!");
    	}
	}
	
	/**
	 * Adds a token from MMORPG mode, if the limit of 30 has not been reached today.
	 * 
	 * @param player The player to receive the token.
	 * 
	 * @see WauzRewards#getTokensEarnedToday(Player, String)
	 * @see WauzRewards#earnToken(Player, String, int, int)
	 */
	public static void earnMmoRpgToken(Player player) {
		final int limit = 30;
		final int today = getTokensEarnedToday(player, "mmorpg");
		if(today >= limit) {
			player.sendMessage(ChatColor.YELLOW + "Tokenlimit (MMORPG) of " + limit + " reached for today!");
			return;
		}
		earnToken(player, "MMORPG", today + 1, limit);
	}
	
	/**
	 * Adds a token from Survival mode, if the limit of 20 has not been reached today.
	 * Also resets the level to the maximum and increases the survival score by 1.
	 * 
	 * @param player The player to receive the token.
	 * 
	 * @see WauzRewards#getTokensEarnedToday(Player, String)
	 * @see WauzRewards#earnToken(Player, String, int, int)
	 * @see WauzCore#MAX_PLAYER_LEVEL_SURVIVAL
	 * @see PlayerConfigurator#setSurvivalScore(org.bukkit.OfflinePlayer, long)
	 */
	public static void earnSurvivalToken(Player player) {
		player.setLevel(WauzCore.MAX_PLAYER_LEVEL_SURVIVAL);
		player.setExp(0);
		
		final int limit = 20;
		final int today = getTokensEarnedToday(player, "survival");
		if(today >= limit) {
			player.sendMessage(ChatColor.YELLOW + "Tokenlimit (Survival) of " + limit + " reached for today!");
			return;
		}
		Long survivalScore = PlayerConfigurator.getSurvivalScore(player) + 1;
		PlayerConfigurator.setSurvivalScore(player, survivalScore);
		player.sendMessage(ChatColor.GOLD + "You reached Survival Score " + Formatters.INT.format(survivalScore) + "!");
		earnToken(player, "Survival", today + 1, limit);
	}
	
	/**
	 * Gets the amount of tokens earned for the current date.
	 * 
	 * @param player The player that earned the tokens.
	 * @param mode The mode the tokens were earned in.
	 * 
	 * @return The amount of tokens earned today.
	 * 
	 * @see WauzDateUtils#getDateLong()
	 * @see PlayerCollectionConfigurator#getTokenLimitDate(Player, String)
	 * @see PlayerCollectionConfigurator#getTokenLimitAmount(Player, String)
	 */
	private static int getTokensEarnedToday(Player player, String mode) {
		long dateLong = PlayerCollectionConfigurator.getTokenLimitDate(player, mode);
		long currentDateLong = WauzDateUtils.getDateLong();
		return dateLong < currentDateLong ? 0 : PlayerCollectionConfigurator.getTokenLimitAmount(player, mode);
	}
	
	/**
	 * Adds a token from the given mode.
	 * Also updates the date for the daily limit and the amount earned today.
	 * 
	 * @param player The player to receive the token.
	 * @param modeDisplay The mode name, how it should be displayed.
	 * @param earnedToday The amount of tokens earned today.
	 * @param maxToday The maximum amount of tokens earnable per day.
	 * 
	 * @see PlayerCollectionConfigurator#setTokenLimitDate(Player, String, long)
	 * @see PlayerCollectionConfigurator#setTokenLimitAmount(Player, String, int)
	 * @see PlayerCollectionConfigurator#setTokens(Player, long)
	 */
	private static void earnToken(Player player, String modeDisplay, int earnedToday, int maxToday) {
		PlayerCollectionConfigurator.setTokenLimitDate(player, modeDisplay.toLowerCase(), WauzDateUtils.getDateLong());
		PlayerCollectionConfigurator.setTokenLimitAmount(player, modeDisplay.toLowerCase(), earnedToday);
		PlayerCollectionConfigurator.setTokens(player, PlayerCollectionConfigurator.getTokens(player) + 1);
		int leftToday = maxToday - earnedToday;
		String leftString = leftToday == 0
				? "You reached the daily limit in " + modeDisplay + "!"
				: "You can earn " + leftToday + " more in " + modeDisplay + " today!";
		player.sendMessage(ChatColor.GOLD + "You earned a token! " + leftString);
		WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
	}

}
