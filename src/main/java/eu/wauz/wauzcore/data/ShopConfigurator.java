package eu.wauz.wauzcore.data;

import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

public class ShopConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	public static boolean isShopGlobal(String shopName) {
		return shopConfigGetString(shopName, "type").equals("Global");
	}
	
	public static int getShopItemsAmout(String shopName) {
		return shopConfigGetInt(shopName, "amount");
	}
	
// Shop Item Details
	
	public static String getItemMaterial(String shopName, int itemIndex) {
		return shopConfigGetString(shopName, "items." + itemIndex + ".material");
	}
	
	public static int getItemAmount(String shopName, int itemIndex) {
		return shopConfigGetInt(shopName, "items." + itemIndex + ".amount");
	}
	
	public static String getItemName(String shopName, int itemIndex) {
		return shopConfigGetString(shopName, "items." + itemIndex + ".name");
	}
	
	public static List<String> getItemLores(String shopName, int itemIndex) {
		return shopConfigGetStringList(shopName, "items." + itemIndex + ".lores");
	}

}
