package eu.wauz.wauzcore.data.players;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.api.GuildConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Guild.yml files.
 * 
 * @author Wauzmons
 */
public class GuildConfigurator extends GuildConfigurationUtils {

// Guild Files
	
	/**
	 * @return All guild uuids.
	 */
	public static List<String> getGuildUuidList() {
		return GuildConfigurationUtils.getGuildUuidList();
	}
	
	/**
	 * @param guild The uuid of the guild to delete.
	 */
	public static void deleteGuild(String guild) {
		GuildConfigurationUtils.deleteGuild(guild);
	}
	
// General Parameters
	
	/**
	 * @param guild The uuid of the guild.
	 * 
	 * @return The name of the guild.
	 */
	public static String getGuildName(String guild) {
		return guildConfigGetString(guild, "name");
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * @param name The new name of the guild.
	 */
	public static void setGuildName(String guild, String name) {
		guildConfigSet(guild, "name", name);
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * 
	 * @return The description of the guild.
	 */
	public static String getGuildDescription(String guild) {
		return guildConfigGetString(guild, "description");
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * @param description The new description of the guild.
	 */
	public static void setGuildDescription(String guild, String description) {
		guildConfigSet(guild, "description", description);
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * 
	 * @return The tabard / banner of the guild.
	 */
	public static ItemStack getGuildTabard(String guild) {
		return guildConfigGetItemStack(guild, "tabard");
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * @param tabard The new tabard of the guild.
	 */
	public static void setGuildTabard(String guild, ItemStack tabard) {
		guildConfigSet(guild, "tabard", tabard);
	}
	
// Guild Members
	
	
	/**
	 * @param guild The uuid of the guild.
	 * 
	 * @return The leader of the guild.
	 */
	public static String getGuildLeaderUuidString(String guild) {
		return guildConfigGetString(guild, "leader");
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * @param leaderUuidString The uuid of the new leader of the guild.
	 */
	public static void setGuildLeaderUuidString(String guild, String leaderUuidString) {
		guildConfigSet(guild, "leader", leaderUuidString);
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * 
	 * @return The list of officers of the guild.
	 */
	public static List<String> getGuildOfficerUuidStrings(String guild) {
		return guildConfigGetStringList(guild, "officers");
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * @param officerUuidStrings The new list of officers of the guild.
	 */
	public static void setGuildOfficerUuidStrings(String guild, List<String> officerUuidStrings) {
		guildConfigSet(guild, "officers", officerUuidStrings);
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * 
	 * @return The list of members of the guild.
	 */
	public static List<String> getGuildMemberUuidStrings(String guild) {
		return guildConfigGetStringList(guild, "members");
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * @param memberUuidStrings The new list of members of the guild.
	 */
	public static void setGuildMemberUuidStrings(String guild, List<String> memberUuidStrings) {
		guildConfigSet(guild, "members", memberUuidStrings);
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * 
	 * @return The list of applicants of the guild.
	 */
	public static List<String> getGuildApplicantUuidStrings(String guild) {
		return guildConfigGetStringList(guild, "applicants");
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * @param applicantUuidStrings The new list of applicants of the guild.
	 */
	public static void setGuildApplicantUuidStrings(String guild, List<String> applicantUuidStrings) {
		guildConfigSet(guild, "applicants", applicantUuidStrings);
	}
	
// Guild Upgrades
	
	/**
	 * @param guild The uuid of the guild.
	 * 
	 * @return The slots upgrade level of the guild.
	 */
	public static int getUpgradeAdditionalSlots(String guild) {
		return guildConfigGetInt(guild, "upgrades.slots");
	}
	
	/**
	 * @param guild The uuid of the guild.
	 * @param tabard The new slots upgrade level of the guild.
	 */
	public static void setUpgradeAdditionalSlots(String guild, int tier) {
		guildConfigSet(guild, "upgrades.slots", tier);
	}

}
