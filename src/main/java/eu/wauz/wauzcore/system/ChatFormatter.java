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

/**
 * Used for formatting and sending all chat messages, to their receivers.
 * 
 * @author Wauzmons
 */
public class ChatFormatter {
	
	/**
	 * Formats a message from an chat event for the global chat.
	 * 
	 * @param event The received PlayerChatEvent.
	 * 
	 * @return The formatted message.
	 */
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
	
	/**
	 * Formats and sends a message from a player to their group.
	 * 
	 * @param player The player who sent the message.
	 * @param message The message they sent.
	 * 
	 * @return If the message has been sent successfully.
	 */
	public static boolean group(Player player, String message) {
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
		if(!validateGroupMessage(player, playerGroup, message, false)) {
			return false;
		}
		
		ChatColor rankColor = getMinecraftRankColor(player);
		String msg = ChatColor.WHITE + "[" + rankColor + player.getDisplayName() + ChatColor.WHITE + " (" +
				 ChatColor.BLUE + "Group" + ChatColor.WHITE + ")] " +
				 ChatColor.GRAY + message;
		
		for(Player member : playerGroup.getPlayers()) {
			member.sendMessage(msg);
		}
		
		return true;
	}
	
	/**
	 * Formats and sends a message from a player to their guild.
	 * 
	 * @param player The player who sent the message.
	 * @param message The message they sent.
	 * 
	 * @return If the message has been sent successfully.
	 */
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
	
	/**
	 * Sends a received message from Discord to the global chat.
	 * 
	 * @param message The message from Discord.
	 * @param user The name of the Discord user.
	 * @param admin If they are the bot admin.
	 */
	public static void discord(String message, String user, boolean admin) {
		ChatColor rankColor;
		if(admin) {
			rankColor = ChatColor.GOLD;
		}
		else {
			rankColor = ChatColor.GREEN;
		}
		
		String msg = ChatColor.WHITE + "[" + rankColor + user + ChatColor.WHITE + " (" +
				 ChatColor.DARK_AQUA  + "Discord" + ChatColor.WHITE + ")] " +
				 ChatColor.GRAY + message;
		
		Bukkit.broadcastMessage(msg);
	}
	
	/**
	 * @param player The player to get the colot for.
	 * 
	 * @return A rank color, based on player permissions.
	 */
	public static ChatColor getMinecraftRankColor(Player player) {
		if(player.hasPermission("wauz.system")) {
			return ChatColor.GOLD;
		}
		else {
			return ChatColor.GREEN;
		}
	}
	
	/**
	 * Checks if a group message can be sent.
	 * 
	 * @param player The player who sent the message.
	 * @param groupObject The group or guild that should receive the message.
	 * @param message The message they sent.
	 * @param isGuild If it is sent to guild (used for error message).
	 * 
	 * @return If the message is valid.
	 */
	public static boolean validateGroupMessage(Player player, Object groupObject, String message, boolean isGuild) {
		if(groupObject == null) {
			player.sendMessage(ChatColor.RED + "You are not in a " + (isGuild ? "guild!" : "group!"));
			return false;
		}
		if(StringUtils.isBlank(message)) {
			player.sendMessage(ChatColor.RED + "Please specify the text to send!");
			return false;
		}
		return true;
	}

}
