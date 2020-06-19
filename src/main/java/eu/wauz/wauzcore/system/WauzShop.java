package eu.wauz.wauzcore.system;

import java.util.HashMap;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.ShopConfigurator;

/**
 * A shop, generated from a shop config file.
 * 
 * @author Wauzmons
 */
public class WauzShop {
	
	/**
	 * A map of shops, indexed by name.
	 */
	private static Map<String, WauzShop> shopMap = new HashMap<>();
	
	/**
	 * Initializes all shops from the configs and fills the internal shop map.
	 * 
	 * @see ShopConfigurator#getShopNameList()
	 */
	public static void init() {
		for(String shopName : ShopConfigurator.getShopNameList()) {
			shopMap.put(shopName, new WauzShop(shopName));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + shopMap.size() + " Shops!");
	}
	
	/**
	 * @param shopName A shop name.
	 * 
	 * @return The shop with that name.
	 */
	public static WauzShop getShop(String shopName) {
		return shopMap.get(shopName);
	}
	
	/**
	 * The name of the shop.
	 */
	private String shopName;
	
	/**
	 *  If the shop is global (gamemode independent).
	 */
	private boolean isGlobal;
	
	/**
	 * Constructs a title, based on the title file in the /WauzCore folder.
	 * 
	 * @param titleName The key of the title.
	 */
	public WauzShop(String shopName) {
		this.shopName = shopName;
	}

	/**
	 * @return The name of the shop.
	 */
	public String getShopName() {
		return shopName;
	}

	/**
	 * @return If the shop is global (gamemode independent).
	 */
	public boolean isGlobal() {
		return isGlobal;
	}

}
