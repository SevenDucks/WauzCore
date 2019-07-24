package eu.wauz.wauzdiscord;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class WauzDiscord extends JavaPlugin {

	private static WauzDiscord instance;
	
	private static ShiroDiscordBot shiroDiscordBot;

	@Override
	public void onEnable() {
		instance = this;
		
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
	}
	
	@Override
	public void onDisable() {
		shiroDiscordBot.stop();
		getLogger().info("Shiro's taking a nap!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("discord") && args.length >= 1) {
			shiroDiscordBot.sendMessage("**Minecraft**: `" + ChatColor.stripColor(StringUtils.join(args, " ")) + "`");
		}
		return true;
	}

	public static WauzDiscord getInstance() {
		return instance;
	}
	
	public static ShiroDiscordBot getShiroDiscordBot() {
		return shiroDiscordBot;
	}

}
