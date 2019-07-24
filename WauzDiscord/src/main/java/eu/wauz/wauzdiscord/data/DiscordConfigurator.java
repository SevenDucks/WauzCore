package eu.wauz.wauzdiscord.data;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

public class DiscordConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	public static String getToken() {
		return mainConfigGetString("Discord", "token");
	}
	
	public static String getAdminUserId() {
		return mainConfigGetString("Discord", "admin");
	}
	
	public static String getMainServerIp() {
		return mainConfigGetString("Discord", "mainserver");
	}
	
	public static String getPlaysMessage() {
		return mainConfigGetString("Discord", "playsmsg");
	}
	
	public static String getErrorMessage() {
		return mainConfigGetString("Discord", "errormsg");
	}
	
// Channel IDs
	
	public static long getGuildId() {
		return mainConfigGetLong("Discord", "guild");
	}
	
	public static long getGeneralChannelId() {
		return mainConfigGetLong("Discord", "channels.general");
	}
	
	public static long getBotsChannelId() {
		return mainConfigGetLong("Discord", "channels.bots");
	}
	
	public static long getAudioChannelId() {
		return mainConfigGetLong("Discord", "channels.audio");
	}

}
