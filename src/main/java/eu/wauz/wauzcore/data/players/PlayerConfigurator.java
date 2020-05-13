package eu.wauz.wauzcore.data.players;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerConfigurationUtils;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.quests.QuestSlot;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * Configurator to fetch or modify data from the Player.yml files.
 * 
 * @author Wauzmons
 */
public class PlayerConfigurator extends PlayerConfigurationUtils {
	
// General Parameters
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The rank of the player.
	 */
	public static String getRank(OfflinePlayer player) {
		return playerConfigGetString(player, "rank", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The survival score of the player.
	 */
	public static long getSurvivalScore(OfflinePlayer player) {
		return playerConfigGetLong(player, "score.survival", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param score The new survival score of the player.
	 */
	public static void setSurvivalScore(OfflinePlayer player, long score) {
		playerConfigSet(player, "score.survival", score, false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The last played time of the player.
	 */
	public static String getLastPlayed(OfflinePlayer player) {
		return WauzDateUtils.formatTimeSince(playerConfigGetLong(player, "lastplayed", false));
	}
	
	/**
	 * Updates the last played time of the player to now.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void setLastPlayed(OfflinePlayer player) {
		playerConfigSet(player, "lastplayed", System.currentTimeMillis(), false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The guild of the player.
	 */
	public static WauzPlayerGuild getGuild(OfflinePlayer player) {
		return WauzPlayerGuild.getGuild(playerConfigGetString(player, "guild", false));
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param guildUuidString The uuid of the new guild of the player.
	 */
	public static void setGuild(OfflinePlayer player, String guildUuidString) {
		playerConfigSet(player, "guild", guildUuidString, false);
	}
	
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
	
// Characters
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return If the character exists.
	 */
	public static boolean doesCharacterExist(OfflinePlayer player, int slot) {
		return playerConfigGetBoolean(player, "char" + slot + ".exists", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return The last login of the character.
	 */
	public static String getLastCharacterLogin(OfflinePlayer player, int slot) {
		return WauzDateUtils.formatTimeSince(playerConfigGetLong(player, "char" + slot + ".lastplayed", false));
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return The race / class of the character.
	 */
	public static String getRaceString(OfflinePlayer player, int slot) {
		return playerConfigGetString(player, "char" + slot + ".race", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return The world of the character.
	 */
	public static String getWorldString(OfflinePlayer player, int slot) {
		return playerConfigGetString(player, "char" + slot + ".pos.world", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return The level of the character.
	 */
	public static String getLevelString(OfflinePlayer player, int slot) {
		return playerConfigGetString(player, "char" + slot + ".level", false);
	}
	
// Character Parameters
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The reace / class of the selected character.
	 */
	public static String getCharacterRace(Player player) {
		return playerConfigGetString(player, "race", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The tabard name of the selected character.
	 */
	public static String getCharacterTabard(Player player) {
		return playerConfigGetString(player, "options.tabard", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @param tabardName The new tabard name of the selected character.
	 */
	public static void setCharacterTabard(Player player, String tabardName) {
		playerConfigSet(player, "options.tabard", tabardName, true);
	}
	
// Locations
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The name of the character's world.
	 */
	public static String getCharacterWorldString(Player player) {
		return playerConfigGetString(player, "pos.world", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The character spawn location.
	 */
	public static Location getCharacterSpawn(Player player) {
		return playerConfigGetLocation(player, "pos.spawn", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The character location.
	 */
	public static Location getCharacterLocation(Player player) {
		return playerConfigGetLocation(player, "pos.location", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param location The new  character location.
	 */
	public static void setCharacterLocation(Player player, Location location) {
		playerConfigSet(player, "pos.location", location.getX() + " " + location.getY() + " " + location.getZ(), true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The character home location.
	 */
	public static Location getCharacterHearthstone(Player player) {
		return playerConfigGetLocation(player, "pos.innloc", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The character home location name.
	 */
	public static String getCharacterHearthstoneRegion(Player player) {
		return playerConfigGetString(player, "pos.innreg", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param location The new character home location.
	 * @param regionName The new character home location name.
	 */
	public static void setCharacterHearthstone(Player player, Location location, String regionName) {
		playerConfigSet(player, "pos.innloc", location.getX() + " " + location.getY() + " " + location.getZ(), true);
		playerConfigSet(player, "pos.innreg", regionName, true);
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
		playerConfigSet(player, "stats.points.total", PlayerPassiveSkillConfigurator.getTotalStatpoints(player) + 2, true);
		playerConfigSet(player, "level", player.getLevel(), true);
		AchievementTracker.addProgress(player, WauzAchievementType.GAIN_LEVELS, 1);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The character experience.
	 */
	public static double getCharacterExperience(Player player) {
		return playerConfigGetDouble(player, "reput.exp", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @param experience The new character experience.
	 */
	public static void setCharacterExperience(Player player, double experience) {
		playerConfigSet(player, "reput.exp", experience, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of coins the character owns.
	 */
	public static long getCharacterCoins(Player player) {
		return playerConfigGetLong(player, "reput.coins", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param amount The new amount of coins the character owns.
	 */
	public static void setCharacterCoins(Player player, long amount) {
		playerConfigSet(player, "reput.coins", amount, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of soulstones the character owns.
	 */
	public static long getCharacterSoulstones(Player player) {
		return playerConfigGetLong(player, "reput.souls", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of republic wauzland reputation the character owns.
	 */
	public static long getCharacterRepRepublicWauzland(Player player) {
		return playerConfigGetLong(player, "reput.wauzland", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of eternal empire reputation the character owns.
	 */
	public static long getCharacterRepEternalEmpire(Player player) {
		return playerConfigGetLong(player, "reput.empire", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of dark legion reputation the character owns.
	 */
	public static long getCharacterRepDarkLegion(Player player) {
		return playerConfigGetLong(player, "reput.legion", true);
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
			return playerConfigGetLong(player, currency, true);
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
			playerConfigSet(player, currency, amount, true);
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
	
// Cooldowns
	
	/**
	 * @param player The player that owns the config file.
	 * @param actionId The action that the cooldown belongs to.
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean isCharacterCooldownReady(Player player, String actionId) {
		Long cooldown = playerConfigGetLong(player, "cooldown." + actionId, true);
		return cooldown == null || cooldown <= System.currentTimeMillis();
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param actionId The action that the cooldown belongs to.
	 * @param cooldown The new cooldown in milliseconds from now.
	 */
	public static void updateCharacterCooldown(Player player, String actionId, Long cooldown) {
		playerConfigSet(player, "cooldown." + actionId, cooldown + System.currentTimeMillis(), true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The remaining effect ticks of pvp protection.
	 */
	public static short getPvPProtectionTicks(Player player) {
		return (short) (0 + playerConfigGetInt(player, "pvp.resticks", true));
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param ticks The new remaining effect ticks of pvp protection.
	 */
	public static void setPvPProtectionTicks(Player player, short ticks) {
		playerConfigSet(player, "pvp.resticks", ticks, true);
	}
	
// Tracker
	
	/**
	 * @param player The player that owns the config file.
	 * @param location The new tracked location.
	 * @param name The new name of the tracked location.
	 */
	public static void setTrackerDestination(Player player, Location location, String name) {
		String locationString = location.getX() + " " + location.getY() + " " + location.getZ();
		playerConfigSet(player, "tracker.coords", locationString, true);
		playerConfigSet(player, "tracker.name", name, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The tracked location.
	 */
	public static Location getTrackerDestinationLocation(Player player) {
		return playerConfigGetLocation(player, "tracker.coords", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The name of the tracked location.
	 */
	public static String getTrackerDestinationName(Player player) {
		return playerConfigGetString(player, "tracker.name", true)
				+ " " + playerConfigGetString(player, "tracker.coords", true);
	}
	
// Arrows
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The selected arrow type.
	 */
	public static String getSelectedArrows(Player player) {
		return playerConfigGetString(player, "arrows.selected", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @param type The new selected arrow type.
	 */
	public static void setSelectedArrowType(Player player, String type) {
		playerConfigSet(player, "arrows.selected", type, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param type The arrow type.
	 * 
	 * @return The amount of arrows.
	 */
	public static int getArrowAmount(Player player, String type) {
		return type.equals("normal") ? 999 : playerConfigGetInt(player, "arrows.amount." + type, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param type The arrow type.
	 * @param amount The new amount of arrows.
	 */
	public static void setArrowAmount(Player player, String type, int amount) {
		playerConfigSet(player, "arrows.amount." + type, amount, true);
	}
	
// Quests - Options
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return If special quests should be hidden.
	 */
	public static boolean getHideSpecialQuestsForCharacter(Player player) {
		return playerConfigGetBoolean(player, "options.hideSpecialQuests", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param value If special quests should now be hidden.
	 */
	public static void setHideSpecialQuestsForCharacter(Player player, boolean value) {
		playerConfigSet(player, "options.hideSpecialQuests", value, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return If completed quests should be hidden.
	 */
	public static boolean getHideCompletedQuestsForCharacter(Player player) {
		return playerConfigGetBoolean(player, "options.hideCompletedQuests", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param value If completed quests should now be hidden.
	 */
	public static void setHideCompletedQuestsForCharacter(Player player, boolean value) {
		playerConfigSet(player, "options.hideCompletedQuests", value, true);
	}
	
// Quests
	
	/**
	 * @param player The player that owns the config file.
	 * @param questSlot The slot to set the quest to.
	 * @param questName The name of the quest.
	 */
	public static void setCharacterQuestSlot(Player player, String questSlot, String questName) {
		playerConfigSet(player, questSlot, questName, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The name of the main quest.
	 */
	public static String getCharacterRunningMainQuest(Player player) {
		return playerConfigGetString(player, QuestSlot.MAIN.getConfigKey(), true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The name of the first campaign quest.
	 */
	public static String getCharacterRunningCampaignQuest1(Player player) {
		return playerConfigGetString(player, QuestSlot.CAMPAIGN1.getConfigKey(), true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The name of the second campaign quest.
	 */
	public static String getCharacterRunningCampaignQuest2(Player player) {
		return playerConfigGetString(player, QuestSlot.CAMPAIGN2.getConfigKey(), true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The name of the first daily quest.
	 */
	public static String getCharacterRunningDailyQuest1(Player player) {
		return playerConfigGetString(player, QuestSlot.DAILY1.getConfigKey(), true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The name of the second daily quest.
	 */
	public static String getCharacterRunningDailyQuest2(Player player) {
		return playerConfigGetString(player, QuestSlot.DAILY2.getConfigKey(), true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The name of the third daily quest.
	 */
	public static String getCharacterRunningDailyQuest3(Player player) {
		return playerConfigGetString(player, QuestSlot.DAILY3.getConfigKey(), true);
	}
	
// Friends
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The list of uuids from the player's friends.
	 */
	public static List<String> getFriendsList(OfflinePlayer player) {
		return playerConfigGetStringList(player, "friends", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param friendsList The new list of uuids from the player's friends.
	 */
	public static void setFriendsList(OfflinePlayer player, List<String> friendsList) {
		playerConfigSet(player, "friends", friendsList, false);
	}
	
}
