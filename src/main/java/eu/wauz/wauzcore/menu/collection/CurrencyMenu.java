package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.economy.WauzCurrency;
import eu.wauz.wauzcore.system.economy.WauzCurrencyCategory;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that shows all collected currencies and reputation.
 * 
 * @author Wauzmons
 * 
 * @see WauzCurrency
 */
public class CurrencyMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "currency";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		CurrencyMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows all currencies, divided into categories / factions.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzCurrency#getCurrencyCategories()
	 * @see CurrencyMenu#setCurrencyCategoryDisplay(Inventory, Player, WauzCurrencyCategory, int)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		List<WauzCurrencyCategory> categories = WauzCurrency.getCurrencyCategories();
		int inventorySize = MenuUtils.roundInventorySize(categories.size());
		WauzInventoryHolder holder = new WauzInventoryHolder(new CurrencyMenu());
		Inventory menu = Bukkit.createInventory(holder, inventorySize, ChatColor.BLACK + "" + ChatColor.BOLD + "Currencies");
		
		for(int index = 0; index < categories.size(); index++) {
			setCurrencyCategoryDisplay(menu, player, categories.get(index), index);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Sets a currency category display to an inventory slot.
	 * 
	 * @param menu The menu inventory.
	 * @param player The player whose currencies should be shown.
	 * @param category The category of which the currencies should be shown.
	 * @param index The slot to use in the inventory.
	 * 
	 * @see WauzCurrencyCategory#getCurrencies()
	 */
	public static void setCurrencyCategoryDisplay(Inventory menu, Player player, WauzCurrencyCategory category, int index) {
		ItemStack currencyItemStack = new ItemStack(index == 0 ? Material.DIAMOND : Material.EMERALD);
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		currencyItemMeta.setDisplayName(ChatColor.GREEN + category.getCurrencyCategoryDisplayName());
		List<String> lores = new ArrayList<String>();
		for(WauzCurrency currency : category.getCurrencies()) {
			lores.add(ChatColor.GOLD + Formatters.INT.format(currency.getCurrencyAmount(player))
				+ ChatColor.YELLOW + " " + currency.getCurrencyDisplayName());
		}
		currencyItemMeta.setLore(lores);
		currencyItemStack.setItemMeta(currencyItemMeta);
		currencyItemStack.setAmount(index == 0 ? 1 : index);
		menu.setItem(index, currencyItemStack);
	}

	/**
	 * No actions available, so all clicks are canceled.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
	}

}
