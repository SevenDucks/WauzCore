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
	 * The display name of the shop.
	 */
	private String shopDisplayName;
	
	/**
	 *  If the shop is global (gamemode independent).
	 */
	private boolean isGlobal;
	
	/**
	 * Constructs a shop, based on the shop file in the /WauzCore/ShopData folder.
	 * 
	 * @param shopName The name of the shop.
	 */
	public WauzShop(String shopName) {
		this.shopName = shopName;
		this.shopDisplayName = ShopConfigurator.getShopName(shopName);
		this.isGlobal = ShopConfigurator.isShopGlobal(shopName);
	}

	/**
	 * @return The name of the shop.
	 */
	public String getShopName() {
		return shopName;
	}

	/**
	 * @return The display name of the shop.
	 */
	public String getShopDisplayName() {
		return shopDisplayName;
	}

	/**
	 * @return If the shop is global (gamemode independent).
	 */
	public boolean isGlobal() {
		return isGlobal;
	}

}
