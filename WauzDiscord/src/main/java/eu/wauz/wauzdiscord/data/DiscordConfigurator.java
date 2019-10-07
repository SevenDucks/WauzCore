package eu.wauz.wauzdiscord.data;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to Fetch or Modify Data from the Discord.yml.
 * 
 * @author Wauzmons
 */
public class DiscordConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @return The Token for the Discord Bot User.
	 */
	public static String getToken() {
		return mainConfigGetString("Discord", "token");
	}
	
	/**
	 * @return The Discord ID of the Admin User.
	 */
	public static String getAdminUserId() {
		return mainConfigGetString("Discord", "admin");
	}
	
	/**
	 * @return The IP of the Server that should handle Bot Commands.
	 */
	public static String getMainServerIp() {
		return mainConfigGetString("Discord", "mainserver");
	}
	
	/**
	 * @return The Text shown under the Bot in the Discord User List.
	 */
	public static String getPlaysMessage() {
		return mainConfigGetString("Discord", "playsmsg");
	}
	
	/**
	 * @return The Discord Message when an Error occurs.
	 */
	public static String getErrorMessage() {
		return mainConfigGetString("Discord", "errormsg");
	}
	
// Channel IDs
	
	/**
	 * @return The ID of the Discord Server.
	 */
	public static long getGuildId() {
		return mainConfigGetLong("Discord", "guild");
	}
	
	/**
	 * @return The ID of the General Channel for the Chat.
	 */
	public static long getGeneralChannelId() {
		return mainConfigGetLong("Discord", "channels.general");
	}
	
	/**
	 * @return The ID of the Bots Channel for Commands.
	 */
	public static long getBotsChannelId() {
		return mainConfigGetLong("Discord", "channels.bots");
	}
	
	/**
	 * @return The ID of the Audio Channel for playing Songs.
	 */
	public static long getAudioChannelId() {
		return mainConfigGetLong("Discord", "channels.audio");
	}

}
