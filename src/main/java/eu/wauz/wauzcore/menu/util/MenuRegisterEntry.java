package eu.wauz.wauzcore.menu.util;

import java.util.List;

import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A public inventory menu for usage in commands or similar.
 * 
 * @author Wauzmons
 *
 * @see MenuRegister
 */
public class MenuRegisterEntry {
	
	/**
	 * The inventory menu.
	 */
	private WauzInventory inventory;
	
	/**
	 * The modes in which the inventory menu can be opened.
	 */
	private List<WauzMode> validModes;
	
	/**
	 * Creates a new public inventory menu entry.
	 * 
	 * @param inventory The inventory menu.
	 * @param validModes The modes in which the inventory menu can be opened.
	 */
	public MenuRegisterEntry(WauzInventory inventory, List<WauzMode> validModes) {
		this.inventory = inventory;
		this.validModes = validModes;
	}
	
	/**
	 * @return The inventory menu.
	 */
	public WauzInventory getInventory() {
		return inventory;
	}
	
	/**
	 * @return The modes in which the inventory menu can be opened.
	 */
	public List<WauzMode> getValidModes() {
		return validModes;
	}

}
