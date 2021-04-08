package eu.wauz.wauzcore.system.util;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import io.papermc.paper.event.player.AsyncChatEvent;
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
	 * Gets the display name of the given item meta.
	 * 
	 * @param itemMeta The item meta to get the display name from.
	 * 
	 * @return The display name.
	 */
	public static String displayName(ItemMeta itemMeta) {
		return fromComponent(itemMeta.displayName());
	}
	
	/**
	 * Gets the display name of the given player.
	 * 
	 * @param player The player to get the display name from.
	 * 
	 * @return The display name.
	 */
	public static String displayName(Player player) {
		return fromComponent(player.displayName());
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
	 * Gets the lore of the given item meta.
	 * 
	 * @param itemMeta The item meta to get the lore from.
	 * 
	 * @return The lore.
	 */
	public static List<String> lore(ItemMeta itemMeta) {
		return itemMeta.lore().stream()
				.map(lore -> fromComponent(lore))
				.collect(Collectors.toList());
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
	 * Sets the modt of the given ping event.
	 * 
	 * @param event The ping event to set the motd for.
	 * @param motd The motd to set.
	 */
	public static void motd(ServerListPingEvent event, String motd) {
		event.motd(toComponent(motd));
	}
	
	/**
	 * Gets the message of the given chat event.
	 * 
	 * @param event The chat event to get the message from.
	 * 
	 * @return The message.
	 */
	public static String message(AsyncChatEvent event) {
		return fromComponent(event.message());
	}
	
	/**
	 * Sets the message of the given chat event.
	 * 
	 * @param event The chat event to set the message for.
	 * @param message The message to set.
	 */
	public static void message(AsyncChatEvent event, String message) {
		event.message(toComponent(message));
	}
	
	/**
	 * Converts the given text to a component.
	 * 
	 * @param text The text to convert.
	 * 
	 * @return The created component.
	 */
	private static TextComponent toComponent(String text) {
		return Component.text(text);
	}
	
	/**
	 * Converts the given component to text.
	 * 
	 * @param component The component to convert.
	 * 
	 * @return The created text.
	 */
	private static String fromComponent(Component component) {
		if(component instanceof TextComponent) {
			return ((TextComponent) component).content();
		}
		return null;
	}
	
}
