package eu.wauz.wauzcore.system.economy;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.ShopConfigurator;
import eu.wauz.wauzcore.items.InventorySerializer;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.system.util.Components;
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
	 * The relation exp gain on purchase of the shop item.
	 */
	private int shopItemRelationExp;
	
	/**
	 * Constructs a shop item, based on the shop file in the /WauzCore/ShopData folder.
	 * 
	 * @param shopName The name of the shop this item can be bought in.
	 * @param itemIndex The number of the shop item.
	 */
	public WauzShopItem(String shopName, int itemIndex) {
		String shopItemType = ShopConfigurator.getItemType(shopName, itemIndex);
		String[] nameParts = shopItemType.split(";");
		String canonicalName = nameParts[0];
		String displayNameSuffix = nameParts.length > 1 ? nameParts[1] : null;
		this.shopItemStack = InventorySerializer.serialize(mythicMobs.getItemStack(canonicalName));
		if(StringUtils.isNotBlank(displayNameSuffix) && ItemUtils.hasDisplayName(shopItemStack)) {
			ItemMeta shopItemMeta = shopItemStack.getItemMeta();
			Components.displayName(shopItemMeta, Components.displayName(shopItemMeta) + displayNameSuffix);
			shopItemStack.setItemMeta(shopItemMeta);
		}
		this.shopItemAmount = ShopConfigurator.getItemAmount(shopName, itemIndex);
		this.shopItemPrice = ShopConfigurator.getItemPrice(shopName, itemIndex);
		this.shopItemCurrency = ShopConfigurator.getItemCurrency(shopName, itemIndex);
		this.shopItemRelationExp = ShopConfigurator.getItemRelationExp(shopName, itemIndex);
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
	 * @return The relation exp gain on purchase of the shop item.
	 */
	public int getShopItemRelationExp() {
		return shopItemRelationExp;
	}

	/**
	 * Generates a player specific instance of the shop item stack.
	 * 
	 * @param player The player who can buy the item.
	 * @param discount The discount on the price of the item.
	 * @param bought If the item was bought.
	 * 
	 * @return A stack of the shop item.
	 */
	public ItemStack getInstance(Player player, WauzShopDiscount discount, boolean bought) {
		ItemStack itemStack = shopItemStack.clone();
		MenuUtils.addItemLore(itemStack, "", false);
		
		if(bought) {
			MenuUtils.addItemLore(itemStack, ChatColor.DARK_GRAY + "Bought (Worthless)", false);
		}
		else {
			int currencyCost = (int) ((1.0 - discount.getTotalDiscount()) * (double) shopItemPrice);
			currencyCost = currencyCost < 1 ? 1 : currencyCost;
			long currencyAmount = shopItemCurrency.getCurrencyAmount(player);
			String currencyName = shopItemCurrency.getCurrencyDisplayName();
			ChatColor currencyAmountColor = currencyAmount >= currencyCost ? ChatColor.GREEN : ChatColor.RED;
			
			String currencyCostString = ChatColor.GOLD + Formatters.INT.format(currencyCost);
			String currencyAmountString = currencyAmountColor + Formatters.INT.format(currencyAmount);
			String priceString = currencyCostString + " (" + currencyAmountString + ChatColor.GOLD + ") ";
			MenuUtils.addItemLore(itemStack, ChatColor.YELLOW + "Price: " + priceString + ChatColor.YELLOW + currencyName, false);
			MenuUtils.addItemLore(itemStack, ChatColor.YELLOW + "Relation Exp Gain: " + ChatColor.LIGHT_PURPLE + shopItemRelationExp, false);
		}
		itemStack.setAmount(shopItemAmount);
		return itemStack;
	}

}
