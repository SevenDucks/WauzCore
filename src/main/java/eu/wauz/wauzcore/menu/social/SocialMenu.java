package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerMailConfigurator;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the main menu, that is used to display social mechanics.
 * 
 * @author Wauzmons
 *
 * @see WauzMenu
 */
public class SocialMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	public String getInventoryId() {
		return "social";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public void openInstance(Player player) {
		SocialMenu.open(player);
	}

	/**
	 * Opens the menu for the given player.
	 * All menus for social mechanics plus a short information are shown.
	 * Here is a quick summary:</br>
	 * Slot 1: The mail menu + unread mail count display.</br>
	 * Slot 4: Return to main menu...</br>
	 * Slot 5: The group menu + total active groups display.</br>
	 * Slot 6: The guild menu + guild name display.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerMailConfigurator#getPlayerMailNameList(Player)
	 * @see WauzPlayerGroupPool#getGroups()
	 * @see PlayerConfigurator#getGuild(org.bukkit.OfflinePlayer)
	 * @see MenuUtils#setMainMenuOpener(Inventory, int)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Social Menu");
		
		ItemStack mailItemStack = HeadUtils.getMailItem();
		ItemMeta mailItemMeta = mailItemStack.getItemMeta();
		mailItemMeta.setDisplayName(ChatColor.GOLD + "Mailbox");
		List<String> mailLores = new ArrayList<>();
		mailLores.add(ChatColor.DARK_PURPLE + "Unread Mails: " + ChatColor.YELLOW
				+ PlayerMailConfigurator.getPlayerMailNameList(player).size());
		mailLores.add("");
		mailLores.add(ChatColor.GRAY + "View your Mails and claim the Attachments");
		mailLores.add(ChatColor.GRAY + "or find out how to send Mails yourself.");
		mailItemMeta.setLore(mailLores);
		mailItemStack.setItemMeta(mailItemMeta);
		menu.setItem(1, mailItemStack);
		
		MenuUtils.setComingSoon(menu, "Titles", 2);
		MenuUtils.setComingSoon(menu, "Player vs Player", 3);
		
		ItemStack groupItemStack = HeadUtils.getGroupItem();
		ItemMeta groupItemMeta = groupItemStack.getItemMeta();
		groupItemMeta.setDisplayName(ChatColor.GOLD + "Group");
		List<String> groupLores = new ArrayList<String>();
		groupLores.add(ChatColor.DARK_PURPLE + "Listed Groups: " + ChatColor.YELLOW
				+ WauzPlayerGroupPool.getGroups().size());
		groupLores.add("");
		groupLores.add(ChatColor.GRAY + "Group up with other Players");
		groupLores.add(ChatColor.GRAY + "and enter their Instances together!");
		groupItemMeta.setLore(groupLores);
		groupItemStack.setItemMeta(groupItemMeta);
		menu.setItem(5, groupItemStack);
		
		ItemStack guildItemStack = HeadUtils.getGuildItem();
		ItemMeta guildItemMeta = guildItemStack.getItemMeta();
		guildItemMeta.setDisplayName(ChatColor.GOLD + "Guild");
		List<String> guildLores = new ArrayList<String>();
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		guildLores.add(ChatColor.DARK_PURPLE + "Your Guild: " + ChatColor.YELLOW
				+ (playerGuild != null ?  playerGuild.getGuildName() : "(None)"));
		guildLores.add("");
		guildLores.add(ChatColor.GRAY + "Join a Guild to get Bonuses, Tabards");
		guildLores.add(ChatColor.GRAY + "and Guildhall Access for all your Chars!");
		guildItemMeta.setLore(guildLores);
		guildItemStack.setItemMeta(guildItemMeta);
		menu.setItem(6, guildItemStack);
		
		MenuUtils.setComingSoon(menu, "Friends", 7);
		
		MenuUtils.setMainMenuOpener(menu, 4);
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a sub menu was clicked, it will be opened.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see MailMenu#open(Player)
	 * @see GroupMenu#open(Player)
	 * @see GuildOverviewMenu#open(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Mailbox")) {
			MailMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Group")) {
			GroupMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Guild")) {
			GuildOverviewMenu.open(player);
		}
	}

}
