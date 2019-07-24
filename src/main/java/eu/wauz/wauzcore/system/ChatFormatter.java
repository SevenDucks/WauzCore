package eu.wauz.wauzcore.system;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class ChatFormatter {
	
	public static final String ICON_PGRPH = "\u00A7";
	
	public static final String ICON_DEGRS = "\u00B0";
	
	public static final String ICON_CARET = "\u00BB";
	
	public static final String ICON_BULLT = "\u2022";
	
	public static final String ICON_DIAMS = "\u2666";
	
	public static final String ICON_HEART = "\u2764";

	public static String global(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
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
		
		ChatColor rankColor = getMinecraftRankColor(player);
		String msg = ChatColor.WHITE + "[" + rankColor + player.getDisplayName() + ChatColor.WHITE + " (" +
					 ChatColor.AQUA  + level + ChatColor.WHITE + ")] " +
					 ChatColor.GRAY + event.getMessage();
		
		if(Bukkit.getPluginManager().isPluginEnabled("WauzDiscord")) {
			Bukkit.getScheduler().callSyncMethod(WauzCore.getInstance(),
					() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord " + msg));
		}
		return msg;
	}
	
	public static boolean group(Player player, String message) {
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
		if(!validateGroupMessage(player, playerGroup, message, false)) {
			return false;
		}
		
		ChatColor rankColor = getMinecraftRankColor(player);
		String msg = ChatColor.WHITE + "[" + rankColor + player.getDisplayName() + ChatColor.WHITE + " (" +
				 ChatColor.BLUE + "Group" + ChatColor.WHITE + ")] " +
				 ChatColor.GRAY + message;
		
		for(Player member : playerGroup.getPlayers())
			member.sendMessage(msg);
		
		return true;
	}
	
	public static boolean guild(Player player, String message) {
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		if(!validateGroupMessage(player, playerGuild, message, true)) {
			return false;
		}
		
		ChatColor rankColor = getMinecraftRankColor(player);
		String msg = ChatColor.WHITE + "[" + rankColor + player.getDisplayName() + ChatColor.WHITE + " (" +
				 ChatColor.GREEN + "Guild" + ChatColor.WHITE + ")] " +
				 ChatColor.GRAY + message;
		
		playerGuild.sendMessageToGuildMembers(msg);
		
		return true;
	}
	
	public static void discord(String message, String user, boolean admin) {
		ChatColor rankColor;
		if(admin) {
			rankColor = ChatColor.GOLD;
		} else {
			rankColor = ChatColor.GREEN;
		}
		
		String msg = ChatColor.WHITE + "[" + rankColor + user + ChatColor.WHITE + " (" +
				 ChatColor.DARK_AQUA  + "Discord" + ChatColor.WHITE + ")] " +
				 ChatColor.GRAY + message;
		
		Bukkit.broadcastMessage(msg);
	}
	
	public static ChatColor getMinecraftRankColor(Player player) {
		if(player.hasPermission("wauz.system")) {
			return ChatColor.GOLD;
		} else {
			return ChatColor.GREEN;
		}
	}
	
	public static boolean validateGroupMessage(Player player, Object groupObject, String message, boolean isGuild) {
		if(groupObject == null) {
			player.sendMessage(ChatColor.RED + "You are not in a " + (isGuild ? "group!" : "group!"));
			return false;
		}
		if(StringUtils.isBlank(message)) {
			player.sendMessage(ChatColor.RED + "Please specify the text to send!");
			return false;
		}
		return true;
	}

}
