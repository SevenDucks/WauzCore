package eu.wauz.wauzcore.system.economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.ShopConfigurator;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.system.util.Formatters;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.items.ItemManager;

/**
 * An item of a shop.
 * 
 * @author Wauzmons
 * 
 * @see WauzShop
 */
public class WauzShopItem {
	
	/**
	 * Access to the MythicMobs API item manager.
	 */
	private static ItemManager mythicMobs = MythicMobs.inst().getItemManager();
	
	/**
	 * A stack of the shop item.
	 */
	private ItemStack shopItemStack;
	
	/**
	 * The amount of the shop item.
	 */
	private int shopItemAmount;
	
	/**
	 * The price of the shop item.
	 */
	private int shopItemPrice;
	
	/**
	 * The currency of the price of the shop item.
	 */
	private WauzCurrency shopItemCurrency;
	
	/**
	 * Constructs a shop item, based on the shop file in the /WauzCore/ShopData folder.
	 * 
	 * @param shopName The name of the shop this item can be bought in.
	 * @param itemIndex The number of the shop item.
	 */
	public WauzShopItem(String shopName, int itemIndex) {
		String shopItemType = ShopConfigurator.getItemType(shopName, itemIndex);
		this.shopItemStack = mythicMobs.getItemStack(shopItemType);
		this.shopItemAmount = ShopConfigurator.getItemAmount(shopName, itemIndex);
		this.shopItemPrice = ShopConfigurator.getItemPrice(shopName, itemIndex);
		this.shopItemCurrency = ShopConfigurator.getItemCurrency(shopName, itemIndex);
	}

	/**
	 * @return A stack of the shop item.
	 */
	public ItemStack getShopItemStack() {
		return shopItemStack;
	}

	/**
	 * @return The amount of the shop item.
	 */
	public int getShopItemAmount() {
		return shopItemAmount;
	}

	/**
	 * @return The price of the shop item.
	 */
	public int getShopItemPrice() {
		return shopItemPrice;
	}

	/**
	 * @return The currency of the price of the shop item.
	 */
	public WauzCurrency getShopItemCurrency() {
		return shopItemCurrency;
	}
	
	/**
	 * Generates a player specific instance of the shop item stack.
	 * 
	 * @param player The player who can buy the item.
	 * @param bought If the item was bought and therefore has no price anymore.
	 * 
	 * @return A stack of the shop item.
	 */
	public ItemStack getInstance(Player player, boolean bought) {
		String priceLore;
		if(bought) {
			priceLore = ChatColor.DARK_GRAY + "Bought (Worthless)";
		}
		else {
			long currencyAmount = shopItemCurrency.getCurrencyAmount(player);
			String currencyName = shopItemCurrency.getCurrencyDisplayName();
			ChatColor currencyAmountColor = currencyAmount >= shopItemPrice ? ChatColor.GREEN : ChatColor.RED;
			
			String currencyCostString = ChatColor.GOLD + Formatters.INT.format(shopItemPrice);
			String currencyAmountString = currencyAmountColor + Formatters.INT.format(currencyAmount);
			String priceString = currencyCostString + " (" + currencyAmountString + ChatColor.GOLD + ") ";
			priceLore = ChatColor.YELLOW + "Price: " + priceString + ChatColor.YELLOW + currencyName;
		}
		ItemStack itemStack = shopItemStack.clone();
		MenuUtils.addItemLore(itemStack, "", false);
		MenuUtils.addItemLore(itemStack, priceLore, false);
		itemStack.setAmount(shopItemAmount);
		return itemStack;
	}

}
