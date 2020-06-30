package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.economy.WauzShop;
import eu.wauz.wauzcore.system.economy.WauzShopActions;
import eu.wauz.wauzcore.system.economy.WauzShopDiscount;
import eu.wauz.wauzcore.system.economy.WauzShopItem;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Used to buy, sell or repair items for different currencies.
 * 
 * @author Wauzmons
 *
 * @see WauzShop
 * @see WauzShopActions
 */
public class ShopMenu implements WauzInventory {
	
	/**
	 * Item slots for the buyable items in the shop menu.
	 */
	private static List<Integer> offerSlots = Arrays.asList(2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 22, 23, 24);
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "shop";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		throw new RuntimeException("Inventory cannot be opened directly!");
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows all buyable items.
	 * If the shop is global, only global currencies will be additionally shown.
	 * Otherwhise all major currencies, aswell as repair and sell options are shown.
	 * Empty slots will be filled with "SOLD OUT" displays.
	 * 
	 * @param player The player that should view the inventory.
	 * @param shopName The name of the shop to load.
	 * 
	 * @see WauzShop#getShop(String)
	 * @see WauzShop#getShopItems()
	 * @see WauzShopItem#getInstance(Player)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, String shopName, String citizenName) {
		WauzShop shop = WauzShop.getShop(shopName);
		WauzShopDiscount shopDiscount = new WauzShopDiscount(player, citizenName);
		ShopMenu shopMenu = new ShopMenu(shop, shopDiscount);
		WauzInventoryHolder holder = new WauzInventoryHolder(shopMenu);
		Inventory menu = Bukkit.createInventory(holder, 27, ChatColor.BLACK + "" + ChatColor.BOLD + shop.getShopDisplayName());
		
		if(shop.isGlobal()) {
			MenuUtils.setGlobalCurrencyDisplay(menu, player, 0);
			
			ItemStack repairItemStack = HeadUtils.getTitlesItem();
			MenuUtils.setItemDisplayName(repairItemStack, ChatColor.GRAY + "Repairing not possible...");
			menu.setItem(8, repairItemStack);
			
			ItemStack sellItemStack = HeadUtils.getTitlesItem();
			MenuUtils.setItemDisplayName(sellItemStack, ChatColor.GRAY + "Selling not possible...");
			menu.setItem(17, sellItemStack);
		}
		else {
			MenuUtils.setCurrencyDisplay(menu, player, 0);
			
			ItemStack repairItemStack = new ItemStack(Material.ANVIL);
			ItemMeta repairItemMeta = repairItemStack.getItemMeta();
			repairItemMeta.setDisplayName(ChatColor.BLUE + "Repair Items");
			List<String> repairLores = new ArrayList<String>();
			repairLores.add(ChatColor.GRAY + "Drag Items here to repair them.");
			repairItemMeta.setLore(repairLores);
			repairItemStack.setItemMeta(repairItemMeta);
			menu.setItem(8, repairItemStack);
			
			ItemStack sellItemStack = new ItemStack(Material.BARRIER, 1);
			ItemMeta sellItemMeta = sellItemStack.getItemMeta();
			sellItemMeta.setDisplayName(ChatColor.RED + "Sell Items");
			List<String> sellLores = new ArrayList<String>();
			sellLores.add(ChatColor.GRAY + "Drag Items here to sell them.");
			sellItemMeta.setLore(sellLores);
			sellItemStack.setItemMeta(sellItemMeta);
			menu.setItem(17, sellItemStack);
		}
		menu.setItem(9, shopDiscount.generateDiscountDisplay());
		
		ItemStack soldItemStack = HeadUtils.getDeclineItem();
		MenuUtils.setItemDisplayName(soldItemStack, ChatColor.DARK_GRAY + "SOLD OUT");
		
		List<WauzShopItem> shopItems = shop.getShopItems();
		for(int index = 0; index < offerSlots.size(); index++) {
			int indexSlot = offerSlots.get(index);
			if(index < shopItems.size()) {
				WauzShopItem shopItem = shopItems.get(index);
				menu.setItem(indexSlot, shopItem.getInstance(player, shopDiscount, false));
			}
			else {
				menu.setItem(indexSlot, soldItemStack);
			}
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The shop displayed in the menu.
	 */
	private WauzShop shop;
	
	/**
	 * The name of the citizen who owns the shop. Can be null.
	 */
	private WauzShopDiscount shopDiscount;
	
	/**
	 * Creates a new shop menu instance.
	 * 
	 * @param shop The shop displayed in the menu.
	 * @param citizenName The name of the citizen who owns the shop. Can be null.
	 */
	private ShopMenu(WauzShop shop, WauzShopDiscount shopDiscount) {
		this.shop = shop;
		this.shopDiscount = shopDiscount;
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and initiates the corresponding buy, sell or repair event.
	 * On an anvil click items on the cursor are repaired, while they are sold on a barrier click.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzShopActions#buy(Player, WauzShopItem, String)
	 * @see WauzShopActions#sell(Player, ItemStack, boolean)
	 * @see WauzShopActions#repair(Player, ItemStack, boolean)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		ItemStack itemOnCursor = player.getItemOnCursor();
		int slot = event.getRawSlot();
		
		if(slot < 27) {
			event.setCancelled(true);
		}
		
		boolean actionSuccess = false;
		if(offerSlots.contains(slot) && !HeadUtils.isHeadMenuItem(clicked, "SOLD OUT")) {
			int itemIndex = offerSlots.indexOf(slot);
			actionSuccess = WauzShopActions.buy(player, shop.getShopItems().get(itemIndex), shopDiscount);
		}
		else if(!ItemUtils.isNotAir(clicked)) {
			return;
		}
		else if(clicked.getType().equals(Material.BARRIER) && ItemUtils.isSpecificItem(clicked, "Sell Items")) {
			actionSuccess = WauzShopActions.sell(player, itemOnCursor, true);				
		}
		else if(clicked.getType().equals(Material.ANVIL) && ItemUtils.isSpecificItem(clicked, "Repair Items")) {
			actionSuccess = WauzShopActions.repair(player, itemOnCursor, true);
		}
		
		if(actionSuccess) {
			open(player, shop.getShopName(), shopDiscount.getCitizenName());
		}
	}
	
}
