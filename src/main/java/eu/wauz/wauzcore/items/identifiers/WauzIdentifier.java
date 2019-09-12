package eu.wauz.wauzcore.items.identifiers;

import org.bukkit.event.inventory.InventoryClickEvent;

public class WauzIdentifier {
	
	public static void identify(InventoryClickEvent event, String itemName) {
		if(itemName.contains("Item"))
			new WauzEquipmentIdentifier().identifyItem(event);
		else if(itemName.contains("Rune"))
			new WauzRuneIdentifier().identifyRune(event);
		else if(itemName.contains("Skillgem"))
			new WauzSkillgemIdentifier().identifySkillgem(event);
	}

}
