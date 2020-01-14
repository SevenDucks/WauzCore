package eu.wauz.wauzcore.items.identifiers;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
		Player player = (Player) event.getWhoClicked();
		ItemStack itemStack = event.getCurrentItem();	
		
		if(itemName.contains("Item")) {
			new WauzEquipmentIdentifier().identifyItem(player, itemStack);
		}
		else if(itemName.contains("Rune")) {
			new WauzRuneIdentifier().identifyRune(player, itemStack);
		}
		else if(itemName.contains("Skillgem")) {
			new WauzSkillgemIdentifier().identifySkillgem(player, itemStack);
		}
	}

}
