package eu.wauz.wauzcore.data;

import java.util.List;

import eu.wauz.wauzcore.data.api.ShopConfigurationUtils;
import eu.wauz.wauzcore.system.economy.WauzCurrency;

/**
 * Configurator to fetch or modify data from the Shop.yml files.
 * 
 * @author Wauzmons
 */
public class ShopConfigurator extends ShopConfigurationUtils {
	
// Shop Files

	/**
	 * @return The list of all shop names.
	 */
	public static List<String> getShopNameList() {
		return ShopConfigurationUtils.getShopNameList();
	}
	
// General Parameters
	
	/**
	 * @param shopName The name of the shop.
	 * 
	 * @return The display name of the shop.
	 */
	public static String getShopName(String shopName) {
		return shopConfigGetString(shopName, "name");
	}
	
	/**
	 * @param shopName The name of the shop.
	 * 
	 * @return If the shop is global (gamemode independent).
	 */
	public static boolean isShopGlobal(String shopName) {
		return shopConfigGetString(shopName, "type").equals("Global");
	}
	
	/**
	 * @param shopName The name of the shop.
	 * 
	 * @return The amount of items in the shop.
	 */
	public static int getShopItemsAmout(String shopName) {
		return shopConfigGetInt(shopName, "amount");
	}
	
// Shop Item Details
	
	/**
	 * @param shopName The name of the shop.
	 * @param itemIndex The number of the shop item.
	 * 
	 * @return The type of the shop item.
	 */
	public static String getItemType(String shopName, int itemIndex) {
		return shopConfigGetString(shopName, "items." + itemIndex + ".type");
	}
	
	/**
	 * @param shopName The name of the shop.
	 * @param itemIndex The number of the shop item.
	 * 
	 * @return The amount of the shop item.
	 */
	public static int getItemAmount(String shopName, int itemIndex) {
		return shopConfigGetInt(shopName, "items." + itemIndex + ".amount");
	}
	
	/**
	 * @param shopName The name of the shop.
	 * @param itemIndex The number of the shop item.
	 * 
	 * @return The price of the shop item.
	 */
	public static int getItemPrice(String shopName, int itemIndex) {
		return shopConfigGetInt(shopName, "items." + itemIndex + ".price");
	}
	
	/**
	 * @param shopName The name of the shop.
	 * @param itemIndex The number of the shop item.
	 * 
	 * @return The currency of the price of the shop item.
	 */
	public static WauzCurrency getItemCurrency(String shopName, int itemIndex) {
		return WauzCurrency.getCurrency(shopConfigGetString(shopName, "items." + itemIndex + ".currency"));
	}
	
	/**
	 * @param shopName The name of the shop.
	 * @param itemIndex The number of the shop item.
	 * 
	 * @return The relation exp gain on purchase of the shop item.
	 */
	public static int getItemRelationExp(String shopName, int itemIndex) {
		return shopConfigGetInt(shopName, "items." + itemIndex + ".relation");
	}

}
