package eu.wauz.wauzcore.data.players;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerConfigurationUtils;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.WauzRank;
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
	 * @param rank The new rank of the player.
	 */
	public static void setRank(OfflinePlayer player, WauzRank rank) {
		playerConfigSet(player, "rank", rank.getRankName(), false);
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
	
// Characters
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return If the character exists.
	 */
	public static boolean doesCharacterExist(OfflinePlayer player, String slot) {
		return playerConfigGetBoolean(player, "char" + slot + ".exists", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return The schema version of the character file.
	 */
	public static int getCharecterSchemaVersion(OfflinePlayer player, String slot) {
		return playerConfigGetInt(player, "char" + slot + ".schemaversion", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return The last login of the character.
	 */
	public static String getLastCharacterLogin(OfflinePlayer player, String slot) {
		return WauzDateUtils.formatTimeSince(playerConfigGetLong(player, "char" + slot + ".lastplayed", false));
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return The world of the character.
	 */
	public static String getWorldString(OfflinePlayer player, String slot) {
		return playerConfigGetString(player, "char" + slot + ".pos.world", false);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The slot of the character.
	 * 
	 * @return The level of the character.
	 */
	public static String getLevelString(OfflinePlayer player, String slot) {
		return playerConfigGetString(player, "char" + slot + ".level", false);
	}
	
// Character Parameters
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The class of the selected character.
	 */
	public static String getCharacterClass(Player player) {
		return playerConfigGetString(player, "class", true);
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
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The title name of the selected character.
	 */
	public static String getCharacterTitle(Player player) {
		return playerConfigGetString(player, "options.title", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @param titleName The new title name of the selected character.
	 */
	public static void setCharacterTitle(Player player, String titleName) {
		playerConfigSet(player, "options.title", titleName, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The choosable titles of the selected character.
	 */
	public static List<String> getCharacterTitleList(Player player) {
		return playerConfigGetStringList(player, "options.titlelist", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param titleList The new choosable titles of the selected character.
	 */
	public static void setCharacterTitleList(Player player, List<String> titleList) {
		playerConfigSet(player, "options.titlelist", titleList, true);
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
