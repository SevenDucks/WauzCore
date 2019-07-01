package eu.wauz.wauzcore.data.players;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.api.PlayerConfigurationUtils;

public class GuildConfigurator extends PlayerConfigurationUtils {

// Guild Files
	
	public static List<String> getGuildUuidList() {
		return PlayerConfigurationUtils.getGuildUuidList();
	}
	
	public static void deleteGuild(String guild) {
		PlayerConfigurationUtils.deleteGuild(guild);
	}
	
// General Parameters
	
	public static String getGuildName(String guild) {
		return guildConfigGetString(guild, "name");
	}
	
	public static void setGuildName(String guild, String name) {
		guildConfigSet(guild, "name", name);
	}
	
	public static String getGuildDescription(String guild) {
		return guildConfigGetString(guild, "description");
	}
	
	public static void setGuildDescription(String guild, String description) {
		guildConfigSet(guild, "description", description);
	}
	
	public static ItemStack getGuildTabard(String guild) {
		return guildConfigGetItemStack(guild, "tabard");
	}
	
	public static void setGuildTabard(String guild, ItemStack tabard) {
		guildConfigSet(guild, "tabard", tabard);
	}
	
// Guild Members
	
	public static String getGuildLeaderUuidString(String guild) {
		return guildConfigGetString(guild, "leader");
	}
	
	public static void setGuildLeaderUuidString(String guild, String leaderUuidString) {
		guildConfigSet(guild, "leader", leaderUuidString);
	}
	
	public static List<String> getGuildOfficerUuidStrings(String guild) {
		return guildConfigGetStringList(guild, "officers");
	}
	
	public static void setGuildOfficerUuidStrings(String guild, List<String> officerUuidStrings) {
		guildConfigSet(guild, "officers", officerUuidStrings);
	}
	
	public static List<String> getGuildMemberUuidStrings(String guild) {
		return guildConfigGetStringList(guild, "members");
	}
	
	public static void setGuildMemberUuidStrings(String guild, List<String> memberUuidStrings) {
		guildConfigSet(guild, "members", memberUuidStrings);
	}
	
	public static List<String> getGuildApplicantUuidStrings(String guild) {
		return guildConfigGetStringList(guild, "applicants");
	}
	
	public static void setGuildApplicantUuidStrings(String guild, List<String> applicantUuidStrings) {
		guildConfigSet(guild, "applicants", applicantUuidStrings);
	}
	
// Guild Upgrades
	
	public static int getUpgradeAdditionalSlots(String guild) {
		return guildConfigGetInt(guild, "upgrades.slots");
	}
	
	public static void setUpgradeAdditionalSlots(String guild, int tier) {
		guildConfigSet(guild, "upgrades.slots", tier);
	}

}
