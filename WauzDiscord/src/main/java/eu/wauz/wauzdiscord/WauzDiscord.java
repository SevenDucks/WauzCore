package eu.wauz.wauzdiscord;

import java.awt.Color;

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
	 * The Discord bot running on this server.
	 */
	private static ShiroDiscordBot shiroDiscordBot;

	/**
	 * Gets called when the server is started.
	 * 1. Creates a new Discord bot instance and logs it in.
	 * 2. Registers the event listeners.
	 * 3. Sends a start message to Discord, if enabled.
	 * 
	 * @see ShiroDiscordBot
	 * @see WauzDiscordListener
	 * @see DiscordConfigurator#showStartStopNotification()
	 */
	@Override
	public void onEnable() {
		instance = this;
		
		/**
		 * Print the version
		 */
		getLogger().info("O~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-O");
		getLogger().info(" _    _                                           ");
		getLogger().info("| |  | | WauzDiscord v" + getDescription().getVersion());
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
		
		if(DiscordConfigurator.showStartStopNotification()) {
			shiroDiscordBot.sendEmbedFromMinecraft(WauzCore.getServerKey() + " has been (re)started!", Color.GREEN);
		}
	}
	
	/**
	 * Gets called when the server is stopped.
	 * Logs out and stops the Discord bot and sends a Discord notification, if enabled.
	 * 
	 * @see ShiroDiscordBot#stop()
	 * @see DiscordConfigurator#showStartStopNotification()
	 */
	@Override
	public void onDisable() {
		if(DiscordConfigurator.showStartStopNotification()) {
			shiroDiscordBot.sendEmbedFromMinecraft(WauzCore.getServerKey() + " has been stopped!", Color.RED);
		}
		
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
			shiroDiscordBot.sendMessageFromMinecraft(StringUtils.join(args, " "));
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
