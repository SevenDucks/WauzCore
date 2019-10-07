package eu.wauz.wauzdiscord;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class WauzDiscord extends JavaPlugin {

	/**
	 * The instance of this Class, that is created by the Minecraft Server.
	 */
	private static WauzDiscord instance;
	
	/**
	 * The Discord Bot running from this Server.
	 */
	private static ShiroDiscordBot shiroDiscordBot;

	/**
	 * Gets called when the Server is started.
	 * 1. Creates a new Discord Bot Instance and logs it in.
	 * 2. Registers the Event Listeners.
	 * 
	 * @see ShiroDiscordBot
	 * @see WauzDiscordListener
	 */
	@Override
	public void onEnable() {
		instance = this;
		
		/**
		 * Print the Version
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
	}
	
	/**
	 * Gets called when the Server is stopped.
	 * Logs out and stops the Discord Bot.
	 * 
	 * @see ShiroDiscordBot#stop()
	 */
	@Override
	public void onDisable() {
		shiroDiscordBot.stop();
		getLogger().info("Shiro's taking a nap!");
	}
	
	/**
	 * Listens for incoming Commands from the plugin.xml.
	 * Used to send messages to the Bot per Ingame Command.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("discord") && args.length >= 1) {
			shiroDiscordBot.sendMessageFromMinecraft(StringUtils.join(args, " "));
		}
		return true;
	}

	/**
	 * @return The instance of this Class, that is created by the Minecraft Server.
	 */
	public static WauzDiscord getInstance() {
		return instance;
	}
	
	/**
	 * @return The Discord Bot running from this Server.
	 */
	public static ShiroDiscordBot getShiroDiscordBot() {
		return shiroDiscordBot;
	}

}
