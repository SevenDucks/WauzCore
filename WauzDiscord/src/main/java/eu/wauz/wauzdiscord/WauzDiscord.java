package eu.wauz.wauzdiscord;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzdiscord.data.DiscordConfigurator;

/**
 * The main class of this module, used to connect Minecraft with Discord.
 * Responsible for executing bot commands and connecting chats.
 * 
 * @author Wauzmons
 */
public class WauzDiscord extends JavaPlugin {

	/**
	 * The instance of this class, that is created by the Minecraft server.
	 */
	private static WauzDiscord instance;
	
	/**
	 * The Discord bot running on the server.
	 */
	private static ShiroDiscordBot shiroDiscordBot;
	
	/**
	 * The filter used to listen to log records from Bukkit.
	 */
	private WauzLogFilter logFilter;

	/**
	 * Gets called when the server is started.
	 * 1. Creates a new Discord bot instance and logs it in.
	 * 2. Registers the event listeners.
	 * 3. Adds the log filter.
	 * 4. Starts all repeating tasks.
	 * 5. Sends a start message to Discord, if enabled.
	 * 
	 * @see ShiroDiscordBot
	 * @see WauzDiscordListener
	 * @see DiscordConfigurator#showStartStopNotification()
	 */
	@Override
	public void onEnable() {
		instance = this;
		getLogger().info("O~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-O");
		getLogger().info(" _    _                                           ");
		getLogger().info("| |  | | WauzDiscord running WauzCore v" + WauzCore.getInstance().getDescription().getVersion());
		getLogger().info("| |  | | __ _ _   _ _____ __ ___   ___  _ __  ___ ");
		getLogger().info("| |/\\| |/ _` | | | |_  / '_ ` _ \\ / _ \\| '_ \\/ __|");
		getLogger().info("\\  /\\  / (_| | |_| |/ /| | | | | | (_) | | | \\__ \\");
		getLogger().info(" \\/  \\/ \\__,_|\\__,_/___|_| |_| |_|\\___/|_| |_|___/");
		getLogger().info("");
		getLogger().info("O-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~O");
		
		shiroDiscordBot = new ShiroDiscordBot();
		getLogger().info("Shiro's body is ready!");
		
		getServer().getPluginManager().registerEvents(new WauzDiscordListener(), this);
		getLogger().info("Registered EventListeners!");
		
		logFilter = new WauzLogFilter();
		((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(logFilter);
		getLogger().info("Added LogFilter!");
		
		/**
		 * Every 12 minutes
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				shiroDiscordBot.updateGeneralChannelServerStats();
				shiroDiscordBot.updateLoggingChannelServerStats();
			}
			
		}, 14400, 14400);
		
		getLogger().info("Scheduled Repeating Tasks!");
		
		if(DiscordConfigurator.showStartStopNotification()) {
			shiroDiscordBot.sendEmbedFromMinecraft(null, ":white_check_mark: " + WauzCore.getServerKey()
					+ " has been started!", null, Color.GREEN, false);
		}
	}
	
	/**
	 * Gets called when the server is stopped.
	 * Logs out and stops the Discord bot and sends a Discord notification, if enabled.
	 * 
	 * @see WauzLogHandler#close()
	 * @see ShiroDiscordBot#stop()
	 * @see DiscordConfigurator#showStartStopNotification()
	 */
	@Override
	public void onDisable() {
		if(DiscordConfigurator.showStartStopNotification()) {
			shiroDiscordBot.sendEmbedFromMinecraft(null, ":octagonal_sign: " + WauzCore.getServerKey()
					+ " has been stopped!", null, Color.RED, false);
		}
		
		logFilter.close();
		shiroDiscordBot.stop();
		getLogger().info("Shiro's taking a nap!");
	}
	
	/**
	 * Listens for incoming commands from the plugin.xml.
	 * Used to send messages to the bot per ingame commands.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("discord") && args.length >= 1) {
			shiroDiscordBot.sendMessageFromMinecraft(StringUtils.join(args, " "), false);
		}
		return true;
	}

	/**
	 * @return The instance of this class, that is created by the Minecraft server.
	 */
	public static WauzDiscord getInstance() {
		return instance;
	}
	
	/**
	 * @return The Discord bot running on this server.
	 */
	public static ShiroDiscordBot getShiroDiscordBot() {
		return shiroDiscordBot;
	}

}
