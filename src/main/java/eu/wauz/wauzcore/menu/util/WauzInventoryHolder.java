package eu.wauz.wauzcore.menu.util;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class WauzInventoryHolder implements InventoryHolder {
	
	private WauzInventory inventory;
	
	public WauzInventoryHolder(WauzInventory inventory) {
		this.inventory = inventory;
	}

	@Override
	@Deprecated
	public Inventory getInventory() {
		return null;
	}
	
	public String getInventoryName() {
		return inventory.getClass().getName();
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		inventory.selectMenuPoint(event);
	}
	
}
