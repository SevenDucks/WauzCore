package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventUnfriend;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerFriends;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for viewing and managing friends.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerFriends
 */
@PublicMenu
public class FriendsMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "friends";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG, WauzMode.SURVIVAL);
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		FriendsMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * A list of all the player's friends, including stats will be shown.
	 * Friends can be removed through right clicking them.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerConfigurator#getFriendsList(OfflinePlayer)
	 * @see StatisticsFetcher#addCharacterLores(List)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Friends List";
		Inventory menu = Components.inventory(new FriendsMenu(), menuTitle, 18);
		
		ItemStack friendsItemStack = MenuIconHeads.getFriendsItem();
		ItemMeta friendsItemMeta = friendsItemStack.getItemMeta();
		Components.displayName(friendsItemMeta, ChatColor.LIGHT_PURPLE + "Your Friends");
		List<String> friendsLores = new ArrayList<>();
		friendsLores.add(ChatColor.GRAY + "Being Friends allows you to:");
		friendsLores.add(ChatColor.GRAY + "1. See your Friends Characters in this Menu");
		friendsLores.add(ChatColor.GRAY + "2. Always have their Status in your Tablist");
		friendsLores.add("");
		friendsLores.add(ChatColor.DARK_PURPLE + "Commands:");
		friendsLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "friend [player] " + ChatColor.GRAY + "Send a Friend Request to a Player");
		friendsItemMeta.setLore(friendsLores);
		friendsItemStack.setItemMeta(friendsItemMeta);
		menu.setItem(1, friendsItemStack);
		
		ItemStack freeSlotItemStack = MenuIconHeads.getSocialItem();
		ItemMeta freeSlotItemMeta = freeSlotItemStack.getItemMeta();
		Components.displayName(freeSlotItemMeta, ChatColor.YELLOW + "Free Slot");
		freeSlotItemStack.setItemMeta(freeSlotItemMeta);
		
		int friendNumber = 0;
		List<String> friends = PlayerConfigurator.getFriendsList(player);
		for(int slot = 2; slot < 17; slot++) {
			if(StringUtils.equalsAny("" + slot, "8", "9")) {
				continue;
			}
			else if(friendNumber + 1 > friends.size()) {
				menu.setItem(slot, freeSlotItemStack);
				continue;
			}
			UUID friendUuid = UUID.fromString(friends.get(friendNumber));
			OfflinePlayer friend = Bukkit.getOfflinePlayer(friendUuid);
			ItemStack skullItemStack = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta skullItemMeta = (SkullMeta) skullItemStack.getItemMeta();
			Components.displayName(skullItemMeta, ChatColor.GREEN + friend.getName());
			skullItemMeta.setOwningPlayer(friend);
			List<String> skullLores = new ArrayList<String>();
			StatisticsFetcher statistics = new StatisticsFetcher(friend);
			statistics.addCharacterLores(skullLores);
			skullItemMeta.setLore(skullLores);
			skullItemStack.setItemMeta(skullItemMeta);
			menu.setItem(slot, skullItemStack);
			friendNumber++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a friend is right clicked, a dialog to unfriend them is shown.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerEventUnfriend
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !event.getClick().toString().contains("RIGHT")) {
			return;
		}
		else if(clicked.getType().equals(Material.PLAYER_HEAD)) {
			SkullMeta sm = (SkullMeta) clicked.getItemMeta();
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			if(sm.getOwningPlayer() == null || playerData == null) {
				return;
			}
			playerData.getSelections().setWauzPlayerEventName("Unfriend Player");
			playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventUnfriend(sm.getOwningPlayer()));
			WauzDialog.open(player, clicked);
		}
	}

}
