package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.ShopConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Builds an usable shop out of a shop config file.
 * 
 * @author Wauzmons
 *
 * @see ShopConfigurator
 */
public class ShopBuilder implements WauzInventory {
	
	/**
	 * A map of all currencies, that can be used to buy items, indexed by display name.
	 */
	private static HashMap<String, String> currency = new HashMap<String, String>();
	
	/**
	 * Registers a new currency, that can be used to buy items.
	 * 
	 * @param displayName The name of the currency in the shop menu.
	 * @param configKey The name of the currency in the player configs.
	 */
	public static void registerCurrency(String displayName, String configKey) {
		currency.put(displayName, configKey);
	}
	
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
	
// Load Items from Config
	
	/**
	 * Opens the menu for the given player.
	 * Reads all buyable items out of a shop config file.
	 * If the shop is global, only global currencies will be shown, the other 8 slots can be buyable items.
	 * Otherwhise all currencies, aswell as repair and sell options are shown, allowing only 6 buyable items.
	 * Empty slots will be filled with "SOLD OUT" signs.
	 * 
	 * @param player The player that should view the inventory.
	 * @param shopName The name of the shop to load.
	 * 
	 * @see ShopConfigurator#isShopGlobal(String)
	 * @see ShopConfigurator#getItemAmount(String, int)
	 */
	public static void open(Player player, String shopName) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new ShopBuilder());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Shop of " + shopName);
		
		boolean isGlobal = ShopConfigurator.isShopGlobal(shopName);
		if(isGlobal) {
			MenuUtils.setGlobalCurrencyDisplay(menu, player, 0);
		}
		else {
			MenuUtils.setCurrencyDisplay(menu, player, 0);
		}
		
		for(int itemIndex = ShopConfigurator.getShopItemsAmout(shopName); itemIndex > 0; itemIndex--) {		
			ItemStack offerItemStack = new ItemStack(Material.getMaterial(ShopConfigurator.getItemMaterial(shopName, itemIndex)),
					 ShopConfigurator.getItemAmount(shopName, itemIndex));
			
			ItemMeta offerItemMeta = offerItemStack.getItemMeta();
			offerItemMeta.setDisplayName(ShopConfigurator.getItemName(shopName, itemIndex));
			offerItemMeta.setLore(ShopConfigurator.getItemLores(shopName, itemIndex));
			offerItemStack.setItemMeta(offerItemMeta);
			menu.setItem(itemIndex, offerItemStack);
		}
		
		for(int index = isGlobal ? 8 : 6; index > ShopConfigurator.getShopItemsAmout(shopName); index--) {
			ItemStack soldItemStack = new ItemStack(Material.OAK_SIGN, 1);
			ItemMeta soldItemMeta = soldItemStack.getItemMeta();
			soldItemMeta.setDisplayName(ChatColor.DARK_GRAY + "SOLD OUT");
			soldItemStack.setItemMeta(soldItemMeta);
			menu.setItem(index, soldItemStack);
		}
		
		if(!isGlobal) {
			ItemStack repairItemStack = new ItemStack(Material.ANVIL);
			ItemMeta repairItemMeta = repairItemStack.getItemMeta();
			repairItemMeta.setDisplayName(ChatColor.BLUE + "Repair Items");
			List<String> repairLores = new ArrayList<String>();
			repairLores.add(ChatColor.DARK_PURPLE + "Drag Items here, to repair them for Coins.");
			repairItemMeta.setLore(repairLores);
			repairItemStack.setItemMeta(repairItemMeta);
			menu.setItem(7, repairItemStack);
		}
		
		if(!isGlobal) {
			ItemStack sellItemStack = new ItemStack(Material.BARRIER, 1);
			ItemMeta sellItemMeta = sellItemStack.getItemMeta();
			sellItemMeta.setDisplayName(ChatColor.RED + "Sell Items");
			List<String> sellLores = new ArrayList<String>();
			sellLores.add(ChatColor.DARK_PURPLE + "Drag Items here, to trade them for Coins.");
			sellItemMeta.setLore(sellLores);
			sellItemStack.setItemMeta(sellItemMeta);
			menu.setItem(8, sellItemStack);
		}
		
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
	 * @see ShopBuilder#sell(Player, ItemStack, Boolean)
	 * @see ShopBuilder#repair(Player, ItemStack, Boolean)
	 * @see ItemUtils#fitsInInventory(Inventory, ItemStack)
	 * @see ItemUtils#isAmmoItem(ItemStack)
	 * @see PlayerConfigurator#setCharacterCurrency(Player, String, long)
	 * @see PlayerConfigurator#setArrowAmount(Player, String, int)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || clicked.getType().equals(Material.OAK_SIGN)) {
			event.setCancelled(true);
			return;
		}
		
// Sell or Repair Item on Cursor
		
		ItemStack itemOnCursor = player.getItemOnCursor();
		
		if(clicked.getType().equals(Material.EMERALD) && ItemUtils.hasColoredName(clicked, ChatColor.GREEN)) {
			event.setCancelled(true);
		}
		else if(clicked.getType().equals(Material.BARRIER) && ItemUtils.hasColoredName(clicked, ChatColor.RED)) {
			event.setCancelled(true);
			sell(player, itemOnCursor, true);				
		}
		else if(clicked.getType().equals(Material.ANVIL) && ItemUtils.hasColoredName(clicked, ChatColor.BLUE)) {
			event.setCancelled(true);
			repair(player, itemOnCursor, true);
		}
		
// Check selected Offer
		
		if(!ItemUtils.hasLore(clicked)) {
			return;
		}
		
		List<String> lores = clicked.getItemMeta().getLore();
		
		for(String lore : lores) {
			if(lore.contains("Price")) {
				event.setCancelled(true);
				
				lores.remove(lore);
				lores.add(ChatColor.DARK_GRAY + "Bought (Worthless)");
				ItemMeta itemMeta = clicked.getItemMeta();
				itemMeta.setLore(lores);
				clicked.setItemMeta(itemMeta);
							
				String[] parts = lore.split(" ");
				int price = Integer.parseInt(parts[1]);
				String type = parts[2];
				
				long money = PlayerConfigurator.getCharacterCurrency(player, currency.get(type));
				
				if(money >= price) {
					if(ItemUtils.isAmmoItem(clicked)) {
						String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
						String arrowType = displayName.split(" ")[0].toLowerCase();
						int arrowAmount = PlayerConfigurator.getArrowAmount(player, arrowType);
						arrowAmount = (arrowAmount + clicked.getAmount()) > 999 ? 999 : (arrowAmount + clicked.getAmount());
						
						PlayerConfigurator.setCharacterCurrency(player, currency.get(type), money - price);
						PlayerConfigurator.setArrowAmount(player, arrowType, arrowAmount);
						String arrowString = "(" + arrowAmount + " / 999) " + displayName;
						player.sendMessage(ChatColor.GREEN + "Your now have " + arrowString + "s with you!");
					}
					else {
						if(!ItemUtils.fitsInInventory(player.getInventory(), clicked)) {
							player.sendMessage(ChatColor.RED + "Your inventory is full!");
							player.closeInventory();
							return;
						}
						PlayerConfigurator.setCharacterCurrency(player, currency.get(type), money - price);
						player.getInventory().addItem(clicked);
						player.sendMessage(ChatColor.GREEN + "Your purchase was successful!");
						WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
					}
				}
				else {
					player.sendMessage(ChatColor.RED + "You don't have enough money!");
				}
				player.closeInventory();
				return;
			}
		}		
	}
	
// Sell Item
	
	/**
	 * Tries to sell the given item for the player.
	 * The item stack will either be sold for a random value per item or with bonus on attack / defense.
	 * Some items have a fixed sell value in lore, while bought items are worth nothing.
	 * TODO: Also refactor this, when implementing equipment builder.
	 * 
	 * @param player The player who wants to sell the item.
	 * @param itemToSell The item that should be sold.
	 * @param fromShop If the selling was triggered from a shop menu.
	 * 
	 * @return If the selling was successful.
	 * 
	 * @see PlayerConfigurator#setCharacterCoins(Player, long)
	 * @see PlayerPassiveSkillConfigurator#getTradingFloat(Player)
	 * @see AchievementTracker#addProgress(Player, WauzAchievementType, double)
	 */
	public static boolean sell(Player player, ItemStack itemToSell, Boolean fromShop) {
		if(itemToSell.equals(null) || itemToSell.getType().equals(Material.AIR)) {
			return false;
		}
		
		int price = (int) (Math.random() * 3 + 1) * itemToSell.getAmount();
		long money = PlayerConfigurator.getCharacterCoins(player);
		
		if(itemToSell.hasItemMeta() && itemToSell.getItemMeta().hasLore()) {
			List<String> lores = itemToSell.getItemMeta().getLore();
			
			for(String s : lores) {
				if(s.contains("Attack")) {
					String[] parts = s.split(" ");
					price = (int) (((Integer.parseInt(parts[1]) / 1.5) * (Math.random() + 2)) * itemToSell.getAmount());
				}
				else if(s.contains("Defense")) {
					String[] parts = s.split(" ");
					price = (int) (((Integer.parseInt(parts[1]) * 3.0) * (Math.random() + 2)) * itemToSell.getAmount());
				}
				
				if(s.contains("Bought")) {
					price = 0;
				}
				else if(s.contains("Sell Value")) {
					String[] parts = s.split(" ");
					price = Integer.parseInt(parts[2]) * itemToSell.getAmount();
				}
			}
		}
		
		long priceOld = price;
		price = (int) ((float) price * (float) PlayerPassiveSkillConfigurator.getTradingFloat(player));
		
		WauzDebugger.log(player, "Item-Price: " + price + " (" + priceOld + ")");
		
		PlayerConfigurator.setCharacterCoins(player, money + price);
		AchievementTracker.addProgress(player, WauzAchievementType.EARN_COINS, price);
		itemToSell.setAmount(0);
		
		WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
		player.sendMessage(ChatColor.GREEN + "Your item was sold for " + price + " Coins!");
		if(fromShop) {
			MenuUtils.setCurrencyDisplay(player.getOpenInventory().getTopInventory(), player, 0);
		}
		return true;
	}
	
// Repair Item
	
	/**
	 * Tries to repair the given item for the player.
	 * Cannot repair items with no or max durability value.
	 * If the item is repaired in a shop menu, it will have a cost, equal to the lost durability.
	 * 
	 * @param player The player who wants to repair the item.
	 * @param itemToRepair The item that should be repaired.
	 * @param fromShop If the repairing was triggered from a shop menu.
	 * 
	 * @return If the repairing was successful.
	 * 
	 * @see EquipmentUtils#getCurrentDurability(ItemStack)
	 * @see EquipmentUtils#getMaximumDurability(ItemStack)
	 * @see PlayerConfigurator#setCharacterCoins(Player, long)
	 * @see DurabilityCalculator#repairItem(Player, ItemStack)
	 */
	public static boolean repair(Player player, ItemStack itemToRepair, Boolean fromShop) {
		if(itemToRepair.equals(null) || itemToRepair.getType().equals(Material.AIR)) {
			if(fromShop) {
				player.sendMessage(ChatColor.RED + "You can't repair air!");
				player.closeInventory();
			}
			return false;
		}
		
		int durability = EquipmentUtils.getCurrentDurability(itemToRepair);
		if(durability == 0) {
			player.sendMessage(ChatColor.RED + "This Item can't be repaired!");
			return false;
		}
		
		int maxDurability = EquipmentUtils.getMaximumDurability(itemToRepair);
		if(durability == maxDurability) {
			player.sendMessage(ChatColor.RED + "This Item already has full durability!");
			return false;
		}
		
		if(fromShop) {
			int price = maxDurability - durability;
			long money = PlayerConfigurator.getCharacterCoins(player);
			
			if(price < money) {
				PlayerConfigurator.setCharacterCoins(player, money - price);
				
				DurabilityCalculator.repairItem(player, itemToRepair);
				player.sendMessage(ChatColor.GREEN + "Your item was repaired for " + price + " Coins!");
				MenuUtils.setCurrencyDisplay(player.getOpenInventory().getTopInventory(), player, 0);
				return true;
			}
			else {
				player.sendMessage(ChatColor.RED + "You don't have enough money to rapair this item!");
				return false;
			}
		}
		else {
			DurabilityCalculator.repairItem(player, itemToRepair);
			player.sendMessage(ChatColor.GREEN + "Your item was repaired for one Scroll!");
			return true;
		}
	}
	
}
