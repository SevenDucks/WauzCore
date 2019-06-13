package eu.wauz.wauzcore.system;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.api.ShiroDiscordBot;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class ChatFormatter {
	
	public static final String ICON_PGRPH = "\u00A7";
	
	public static final String ICON_DEGRS = "\u00B0";
	
	public static final String ICON_CARET = "\u00BB";
	
	public static final String ICON_BULLT = "\u2022";
	
	public static final String ICON_DIAMS = "\u2666";
	
	public static final String ICON_HEART = "\u2764";
	
	private static ShiroDiscordBot shiroDiscordBot = WauzCore.getShiroDiscordBot();

	public static String global(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		ChatColor rank;
		if(player.hasPermission("wauz.system")) {
			rank = ChatColor.GOLD;
		} else {
			rank = ChatColor.GREEN;
		}
		
		String level;
		if(WauzMode.inHub(player)) {
			level = "Hub";
		}
		else if(WauzMode.isSurvival(player)) {
			level = "Survival";
		}
		else {
			level = "" + player.getLevel();
		}
		
		String msg = ChatColor.WHITE + "[" + rank + player.getDisplayName() + ChatColor.WHITE + " (" +
					 ChatColor.AQUA  + level + ChatColor.WHITE + ")] " +
					 ChatColor.GRAY + event.getMessage();
		
		shiroDiscordBot.sendMessage("**Minecraft**: `" + ChatColor.stripColor(msg) + "`");
		return msg;
	}
	
	public static boolean group(Player player, String message) {
		WauzPlayerGroup pg = WauzPlayerGroupPool.getGroup(player);
		if(pg == null) {
			player.sendMessage(ChatColor.RED + "You are not in a group!");
			return true;
		}
		if(StringUtils.isBlank(message)) {
			player.sendMessage(ChatColor.RED + "Please specify the text to send!");
			return false;
		}
		
		ChatColor rank;
		if(player.hasPermission("wauz.system")) {
			rank = ChatColor.GOLD;
		} else {
			rank = ChatColor.GREEN;
		}
		
		String msg = ChatColor.WHITE + "[" + rank + player.getDisplayName() + ChatColor.WHITE + " (" +
				 ChatColor.BLUE + "Group" + ChatColor.WHITE + ")] " +
				 ChatColor.GRAY + message;
		
		for(Player member : pg.getPlayers())
			member.sendMessage(msg);
		
		return true;
	}
	
	public static boolean guild(Player player, String message) {
		WauzPlayerGuild pg = PlayerConfigurator.getGuild(player);
		if(pg == null) {
			player.sendMessage(ChatColor.RED + "You are not in a guild!");
			return true;
		}
		if(StringUtils.isBlank(message)) {
			player.sendMessage(ChatColor.RED + "Please specify the text to send!");
			return false;
		}
		
		ChatColor rank;
		if(player.hasPermission("wauz.system")) {
			rank = ChatColor.GOLD;
		} else {
			rank = ChatColor.GREEN;
		}
		
		String msg = ChatColor.WHITE + "[" + rank + player.getDisplayName() + ChatColor.WHITE + " (" +
				 ChatColor.GREEN + "Guild" + ChatColor.WHITE + ")] " +
				 ChatColor.GRAY + message;
		
		pg.sendMessageToGuildMembers(msg);
		
		return true;
	}
	
	public static void discord(String message, String user, boolean admin) {
		ChatColor rank;
		if(admin) {
			rank = ChatColor.GOLD;
		} else {
			rank = ChatColor.GREEN;
		}
		
		String msg = ChatColor.WHITE + "[" + rank + user + ChatColor.WHITE + " (" +
				 ChatColor.DARK_AQUA  + "Discord" + ChatColor.WHITE + ")] " +
				 ChatColor.GRAY + message;
		
		Bukkit.broadcastMessage(msg);
	}

}
