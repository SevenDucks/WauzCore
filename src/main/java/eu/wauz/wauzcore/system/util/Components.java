package eu.wauz.wauzcore.system.util;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/**
 * Util class for interacting with adventure components.
 * 
 * @author Wauzmons
 */
public class Components {

	/**
	 * Creates a custom inventory instance.
	 * 
	 * @param inventory The inventory to instantiate.
	 * @param title The title of the inventory instance.
	 * @param slots The slots of the inventory instance.
	 * 
	 * @return The created inventory.
	 */
	public static Inventory inventory(WauzInventory inventory, String title, int slots) {
		return Bukkit.createInventory(new WauzInventoryHolder(inventory), slots, toComponent(title));
	}
	
	/**
	 * Sets the display name of the given item meta.
	 * 
	 * @param itemMeta The item meta to set the display name for.
	 * @param displayName The display name to set.
	 */
	public static void displayName(ItemMeta itemMeta, String displayName) {
		itemMeta.displayName(toComponent(displayName));
	}
	
	/**
	 * Sets the lore of the given item meta.
	 * 
	 * @param itemMeta The item meta to set the lore for.
	 * @param loreLines The lore to set.
	 */
	public static void lore(ItemMeta itemMeta, List<String> loreLines) {
		itemMeta.lore(loreLines.stream()
				.map(lore -> toComponent(lore))
				.collect(Collectors.toList()));
	}
	
	/**
	 * Converts the given text to a component.
	 * 
	 * @param text The text to convert.
	 */
	public static TextComponent toComponent(String text) {
		return Component.text(text);
	}
	
}
