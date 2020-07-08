package eu.wauz.wauzcore.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public interface CustomItem {
	
	/**
	 * Handles the interaction with the item.
	 * 
	 * @param event The interaction event.
	 */
	public void use(PlayerInteractEvent event);
	
	/**
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	public List<Material> getCustomItemMaterials();

}
