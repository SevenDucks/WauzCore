package eu.wauz.wauzcore.system;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import io.papermc.paper.event.player.AsyncChatEvent;

/**
 * Used for formatting and sending all chat messages, to their receivers.
 * 
 * @author Wauzmons
 */
public class ChatFormatter {
	
	/**
	 * Formats and sends a message from an chat event to the global chat.
	 * 
	 * @param event The received PlayerChatEvent.
	 */
	public static void global(AsyncChatEvent event) {
		Player player = event.getPlayer();
		
		String rankPrefix = getRankPrefix(player);
		String gamemodeSuffix = getGamemodeSuffix(player);
		String msg = ChatColor.WHITE + "[" + rankPrefix + player.getName() + ChatColor.WHITE + " (" +
				ChatColor.AQUA  + gamemodeSuffix + ChatColor.WHITE + ")] " +
				ChatColor.GRAY + Components.message(event);
		
		Components.broadcastGlobally(player, msg);
		WauzCore.getInstance().getLogger().getParent().info(msg);
		WauzCore.getDiscordBot().sendMessageFromMinecraft(msg, false);
	}
	
	/**
	 * Formats and sends an item stack message from a player to the global chat.
	 * 
	 * @param player The player who sent the item stack message.
	 * @param itemStack The item stack to show in the message.
	 * 
	 * @see UnicodeUtils#shareChatItem(ItemStack, String)
	 */
	public static void share(Player player, ItemStack itemStack) {
		String rankPrefix = getRankPrefix(player);
		String gamemodeSuffix = getGamemodeSuffix(player);
		String msg = ChatColor.WHITE + "[" + rankPrefix + player.getName() + ChatColor.WHITE + " (" +
				ChatColor.AQUA  + gamemodeSuffix + ChatColor.WHITE + ") (" +
				ChatColor.GRAY + "Item" + ChatColor.WHITE + ")]";
		
		UnicodeUtils.shareChatItem(player, itemStack, msg);
	}
	
	/**
	 * Formats and sends a message from a player to their group.
	 * 
	 * @param player The player who sent the message.
	 * @param message The message they sent.
	 */
	public static void group(Player player, String message) {
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
		if(!validateGroupMessage(player, playerGroup, message, false)) {
			return;
		}
		
		String rankPrefix = getRankPrefix(player);
		String gamemodeSuffix = getGamemodeSuffix(player);
		String msg = ChatColor.WHITE + "[" + rankPrefix + player.getName() + ChatColor.WHITE + " (" +
				ChatColor.AQUA  + gamemodeSuffix + ChatColor.WHITE + ") (" +
				ChatColor.BLUE + "Group" + ChatColor.WHITE + ")] " +
				ChatColor.GRAY + message;
		
		for(Player member : playerGroup.getPlayers()) {
			member.sendMessage(msg);
		}
	}
	
	/**
	 * Formats and sends a message from a player to their guild.
	 * 
	 * @param player The player who sent the message.
	 * @param message The message they sent.
	 */
	public static void guild(Player player, String message) {
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		if(!validateGroupMessage(player, playerGuild, message, true)) {
			return;
		}
		
		String rankPrefix = getRankPrefix(player);
		String gamemodeSuffix = getGamemodeSuffix(player);
		String msg = ChatColor.WHITE + "[" + rankPrefix + player.getName() + ChatColor.WHITE + " (" +
				ChatColor.AQUA  + gamemodeSuffix + ChatColor.WHITE + ") (" +
				ChatColor.GREEN + "Guild" + ChatColor.WHITE + ")] " +
				ChatColor.GRAY + message;
		
		playerGuild.sendMessageToGuildMembers(msg);
	}
	
	/**
	 * Sends a received message from Discord to the global chat.
	 * 
	 * @param message The message from Discord.
	 * @param user The name of the Discord user.
	 */
	public static void discord(String message, String user) {
		String msg = ChatColor.WHITE + "[" + ChatColor.GREEN + user + ChatColor.WHITE + " (" +
				ChatColor.DARK_AQUA  + "Discord" + ChatColor.WHITE + ")] " +
				ChatColor.GRAY + message;
		
		Components.broadcastLocally(msg);
	}
	
	/**
	 * Gets the rank prefix of a player.
	 * 
	 * @param player The player to get the prefix for.
	 * 
	 * @return A rank prefix and color, based on player rank and title.
	 */
	public static String getRankPrefix(Player player) {
		WauzRank rank = WauzRank.getRank(player);
		String title = WauzTitle.getTitle(player);
		
		String rankPrefix;
		if(StringUtils.isNotBlank(title)) {
			rankPrefix = rank.getRankColor() + title + " ";
		}
		else {
			rankPrefix = StringUtils.isBlank(rank.getRankPrefix()) ? "" : rank.getRankPrefix() + " ";
		}
		return rankPrefix + rank.getRankColor();
	}
	
	/**
	 * Gets the gamemode suffix of a player.
	 * 
	 * @param player The player to get the suffix for.
	 * 
	 * @return A gamemode suffix, based on player world and level.
	 */
	public static String getGamemodeSuffix(Player player) {
		switch (WauzMode.getMode(player)) {
		case MMORPG:
			return WauzMode.inHub(player) ? "Hub" : "MMO, " + PlayerCollectionConfigurator.getCharacterLevel(player);
		case SURVIVAL:
			return WauzMode.inOneBlock(player) ? "OneBlock" : "Survival";
		case ARCADE:
			return "DropGuys";
		default:
			return player.getWorld().getName();
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
