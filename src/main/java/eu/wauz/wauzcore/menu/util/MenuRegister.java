package eu.wauz.wauzcore.menu.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A collection of public inventory menus for usage in commands or similar.
 * 
 * @author Wauzmons
 * 
 * @see MenuRegisterEntry
 */
public class MenuRegister {
	
	/**
	 * A map of all registered inventory menus.
	 */
	private static Map<String, MenuRegisterEntry> inventoryMap = new HashMap<>();
	
	/**
	 * Gets an inventory menu for given id from the map.
	 * 
	 * @param inventoryId The id of the inventory menu.
	 * 
	 * @return The inventory menu or null, if not found.
	 */
	public static MenuRegisterEntry getInventory(String inventoryId) {
		return inventoryMap.get(inventoryId);
	}
	
	/**
	 * @return A list of all inventory menu ids.
	 */
	public static List<String> getAllInventoryIds() {
		return new ArrayList<>(inventoryMap.keySet());
	}
	
	/**
	 * Registers an inventory menu.
	 * 
	 * @param inventory The inventory menu to register.
	 * @param validModes The modes in which the inventory menu can be opened.
	 */
	public static void registerInventory(WauzInventory inventory, WauzMode... validModes) {
		MenuRegisterEntry entry = new MenuRegisterEntry(inventory, Arrays.asList(validModes));
		inventoryMap.put(inventory.getInventoryId().toLowerCase(), entry);
	}

}
