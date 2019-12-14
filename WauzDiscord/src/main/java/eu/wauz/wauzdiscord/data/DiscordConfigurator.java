package eu.wauz.wauzdiscord.data;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Discord.yml.
 * 
 * @author Wauzmons
 */
public class DiscordConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @return The token for the Discord bot user.
	 */
	public static String getToken() {
		return mainConfigGetString("Discord", "token");
	}
	
	/**
	 * @return The Discord ID of the admin user.
	 */
	public static String getAdminUserId() {
		return mainConfigGetString("Discord", "admin");
	}
	
	/**
	 * @return The IP of the server that should handle bot commands.
	 */
	public static String getMainServerIp() {
		return mainConfigGetString("Discord", "mainserver");
	}
	
	/**
	 * @return The text shown under the bot in the Discord user list.
	 */
	public static String getPlaysMessage() {
		return mainConfigGetString("Discord", "playsmsg");
	}
	
	/**
	 * @return The Discord message when an error occurs.
	 */
	public static String getErrorMessage() {
		return mainConfigGetString("Discord", "errormsg");
	}
	
// Channel IDs
	
	/**
	 * @return The ID of the Discord server.
	 */
	public static long getGuildId() {
		return mainConfigGetLong("Discord", "guild");
	}
	
	/**
	 * @return The ID of the general channel for the chat.
	 */
	public static long getGeneralChannelId() {
		return mainConfigGetLong("Discord", "channels.general");
	}
	
	/**
	 * @return The ID of the bots channel for commands.
	 */
	public static long getBotsChannelId() {
		return mainConfigGetLong("Discord", "channels.bots");
	}
	
	/**
	 * @return The ID of the audio channel for playing songs.
	 */
	public static long getAudioChannelId() {
		return mainConfigGetLong("Discord", "channels.audio");
	}
	
// Notifications
	
	/**
	 * @return If Discord notifications about the server starting / stopping should be shown.
	 */
	public static boolean showStartStopNotification() {
		return mainConfigGetBoolean("Discord", "notifications.startstop"); 
	}
	
	/**
	 * @return If Discord notifications about players joining / leaving should be shown.
	 */
	public static boolean showJoinLeaveNotification() {
		return mainConfigGetBoolean("Discord", "notifications.joinleave"); 
	}

}
