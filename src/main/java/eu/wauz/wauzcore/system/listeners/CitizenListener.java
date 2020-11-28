package eu.wauz.wauzcore.system.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import eu.wauz.wauzcore.menu.CitizenInteractionMenu;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizen;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizenCache;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizenSpawner;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.jitse.npclib.api.events.NPCHideEvent;
import net.jitse.npclib.api.events.NPCInteractEvent;
import net.jitse.npclib.api.events.NPCShowEvent;

/**
 * A listener to catch events, related to citizens or other custom npcs.
 * 
 * @author Wauzmons
 */
public class CitizenListener implements Listener {
	
	/**
	 * Opens the citzen interaction menu for the player who clicked the npc.
	 * 
	 * @param event The interact event.
	 * 
	 * @see CitizenInteractionMenu#open(Player, WauzCitizen)
	 */
	@EventHandler
	public void onCitizenInteraction(NPCInteractEvent event) {
		if(WauzMode.isMMORPG(event.getWhoClicked()) && Cooldown.playerEntityInteraction(event.getWhoClicked())) {
			WauzCitizen citizen = WauzCitizenSpawner.getCitizen(event.getNPC());
			CitizenInteractionMenu.open(event.getWhoClicked(), citizen);
		}
	}
	
	/**
	 * Updates the cache when a citizen is shown.
	 * 
	 * @param event The show event.
	 * 
	 * @see WauzCitizenCache#markAsShown(net.jitse.npclib.api.NPC, Player)
	 */
	@EventHandler
	public void onCitizenShow(NPCShowEvent event) {
		WauzCitizenCache.markAsShown(event.getNPC(), event.getPlayer());
	}
	
	/**
	 * Updates the cache when a citizen is hidden.
	 * 
	 * @param event The hide event.
	 * 
	 * @see WauzCitizenCache#markAsHidden(net.jitse.npclib.api.NPC, Player)
	 */
	@EventHandler
	public void onCitizenShow(NPCHideEvent event) {
		WauzCitizenCache.markAsHidden(event.getNPC(), event.getPlayer());
	}

}
