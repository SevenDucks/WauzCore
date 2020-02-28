package eu.wauz.wauzcore.system.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.EventMapper;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A listener to catch events, related to inventory and menu interactions.
 * 
 * @author Wauzmons
 */
public class InventoryListener implements Listener {
	
	/**
	 * Lets the mapper decide how to handle the interaction with an inventory / menu.
	 * 
	 * @param event
	 * 
	 * @see EventMapper#handleMenuInteraction(InventoryClickEvent)
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		EventMapper.handleMenuInteraction(event);
	}

	/**
	 * Updates the scoreboard of an MMORPG player,
	 * in case they dropped an item that is relevant to a quest.
	 * Also prevents dropping of static items (e.g. mana points).
	 * 
	 * @param event
	 * 
	 * @see WauzPlayerScoreboard#scheduleScoreboard(Player)
	 * @see MenuUtils#checkForStaticItemDrop(PlayerDropItemEvent)
	 */
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			MenuUtils.checkForStaticItemDrop(event);
			WauzPlayerScoreboard.scheduleScoreboard(event.getPlayer());
		}
	}

	/**
	 * Prevents swapping of static items (e.g. mana points) in MMORPG mode.
	 * 
	 * @param event
	 * 
	 * @see MenuUtils#checkForStaticItemSwap(PlayerSwapHandItemsEvent)
	 */
	@EventHandler
	public void onSwapItem(PlayerSwapHandItemsEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			MenuUtils.checkForStaticItemSwap(event);
		}
	}

}
