package eu.wauz.wauzcore.system.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.util.PetEggUtils;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;

/**
 * A listener to catch events, related to the standalone pet module.
 * 
 * @author Wauzmons
 */
public class PetModuleListener implements Listener {
	
	/**
	 * Loads and initializes all data for the standalone pet module.
	 */
	public PetModuleListener() {
		WauzPet.init();
		WauzCore.getInstance().getLogger().info("Loaded Standalone Pet Module!");
	}
	
	/**
	 * Handles the interaction between players and pet eggs.
	 * 
	 * @param event The interact event.
	 * 
	 * @see WauzPetEgg#tryToSummon(PlayerInteractEvent)
	 */
	@EventHandler
	public void onInteraction(PlayerInteractEvent event) {
		ItemStack itemStack = event.getPlayer().getEquipment().getItemInMainHand();
		if(itemStack.getType().toString().endsWith("_SPAWN_EGG") && PetEggUtils.isEggItem(itemStack)) {
			WauzPetEgg.tryToSummon(event);
			event.setCancelled(true);
		}
	}

}
