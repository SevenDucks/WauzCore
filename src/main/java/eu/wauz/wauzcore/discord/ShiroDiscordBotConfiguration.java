package eu.wauz.wauzcore.discord;

import eu.wauz.wauzcore.data.DiscordConfigurator;

/**
 * The configuration values of the Discord bot.
 * 
 * @author Wauzmons
 */
public class ShiroDiscordBotConfiguration {
	
	/**
	 * The token for the Discord bot user.
	 */
	public static final String TOKEN = DiscordConfigurator.getToken();
	
	/**
	 * The Discord ID of the admin user.
	 */
	public static final String ADMIN = DiscordConfigurator.getAdminUserId();
	
	/**
	 * The IP of the server that should handle bot commands.
	 */
	public static final String MAIN_SERVER_IP = DiscordConfigurator.getMainServerIp();
	
	/**
	 * The text shown under the bot in the Discord user list.
	 */
	public static final String PLAYS_MESSAGE = DiscordConfigurator.getPlaysMessage();
	
	/**
	 * The Discord message when an error occurs.
	 */
	public static final String ERROR_MESSAGE = DiscordConfigurator.getErrorMessage();

}
