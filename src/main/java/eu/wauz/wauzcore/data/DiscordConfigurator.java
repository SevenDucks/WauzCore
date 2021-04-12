package eu.wauz.wauzcore.data;

import java.util.List;

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
	 * @return The text shown under the bot in the Discord user list.
	 */
	public static String getPlaysMessage() {
		return mainConfigGetString("Discord", "playsmsg");
	}
	
// Channel IDs
	
	/**
	 * @return The ID of the general channel for the chat.
	 */
	public static long getGeneralChannelId() {
		return mainConfigGetLong("Discord", "channels.general");
	}
	
	/**
	 * @return The ID of the logging channel for the server log.
	 */
	public static long getLoggingChannelId() {
		return mainConfigGetLong("Discord", "channels.logging");
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
	
	/**
	 * @return If Discord notifications about player deaths should be shown.
	 */
	public static boolean showDeathNotification() {
		return mainConfigGetBoolean("Discord", "notifications.death");
	}
	
	/**
	 * @return The list of log entries, that shouldn't be forwarded to Discord.
	 */
	public static List<String> getIgnoredLogMessages() {
		return mainConfigGetStringList("Discord", "ignorelog");
	}

}
