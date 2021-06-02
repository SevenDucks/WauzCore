package eu.wauz.wauzcore.data;

import java.util.List;
import java.util.Set;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * Configurator to fetch or modify data from the Server.yml.
 * 
 * @author Wauzmons
 */
public class ServerConfigurator extends GlobalConfigurationUtils {
	
	/**
	 * The message of the day string with unfilled placeholders.
	 */
	private static String motdBase;
	
	/**
	 * Goodbye message part for the message of the day.
	 */
	private static String motdGoodbye;
	
	/**
	 * All possible randomized message parts for the message of the day.
	 */
	private static List<String> motdRandomMessages;
	
// General Parameters
	
	/**
	 * @return The key for the Minecraft server.
	 */
	public static String getServerKey() {
		return mainConfigGetString("Server", "key");
	}
	
	/**
	 * @return All modules that are activated on the server.
	 */
	public static List<String> getModules() {
		return mainConfigGetStringList("Server", "modules");
	}
	
// Embedded Servers
	
	/**
	 * @return The port for the embedded HTTP server.
	 */
	public static int getServerApiPort() {
		return mainConfigGetInt("Server", "apiport");
	}
	
	/**
	 * @return The port for the embedded FTP server.
	 */
	public static int getServerFtpPort() {
		return mainConfigGetInt("Server", "ftpport");
	}
	
	/**
	 * @return A list of all FTP users.
	 */
	public static Set<String> getServerFtpUsers() {
		return mainConfigGetKeys("Server", "ftpusers");
	}
	
	/**
	 * @param user The name of the FTP user.
	 * 
	 * @return The password of the user.
	 */
	public static String getFtpUserPassword(String user) {
		return mainConfigGetString("Server", "ftpusers." + user + ".pass");
	}
	
	/**
	 * @param user The name of the FTP user.
	 * 
	 * @return The home path of the user.
	 */
	public static String getFtpUserHomePath(String user) {
		return mainConfigGetString("Server", "ftpusers." + user + ".path");
	}
	
// Message of the Day
	
	/**
	 * @param playerName The name of the player to put in the message.
	 * 
	 * @return The message of the day.
	 */
	public static String getServerMotd(String playerName) {
		initMessagesParts();
		String motd = motdBase;
		if(playerName == null) {
			String randomMessage = motdRandomMessages.get(Chance.randomInt(motdRandomMessages.size()));
			motd = motd.replace("%random%", randomMessage);
		}
		else {
			String goodbyeMessage = motdGoodbye.replace("%player%", playerName);
			motd = motd.replace("%random%", goodbyeMessage);
		}
		return motd;
	}
	
	/**
	 * Loads and caches the message of the day parts from the config.
	 */
	private static void initMessagesParts() {
		if(motdBase == null) {
			motdBase = mainConfigGetString("Server", "motd.base");
		}
		if(motdGoodbye == null) {
			motdGoodbye = mainConfigGetString("Server", "motd.goodbye");
		}
		if(motdRandomMessages == null) {
			motdRandomMessages = RandomMessageConfiguration.getMotds();
		}
	}

}
