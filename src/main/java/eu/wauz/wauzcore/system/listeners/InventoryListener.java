package eu.wauz.wauzcore.system.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.EventMapper;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A listener to catch events, related to inventory and menu interactions.
 * 
 * @author Wauzmons
 */
public class InventoryListener implements Listener {
	
	/**
	 * Lets the mapper decide how to handle the interaction with an inventory menu.
	 * 
	 * @param event The click event.
	 * 
	 * @see EventMapper#handleMenuInteraction(InventoryClickEvent)
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		EventMapper.handleMenuInteraction(event);
	}
	
	/**
	 * Closes the current inventory when opening a new inventory.
	 * 
	 * @param event The open event.
	 */
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		event.getPlayer().closeInventory();
	}
	
	/**
	 * Lets the mapper decide how to handle the closing of an inventory menu.
	 * 
	 * @param event The close event.
	 * 
	 * @see EventMapper#handleMenuClose(InventoryCloseEvent)
	 */
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		EventMapper.handleMenuClose(event);
	}

	/**
	 * Updates the scoreboard of an MMORPG player,
	 * in case they dropped an item that is relevant to a quest.
	 * Also prevents dropping of static items (e.g. mana points).
	 * 
	 * @param event The drop event.
	 * 
	 * @see WauzPlayerScoreboard#scheduleScoreboardRefresh(Player)
	 * @see MenuUtils#checkForStaticItemDrop(PlayerDropItemEvent)
	 */
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			MenuUtils.checkForStaticItemDrop(event);
			WauzPlayerScoreboard.scheduleScoreboardRefresh(event.getPlayer());
		}
		else if(WauzMode.isArcade(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents swapping items in MMORPG or Arcade mode.
	 * 
	 * @param event The swap event.
	 */
	@EventHandler
	public void onSwapItem(PlayerSwapHandItemsEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer()) || WauzMode.isArcade(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents MMORPG players from crafting with normal Minecraft recipes.
	 * 
	 * @param event The craft event.
	 */
	@EventHandler
	public void onItemCraft(PrepareItemCraftEvent event) {
		CraftingInventory inventory = event.getInventory();
		if(!event.getViewers().isEmpty() && WauzMode.isMMORPG(event.getViewers().get(0))) {
			inventory.setResult(new ItemStack(Material.AIR));
		}
	}

}
