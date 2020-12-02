package eu.wauz.wauzcore.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.annotations.Item;

/**
 * A class for handling the usage of dungeon / instance maps.
 * 
 * @author Wauzmons
 */
@Item
public class WauzDungeonMap implements CustomItem {

	/**
	 * Handles the interaction with a dungeon map item.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see WauzTeleporter#enterInstanceTeleportManual(PlayerInteractEvent)
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		WauzTeleporter.enterInstanceTeleportManual(event);
	}

	/**
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.PAPER);
	}

}
