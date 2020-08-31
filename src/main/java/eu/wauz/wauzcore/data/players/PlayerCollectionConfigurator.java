package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerConfigurationUtils;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;

/**
 * Configurator to fetch or modify collection data from the Player.yml files.
 * 
 * @author Wauzmons
 */
public class PlayerCollectionConfigurator extends PlayerConfigurationUtils {
	
// Tokens

	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of tokens of the player.
	 */
	public static long getTokens(Player player) {
		return playerConfigGetLong(player, "tokens", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param tokens The new amount of tokens of the player.
	 */
	public static void setTokens(Player player, long tokens) {
		playerConfigSet(player, "tokens", tokens, false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mode The gamemode for the limit.
	 * 
	 * @return The date of the last earned tokens as number in yyyyMMdd format.
	 */
	public static long getTokenLimitDate(Player player, String mode) {
		return playerConfigGetLong(player, "tokenlimit." + mode + ".date", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mode The gamemode for the limit.
	 * @param dateLong The new date of the last earned tokens as number in yyyyMMdd format.
	 */
	public static void setTokenLimitDate(Player player, String mode, long dateLong) {
		playerConfigSet(player, "tokenlimit." + mode + ".date", dateLong, false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mode The gamemode for the limit.
	 * 
	 * @return The amount of limited tokens earned today.
	 */
	public static int getTokenLimitAmount(Player player, String mode) {
		return playerConfigGetInt(player, "tokenlimit." + mode + ".amount", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mode The gamemode for the limit.
	 * @param tokenAmount The new amount of limited tokens earned today.
	 */
	public static void setTokenLimitAmount(Player player, String mode, int tokenAmount) {
		playerConfigSet(player, "tokenlimit." + mode + ".amount", tokenAmount, false);
	}
	
// Experience, Currencies and Reputation

	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The character level.
	 */
	public static int getCharacterLevel(Player player) {
		return playerConfigGetInt(player, "level", true);
	}
	
	/**
	 * Increases the character level by 1.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void levelUpCharacter(Player player) {
		playerConfigSet(player, "stats.points.total", PlayerSkillConfigurator.getTotalStatpoints(player) + 2, true);
		playerConfigSet(player, "level", player.getLevel(), true);
		AchievementTracker.addProgress(player, WauzAchievementType.GAIN_LEVELS, 1);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The character experience.
	 */
	public static double getCharacterExperience(Player player) {
		return playerConfigGetDouble(player, "exp", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @param experience The new character experience.
	 */
	public static void setCharacterExperience(Player player, double experience) {
		playerConfigSet(player, "exp", experience, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of coins the character owns.
	 */
	public static long getCharacterCoins(Player player) {
		return playerConfigGetLong(player, "curenncies.coins", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param amount The new amount of coins the character owns.
	 */
	public static void setCharacterCoins(Player player, long amount) {
		playerConfigSet(player, "curenncies.coins", amount, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of soulstones the character owns.
	 */
	public static long getCharacterSoulstones(Player player) {
		return playerConfigGetLong(player, "curenncies.souls", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param amount The new amount of medals the character owns.
	 */
	public static void setCharacterMedals(Player player, long amount) {
		playerConfigSet(player, "curenncies.medals", amount, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of medals the character owns.
	 */
	public static long getCharacterMedals(Player player) {
		return playerConfigGetLong(player, "curenncies.medals", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param amount The new amount of soulstones the character owns.
	 */
	public static void setCharacterSoulstones(Player player, long amount) {
		playerConfigSet(player, "curenncies.souls", amount, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param currency The type of currency.
	 * 
	 * @return The amount of the given currency the character owns.
	 */
	public static long getCharacterCurrency(Player player, String currency) {
		if(currency.equals("tokens")) {
			return getTokens(player);
		}
		else {
			return playerConfigGetLong(player, "curenncies." + currency, true);
		}
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param currency The type of currency.
	 * @param amount The new amount of the given currency the character owns.
	 */
	public static void setCharacterCurrency(Player player, String currency, long amount) {
		if(currency.equals("tokens")) {
			setTokens(player, amount);
		}
		else {
			playerConfigSet(player, "curenncies." + currency, amount, true);
		}
	}
	
// Achievements

	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of achievements the player earned.
	 */
	public static int getCharacterCompletedAchievements(Player player) {
		return playerConfigGetInt(player, "achievements.completed", true);
	}
	
	/**
	 * Adds 1 to the amount of achievements the player earned.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void addCharacterCompletedAchievements(Player player) {
		playerConfigSet(player, "achievements.completed", getCharacterCompletedAchievements(player) + 1, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param type The type of the generic achievement.
	 * 
	 * @return The progress of the generic achievement.
	 */
	public static double getCharacterAchievementProgress(Player player, WauzAchievementType type) {
		return playerConfigGetDouble(player, "achievements.generic." + type.getKey(), true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param type The type of the generic achievement.
	 * @param progress The progress of the generic achievement.
	 */
	public static void setCharacterAchievementProgress(Player player, WauzAchievementType type, double progress) {
		playerConfigSet(player, "achievements.generic." + type.getKey(), progress, true);
	}

}
