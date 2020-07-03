package eu.wauz.wauzcore.items.scrolls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.events.WauzPlayerEventHomeChange;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;

/**
 * A class for handling the usage of scrolls and socketable items.
 * 
 * @author Wauzmons
 */
public class WauzScrolls {
	
	/**
	 * A map of all scrolls, indexed by scroll name.
	 */
	private static Map<String, InventoryScroll> scrollMap = new HashMap<>();
	
	/**
	 * A list of materials a scroll or socketable item can have.
	 */
	private static List<Material> validScrollMaterials = new ArrayList<Material>(Arrays.asList(
			Material.NAME_TAG, Material.FIREWORK_STAR, Material.REDSTONE));
	
	/**
	 * Gets a scroll for the given name from the map.
	 * Can also find scroll objects for runes and skillgems.
	 * 
	 * @param scrollName The name of the scroll.
	 * 
	 * @return The scroll or null, if not found.
	 */
	public static InventoryScroll getScroll(String scrollName) {
		if(scrollName.contains("Rune")) {
			return scrollMap.get("Generic Rune");
		}
		else if(scrollName.contains("Skillgem")) {
			return scrollMap.get("Generic Skillgem");
		}
		else {
			return scrollMap.get(scrollName);
		}
	}
	
	/**
	 * Registers a scroll.
	 * 
	 * @param scroll The scroll to register.
	 */
	public static void registerScroll(InventoryScroll scroll) {
		scrollMap.put(scroll.getScrollName(), scroll);
	}
	
	/**
	 * Handles the usage of right click scrolls.
	 * Includes following types: Summoning, Comfort.
	 * Removes the scroll item, if successful.
	 * 
	 * @param event The interact event.
	 * 
	 * @see WauzScrolls#onScrollItemInteract(InventoryClickEvent, String) For item interactive scrolls...
	 * @see PetOverviewMenu#addPet(PlayerInteractEvent)
	 * @see WauzPlayerEventHomeChange
	 */
	public static void onScrollItemInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack scroll = player.getEquipment().getItemInMainHand();
		if(!ItemUtils.hasDisplayName(scroll)) {
			return;
		}
		
		String scrollName = scroll.getItemMeta().getDisplayName();
		if(scrollName.contains("Scroll of Summoning")) {
			PetOverviewMenu.addPet(event);
		}
		else if(scrollName.contains("Scroll of Comfort")) {
			new WauzPlayerEventHomeChange(player.getLocation(), scroll).execute(player);
		}
	}
	
	/**
	 * Handles the usage of item interactive scrolls, aswell as runes and skillgems.
	 * Includes following types: Wisdom, Fortune, Toughness, Regret.
	 * Removes the scroll item, if successful.
	 * 
	 * @param event The inventory event.
	 * @param itemName The name of the item, the scroll is used on.
	 * 
	 * @see WauzScrolls#onScrollItemInteract(PlayerInteractEvent) For right click scrolls...
	 * @see WauzScrolls#getScroll(String)
	 * @see InventoryScroll#use(InventoryClickEvent, String)
	 */
	public static void onScrollItemInteract(InventoryClickEvent event, String itemName) {
		Player player = (Player) event.getWhoClicked();
		ItemStack scrollItemStack = player.getItemOnCursor();
		if(!validScrollMaterials.contains(scrollItemStack.getType())) {
			return;
		}
		
		String scrollName = scrollItemStack.getItemMeta().getDisplayName();
		InventoryScroll scroll = getScroll(ChatColor.stripColor(scrollName));
		if(scroll != null && scroll.use(event, itemName)) {
			scrollItemStack.setAmount(scrollItemStack.getAmount() - 1);
			event.setCancelled(true);
		}
	}

}
