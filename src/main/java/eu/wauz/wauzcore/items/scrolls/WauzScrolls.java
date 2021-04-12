package eu.wauz.wauzcore.items.scrolls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.events.PetObtainEvent;
import eu.wauz.wauzcore.events.WauzPlayerEventHomeChange;
import eu.wauz.wauzcore.items.CustomItem;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.LootContainer;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;
import eu.wauz.wauzcore.mobs.towers.WauzTowers;
import eu.wauz.wauzcore.system.annotations.Item;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A class for handling the usage of scrolls and socketable items.
 * 
 * @author Wauzmons
 */
@Item
public class WauzScrolls implements CustomItem {
	
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
	 * 
	 * @param event The interaction event.
	 * 
	 * @see WauzScrolls#onScrollItemInteract(PlayerInteractEvent)
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		onScrollItemInteract(event);
	}
	
	/**
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.NAME_TAG);
	}
	
	/**
	 * Handles the usage of right click scrolls.
	 * Includes following types: Comfort, Summoning, Blueprint.
	 * Removes the scroll item, if successful.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see WauzScrolls#onScrollItemInteract(InventoryClickEvent, String) For item interactive scrolls...
	 * @see WauzPlayerEventHomeChange
	 * @see PetObtainEvent#call(Player, WauzPet)
	 * @see WauzTowers#tryToConstruct(Player, String)
	 */
	public static void onScrollItemInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack scroll = player.getEquipment().getItemInMainHand();
		if(!ItemUtils.hasDisplayName(scroll)) {
			return;
		}
		
		String scrollName = Components.displayName(scroll.getItemMeta());
		if(scrollName.contains("Scroll of Comfort")) {
			new WauzPlayerEventHomeChange(player.getLocation(), scroll).execute(player);
		}
		else if(scrollName.contains("Scroll of Summoning")) {
			WauzPet newPet = WauzPet.getPet(ChatColor.stripColor(Components.lore(scroll.getItemMeta()).get(0)));
			if(newPet == null) {
				player.sendMessage(ChatColor.RED + "Your scroll is invalid or outdated!");
				return;
			}
			ItemStack newPetItemStack = WauzPetEgg.getEggItem(player, newPet, null, System.currentTimeMillis());
			PetObtainEvent.call(player, newPet);
			LootContainer.open(player, Collections.singletonList(newPetItemStack));
			player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_EGG_HATCH, 1, 1);
			scroll.setAmount(scroll.getAmount() - 1);
		}
		else if(scrollName.contains("Blueprint: ") && WauzTowers.tryToConstruct(player, StringUtils.substringAfter(scrollName, ": "))) {
			scroll.setAmount(scroll.getAmount() - 1);
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
		
		String scrollName = Components.displayName(scrollItemStack.getItemMeta());
		InventoryScroll scroll = getScroll(ChatColor.stripColor(scrollName));
		if(scroll != null && scroll.use(event, itemName)) {
			scrollItemStack.setAmount(scrollItemStack.getAmount() - 1);
			event.setCancelled(true);
		}
	}

}
