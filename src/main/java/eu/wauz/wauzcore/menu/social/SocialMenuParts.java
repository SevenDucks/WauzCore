package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerMailConfigurator;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.players.WauzPlayerFriends;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * An util to add social sub menus to the main menu.
 * 
 * @author Wauzmons
 *
 * @see WauzMenu
 */
public class SocialMenuParts {
	
	/**
	 * Fills the given main menu with sub menu icons.<br>
	 * Row 1, Slot 1: The mail menu + unread mail count display.<br>
	 * Row 1, Slot 2: The group menu + total active groups display.<br>
	 * Row 2, Slot 1: The friends menu + friend count display.<br>
	 * Row 2, Slot 2: The guild menu + guild name display.
	 * 
	 * @param player The player that should view the inventory.
	 * @param menu The main menu inventory.
	 * @param startIndex The first slot of the inventory to fill.
	 */
	public static void addMenuParts(Player player, Inventory menu, int startIndex) {
		ItemStack mailItemStack = MenuIconHeads.getMailItem();
		ItemMeta mailItemMeta = mailItemStack.getItemMeta();
		Components.displayName(mailItemMeta, ChatColor.GOLD + "Mailbox");
		List<String> mailLores = new ArrayList<>();
		mailLores.add(ChatColor.DARK_PURPLE + "Unread Mails: " + ChatColor.YELLOW
				+ Formatters.INT.format(PlayerMailConfigurator.getPlayerMailNameList(player).size()));
		mailLores.add("");
		mailLores.add(ChatColor.GRAY + "View your Mails and claim the Attachments");
		mailLores.add(ChatColor.GRAY + "or find out how to send Mails yourself.");
		Components.lore(mailItemMeta, mailLores);
		mailItemStack.setItemMeta(mailItemMeta);
		menu.setItem(startIndex, mailItemStack);
		
		ItemStack groupItemStack = MenuIconHeads.getGroupItem();
		ItemMeta groupItemMeta = groupItemStack.getItemMeta();
		Components.displayName(groupItemMeta, ChatColor.GOLD + "Group");
		List<String> groupLores = new ArrayList<>();
		groupLores.add(ChatColor.DARK_PURPLE + "Listed Groups: " + ChatColor.YELLOW
				+ WauzPlayerGroupPool.getGroups().size());
		groupLores.add("");
		groupLores.add(ChatColor.GRAY + "Group up with other Players");
		groupLores.add(ChatColor.GRAY + "and enter their Instances together!");
		Components.lore(groupItemMeta, groupLores);
		groupItemStack.setItemMeta(groupItemMeta);
		menu.setItem(startIndex + 1, groupItemStack);
		
		ItemStack friendsItemStack = MenuIconHeads.getFriendsItem();
		ItemMeta friendsItemMeta = friendsItemStack.getItemMeta();
		Components.displayName(friendsItemMeta, ChatColor.GOLD + "Friends");
		List<String> friendsLores = new ArrayList<>();
		friendsLores.add(ChatColor.DARK_PURPLE + "Friend Count: " + ChatColor.YELLOW
				+ WauzPlayerFriends.getFriendCount(player) + " / " + WauzPlayerFriends.MAX_FRIEND_AMOUNT);
		friendsLores.add("");
		friendsLores.add(ChatColor.GRAY + "View and Manage your list of Friends.");
		friendsLores.add(ChatColor.GRAY + "Use /friend [player] to add new Friends.");
		Components.lore(friendsItemMeta, friendsLores);
		friendsItemStack.setItemMeta(friendsItemMeta);
		menu.setItem(startIndex + 9, friendsItemStack);
		
		ItemStack guildItemStack = MenuIconHeads.getGuildItem();
		ItemMeta guildItemMeta = guildItemStack.getItemMeta();
		Components.displayName(guildItemMeta, ChatColor.GOLD + "Guild");
		List<String> guildLores = new ArrayList<>();
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		guildLores.add(ChatColor.DARK_PURPLE + "Your Guild: " + ChatColor.YELLOW
				+ (playerGuild != null ?  playerGuild.getGuildName() : "(None)"));
		guildLores.add("");
		guildLores.add(ChatColor.GRAY + "Join a Guild to get Bonuses, Tabards");
		guildLores.add(ChatColor.GRAY + "and Guildhall Access for all your Chars!");
		Components.lore(guildItemMeta, guildLores);
		guildItemStack.setItemMeta(guildItemMeta);
		menu.setItem(startIndex + 10, guildItemStack);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * 
	 * @param player The placer who clicked a menu item.
	 * @param clicked The item that was clicled.
	 * 
	 * @return If an event was triggered.
	 * 
	 * @see MailMenu#open(Player)
	 * @see GroupMenu#open(Player)
	 * @see FriendsMenu#open(Player)
	 * @see GuildOverviewMenu#open(Player)
	 */
	public static boolean check(Player player, ItemStack clicked) {
		if(HeadUtils.isHeadMenuItem(clicked, "Mailbox")) {
			MailMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Group")) {
			GroupMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Friends")) {
			FriendsMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Guild")) {
			GuildOverviewMenu.open(player);
			return true;
		}
		return false;
	}

}
