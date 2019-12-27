package eu.wauz.wauzcore.data;

import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Shop.yml files.
 * 
 * @author Wauzmons
 */
public class ShopConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @param shopName Name of the shop.
	 * 
	 * @return If the shop is global.
	 */
	public static boolean isShopGlobal(String shopName) {
		return shopConfigGetString(shopName, "type").equals("Global");
	}
	
	/**
	 * @param shopName Name of the shop.
	 * 
	 * @return The amount of items in the shop.
	 */
	public static int getShopItemsAmout(String shopName) {
		return shopConfigGetInt(shopName, "amount");
	}
	
// Shop Item Details
	
	/**
	 * @param shopName Name of the shop.
	 * @param itemIndex Number of the shop item.
	 * 
	 * @return The material of the shop item.
	 */
	public static String getItemMaterial(String shopName, int itemIndex) {
		return shopConfigGetString(shopName, "items." + itemIndex + ".material");
	}
	
	/**
	 * @param shopName Name of the shop.
	 * @param itemIndex Number of the shop item.
	 * 
	 * @return The amount of the shop item.
	 */
	public static int getItemAmount(String shopName, int itemIndex) {
		return shopConfigGetInt(shopName, "items." + itemIndex + ".amount");
	}
	
	/**
	 * @param shopName Name of the shop.
	 * @param itemIndex Number of the shop item.
	 * 
	 * @return The name of the shop item.
	 */
	public static String getItemName(String shopName, int itemIndex) {
		return shopConfigGetString(shopName, "items." + itemIndex + ".name");
	}
	
	/**
	 * @param shopName Name of the shop.
	 * @param itemIndex Number of the shop item.
	 * 
	 * @return The lores of the shop item.
	 */
	public static List<String> getItemLores(String shopName, int itemIndex) {
		return shopConfigGetStringList(shopName, "items." + itemIndex + ".lores");
	}

}
