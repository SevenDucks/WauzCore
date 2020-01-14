package eu.wauz.wauzcore.items.identifiers;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Generic identifier, used for determining what typed identifier should be used.
 * 
 * @author Wauzmons
 * 
 * @see WauzEquipmentIdentifier
 * @see WauzRuneIdentifier
 * @see WauzSkillgemIdentifier
 */
public class WauzIdentifier {
	
	/**
	 * Creates and runs a typed identifier for the event, based on the given name.
	 * 
	 * @param event The inventory event, which triggered the identifying.
	 * @param itemName The name of the item to identify.
	 */
	public static void identify(InventoryClickEvent event, String itemName) {
		if(itemName.contains("Item")) {
			new WauzEquipmentIdentifier().identifyItem(event);
		}
		else if(itemName.contains("Rune")) {
			new WauzRuneIdentifier().identifyRune(event);
		}
		else if(itemName.contains("Skillgem")) {
			new WauzSkillgemIdentifier().identifySkillgem(event);
		}
	}

}
