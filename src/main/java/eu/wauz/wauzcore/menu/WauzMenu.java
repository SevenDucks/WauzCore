package eu.wauz.wauzcore.menu;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.CustomItem;
import eu.wauz.wauzcore.menu.abilities.AbilityMenuParts;
import eu.wauz.wauzcore.menu.collection.CollectionMenuParts;
import eu.wauz.wauzcore.menu.social.SocialMenuParts;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.system.annotations.Item;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * The main menu of the game, that will by default show all sub menus of the MMORPG mode.
 * 
 * @author Wauzmons
 * 
 * @see WauzModeMenu
 */
@PublicMenu
@Item
public class WauzMenu implements WauzInventory, CustomItem {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "main-menu";
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
		WauzMenu.open(player);
	}
	
	/**
	 * Handles the interaction with the menu opener item.
	 * 
	 * @param event The interaction event.
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		WauzMenu.open(event.getPlayer());
	}
	
	/**
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.NETHER_STAR);
	}
	
	/**
	 * Opens the menu for the given player.
	 * If it is opened in the hub, it will be redirected to the mode selection.
	 * Else all sub menus of the MMORPG mode plus a short information is shown.
	 * This includes the ability, collection and social menus.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzModeMenu#open(Player)
	 * @see SocialMenuParts#addMenuParts(Player, Inventory, int)
	 * @see CollectionMenuParts#addMenuParts(Player, Inventory, int)
	 * @see AbilityMenuParts#addMenuParts(Player, Inventory, int)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		if(WauzMode.inHub(player)) {
			WauzModeMenu.open(player);
			return;
		}
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Main Menu";
		Inventory menu = Components.inventory(new WauzMenu(), menuTitle, 18);
		
		SocialMenuParts.addMenuParts(player, menu, 0);
		CollectionMenuParts.addMenuParts(player, menu, 3);
		AbilityMenuParts.addMenuParts(player, menu, 7);

		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a sub menu was clicked, it will be opened.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see SocialMenuParts#check(Player, ItemStack)
	 * @see CollectionMenuParts#check(Player, ItemStack)
	 * @see AbilityMenuParts#check(Player, ItemStack)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null
				|| SocialMenuParts.check(player, clicked)
				|| CollectionMenuParts.check(player, clicked)
				|| AbilityMenuParts.check(player, clicked)) {
			return;
		}
	}

}
