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

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.economy.WauzShop;
import eu.wauz.wauzcore.system.economy.WauzShopActions;
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
	public static void open(Player player, String shopName) {
		WauzShop shop = WauzShop.getShop(shopName);
		WauzInventoryHolder holder = new WauzInventoryHolder(new ShopMenu());
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
		
		ItemStack discountItemStack = HeadUtils.getCitizenRelationItem();
		MenuUtils.setItemDisplayName(discountItemStack, ChatColor.GRAY + "Discount not possible...");
		menu.setItem(9, discountItemStack);
		
		ItemStack soldItemStack = HeadUtils.getDeclineItem();
		MenuUtils.setItemDisplayName(soldItemStack, ChatColor.DARK_GRAY + "SOLD OUT");
		
		List<WauzShopItem> shopItems = shop.getShopItems();
		for(int index = 0; index < offerSlots.size(); index++) {
			int indexSlot = offerSlots.get(index);
			if(index < shopItems.size()) {
				WauzShopItem shopItem = shopItems.get(index);
				menu.setItem(indexSlot, shopItem.getInstance(player));
			}
			else {
				menu.setItem(indexSlot, soldItemStack);
			}
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}	
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and initiates the corresponding buy, sell or repair event.
	 * On an anvil click items on the cursor are repaired, while they are sold on a barrier click.
	 * Unlike ammo items (arrows), physical items, can only be bought when there is inventory space left.
	 * Items cannot be bought, if the player doesn't have enough money.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzShopActions#sell(Player, ItemStack, Boolean)
	 * @see WauzShopActions#repair(Player, ItemStack, Boolean)
	 * @see ItemUtils#fitsInInventory(Inventory, ItemStack)
	 * @see ItemUtils#isAmmoItem(ItemStack)
	 * @see PlayerConfigurator#setCharacterCurrency(Player, String, long)
	 * @see PlayerConfigurator#setArrowAmount(Player, String, int)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		ItemStack itemOnCursor = player.getItemOnCursor();
		
		if(!ItemUtils.isNotAir(clicked)) {
			event.setCancelled(true);
		}
		else if(clicked.getType().equals(Material.BARRIER) && ItemUtils.hasColoredName(clicked, ChatColor.RED)) {
			event.setCancelled(true);
			WauzShopActions.sell(player, itemOnCursor, true);				
		}
		else if(clicked.getType().equals(Material.ANVIL) && ItemUtils.hasColoredName(clicked, ChatColor.BLUE)) {
			event.setCancelled(true);
			WauzShopActions.repair(player, itemOnCursor, true);
		}
		
//		if(!ItemUtils.hasLore(clicked)) {
//			return;
//		}
//		
//		List<String> lores = clicked.getItemMeta().getLore();
//		
//		for(String lore : lores) {
//			if(lore.contains("Price")) {
//				event.setCancelled(true);
//				
//				lores.remove(lore);
//				lores.add(ChatColor.DARK_GRAY + "Bought (Worthless)");
//				ItemMeta itemMeta = clicked.getItemMeta();
//				itemMeta.setLore(lores);
//				clicked.setItemMeta(itemMeta);
//							
//				String[] parts = lore.split(" ");
//				int price = Integer.parseInt(parts[1]);
//				String type = parts[2];
//				
//				long money = PlayerConfigurator.getCharacterCurrency(player, currency.get(type));
//				
//				if(money >= price) {
//					if(ItemUtils.isAmmoItem(clicked)) {
//						String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
//						String arrowType = displayName.split(" ")[0].toLowerCase();
//						int arrowAmount = PlayerConfigurator.getArrowAmount(player, arrowType);
//						arrowAmount = (arrowAmount + clicked.getAmount()) > 999 ? 999 : (arrowAmount + clicked.getAmount());
//						
//						PlayerConfigurator.setCharacterCurrency(player, currency.get(type), money - price);
//						PlayerConfigurator.setArrowAmount(player, arrowType, arrowAmount);
//						String arrowString = "(" + arrowAmount + " / 999) " + displayName;
//						player.sendMessage(ChatColor.GREEN + "Your now have " + arrowString + "s with you!");
//					}
//					else {
//						if(!ItemUtils.fitsInInventory(player.getInventory(), clicked)) {
//							player.sendMessage(ChatColor.RED + "Your inventory is full!");
//							player.closeInventory();
//							return;
//						}
//						PlayerConfigurator.setCharacterCurrency(player, currency.get(type), money - price);
//						player.getInventory().addItem(clicked);
//						player.sendMessage(ChatColor.GREEN + "Your purchase was successful!");
//						WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
//					}
//				}
//				else {
//					player.sendMessage(ChatColor.RED + "You don't have enough money!");
//				}
//				player.closeInventory();
//				return;
//			}
//		}		
	}
	
}
