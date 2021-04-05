package eu.wauz.wauzcore.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.skills.passive.PassiveWeight;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Represents a virtual chest, the player can use however they want.
 * 
 * @author Wauzmons
 */
@PublicMenu
public class Backpack implements WauzInventory {
	
	/**
	 * The base amount of slots available in the backpack.
	 */
	public static final int BASE_SIZE = 7;
	
	/**
	 * A map of cached backpacks, indexed by player.
	 */
	private static final Map<Player, Inventory> backpackMap = new HashMap<>();
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "backpack";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG);
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		Backpack.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows the contents of the backpack, including locked slots.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public static void open(Player player) {
		player.openInventory(getBackpack(player));
	}
	
	/**
	 * Gets or creates / loads the backpack of the given player.
	 * 
	 * @param player The player who owns the backpack.
	 */
	public static Inventory getBackpack(Player player) {
		Inventory backpack = backpackMap.get(player);
		if(backpack == null) {
			String backpackTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Backpack";
			backpack = Components.inventory(null, backpackTitle, 36);
			backpack.setContents(PlayerCollectionConfigurator.getCharacterInventoryContents(player, "backpack"));
			backpackMap.put(player, backpack);
			updateLockedSlots(player);
		}
		return backpack;
	}
	
	/**
	 * Updates the slots that are locked in the backpack of the given player.
	 * 
	 * @param player The player who owns the backpack.
	 */
	public static void updateLockedSlots(Player player) {
		Inventory backpack = getBackpack(player);
		ItemStack lockedItemStack = new ItemStack(Material.BARRIER);
		MenuUtils.setItemDisplayName(lockedItemStack, ChatColor.RED + "Slot Locked");
		int slotCount = BASE_SIZE + WauzPlayerDataPool.getPlayer(player).getSkills().getCachedPassive(PassiveWeight.PASSIVE_NAME).getLevel();
		for(int slot = 0; slot < backpack.getSize(); slot++) {
			if(slot >= slotCount) {
				backpack.setItem(slot, lockedItemStack);
			}
			else if(ItemUtils.isMaterial(backpack.getItem(slot), Material.BARRIER)) {
				backpack.setItem(slot, null);
			}
		}
	}
	
	/**
	 * Unloads the cache of the backpack of the given player.
	 * 
	 * @param player The player who owns the backpack.
	 */
	public static void unloadBackpack(Player player) {
		backpackMap.remove(player);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The event will be normally executed, just like inside a chest.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		
	}

}
