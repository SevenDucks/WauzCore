package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.ShopConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class ShopBuilder implements WauzInventory {
	
	public static HashMap<String, String> currency = new HashMap<String, String>();
	
// Load Items from Config
	
	public static void open(Player player, String shopName) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new ShopBuilder());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Shop of " + shopName);
		
		boolean isGlobal = ShopConfigurator.isShopGlobal(shopName);
		if(isGlobal)
			MenuUtils.setGlobalCurrencyDisplay(menu, player, 0);
		else
			MenuUtils.setCurrencyDisplay(menu, player, 0);
		
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
			ItemStack soldItemStack = new ItemStack(Material.SIGN, 1);
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
			repairLores.add(ChatColor.DARK_PURPLE + "Drag Items here,");
			repairLores.add(ChatColor.DARK_PURPLE + "to repair them for Coins.");
			repairItemMeta.setLore(repairLores);
			repairItemStack.setItemMeta(repairItemMeta);
			menu.setItem(7, repairItemStack);
		}
		
		if(!isGlobal) {
			ItemStack sellItemStack = new ItemStack(Material.BARRIER, 1);
			ItemMeta sellItemMeta = sellItemStack.getItemMeta();
			sellItemMeta.setDisplayName(ChatColor.RED + "Sell Items");
			List<String> sellLores = new ArrayList<String>();
			sellLores.add(ChatColor.DARK_PURPLE + "Drag Items here,");
			sellLores.add(ChatColor.DARK_PURPLE + "to trade them for Coins.");
			sellItemMeta.setLore(sellLores);
			sellItemStack.setItemMeta(sellItemMeta);
			menu.setItem(8, sellItemStack);
		}
		
		player.openInventory(menu);
	}	
	
	public void selectMenuPoint(InventoryClickEvent event) {
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || clicked.getType().equals(Material.SIGN)) {
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
		
		if(!clicked.hasItemMeta() || !clicked.getItemMeta().hasLore())
			return;
		
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
						WauzPlayerScoreboard.scheduleScoreboard(player);
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
	
	public static boolean sell(Player player, ItemStack itemToSell, Boolean fromShop) {
		if(itemToSell.equals(null) || itemToSell.getType().equals(Material.AIR)) {
			if(fromShop) {
				player.sendMessage(ChatColor.RED + "You can't sell air!");
				player.closeInventory();
			}
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
		itemToSell.setAmount(0);
		
		WauzPlayerScoreboard.scheduleScoreboard(player);
		player.sendMessage(ChatColor.GREEN + "Your item was sold for " + price + " Coins!");
		if(fromShop) player.closeInventory();
		return true;
	}
	
// Repair Item
	
	public static boolean repair(Player player, ItemStack itemToRepair, Boolean fromShop) {
		if(itemToRepair.equals(null) || itemToRepair.getType().equals(Material.AIR)) {
			if(fromShop) {
				player.sendMessage(ChatColor.RED + "You can't repair air!");
				player.closeInventory();
			}
			return false;
		}
		
		if(!(itemToRepair.getItemMeta() instanceof Damageable)) {
			player.sendMessage(ChatColor.RED + "This Item can't be repaired!");
			return false;
		}
		
		Damageable damagable = (Damageable) itemToRepair.getItemMeta(); 
		WauzDebugger.log(player, "Durability: " + damagable.getDamage());
		
		if(damagable.getDamage() == 0) {
			player.sendMessage(ChatColor.RED + "This Item already has full durability!");
			return false;
		}
		
		if(fromShop) {
			int price = damagable.getDamage();
			long money = PlayerConfigurator.getCharacterCoins(player);
			
			if(price < money) {
				PlayerConfigurator.setCharacterCoins(player, money - price);
				damagable.setDamage(0);
				itemToRepair.setItemMeta((ItemMeta) damagable);
				player.sendMessage(ChatColor.GREEN + "Your item was repaired for " + price + " Coins!");
				
				int empty = player.getInventory().firstEmpty();
				if(empty >= 0) {
					player.getInventory().setItem(empty, itemToRepair);
					itemToRepair.setAmount(0);
				}
				player.closeInventory();
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "You don't have enough money to rapair this item!");
				return false;
			}
		} else {
			damagable.setDamage(0);
			itemToRepair.setItemMeta((ItemMeta) damagable);
			player.sendMessage(ChatColor.GREEN + "Your item was repaired for one Scroll!");
			return true;
		}
	}
	
}
