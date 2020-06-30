package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.events.WauzPlayerEventHomeChange;
import eu.wauz.wauzcore.items.identifiers.WauzIdentifier;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.economy.WauzShopActions;

/**
 * A class for handling the usage of scrolls and socketable items.
 * 
 * @author Wauzmons
 */
public class WauzScrolls {
	
	/**
	 * A list of materials a scroll or socketable item can have.
	 */
	private static List<Material> validScrollMaterials = new ArrayList<Material>(Arrays.asList(
			Material.NAME_TAG, Material.FIREWORK_STAR, Material.REDSTONE));
	
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
	 * @param itemName The name of the scroll or socketable item.
	 * 
	 * @see WauzScrolls#onScrollItemInteract(PlayerInteractEvent) For right click scrolls...
	 * @see WauzIdentifier#identify(InventoryClickEvent, String)
	 * @see WauzShopActions#sell(Player, ItemStack, boolean)
	 * @see WauzShopActions#repair(Player, ItemStack, boolean)
	 * @see WauzEquipment#clearAllSockets(InventoryClickEvent)
	 * @see WauzEquipment#insertRune(InventoryClickEvent)
	 * @see WauzEquipment#insertSkillgem(InventoryClickEvent)
	 */
	public static void onScrollItemInteract(InventoryClickEvent event, String itemName) {
		Player player = (Player) event.getWhoClicked();
		ItemStack scroll = (player.getItemOnCursor());
		if(!validScrollMaterials.contains(scroll.getType())) {
			return;
		}
		String scrollName = scroll.getItemMeta().getDisplayName();
		
		ItemStack itemStack = event.getCurrentItem();
		boolean isNotScroll = !itemName.contains("Scroll");
		boolean isIdentified = !itemName.contains("Unidentified");
		
		if(isNotScroll && scrollName.contains("Scroll of Wisdom")) {
			if(!isIdentified) {
				WauzIdentifier.identify(event, itemName);
				AchievementTracker.addProgress(player, WauzAchievementType.IDENTIFY_ITEMS, 1);
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
		else if(isNotScroll && scrollName.contains("Scroll of Fortune")) {
			if(WauzShopActions.sell((Player) player, itemStack, false)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}	
		}
		else if(isNotScroll && scrollName.contains("Scroll of Toughness")) {
			if(WauzShopActions.repair((Player) player, itemStack, false)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
		else if(isNotScroll && scrollName.contains("Scroll of Regret")) {
			if(WauzEquipment.clearAllSockets(event)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
		else if(scrollName.contains("Rune")) {
			if(isIdentified && WauzEquipment.insertRune(event)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
		else if(scrollName.contains("Skillgem")) {
			if(isIdentified && WauzEquipment.insertSkillgem(event)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
	}

}
