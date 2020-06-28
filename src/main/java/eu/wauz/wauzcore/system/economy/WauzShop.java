package eu.wauz.wauzcore.system.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.ShopConfigurator;
import eu.wauz.wauzcore.menu.ShopMenu;

/**
 * A shop, generated from a shop config file.
 * 
 * @author Wauzmons
 * 
 * @see WauzShopItem
 * @see ShopMenu
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
	 * The amount of items in the shop.
	 */
	private int itemAmount;
	
	/**
	 * All the items the shop offers.
	 */
	private List<WauzShopItem> shopItems = new ArrayList<>();
	
	/**
	 * Constructs a shop, based on the shop file in the /WauzCore/ShopData folder.
	 * 
	 * @param shopName The name of the shop.
	 */
	public WauzShop(String shopName) {
		this.shopName = shopName;
		this.shopDisplayName = ShopConfigurator.getShopName(shopName);
		this.isGlobal = ShopConfigurator.isShopGlobal(shopName);
		this.itemAmount = ShopConfigurator.getShopItemsAmout(shopName);
		
		for(int itemIndex = 1; itemIndex <= itemAmount; itemIndex++) {
			shopItems.add(new WauzShopItem(shopName, itemIndex));
		}
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

	/**
	 * @return The amount of items in the shop.
	 */
	public int getItemAmount() {
		return itemAmount;
	}

	/**
	 * @return All the items the shop offers.
	 */
	public List<WauzShopItem> getShopItems() {
		return shopItems;
	}

}
