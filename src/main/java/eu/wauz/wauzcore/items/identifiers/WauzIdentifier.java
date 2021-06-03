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
	 * @param manual If the identification was triggered manually.
	 * 
	 * @see WauzIdentifier#identify(Player, ItemStack, String, boolean)
	 */
	public static void identify(InventoryClickEvent event, String itemName, boolean manual) {
		Player player = (Player) event.getWhoClicked();
		ItemStack itemStack = event.getCurrentItem();	
		identify(player, itemStack, itemName, manual);
	}
	
	/**
	 * Creates and runs a typed identifier for the item stack, based on the given name.
	 * 
	 * @param player The player who identifies the item.
	 * @param itemStack The item stack, that is getting identified.
	 * @param itemName The name of the item to identify.
	 * @param manual If the identification was triggered manually.
	 */
	public static void identify(Player player, ItemStack itemStack, String itemName, boolean manual) {
		if(itemName.contains("Item")) {
			new WauzEquipmentIdentifier().identifyItem(player, itemStack, manual);
		}
		else if(itemName.contains("Rune")) {
			new WauzRuneIdentifier().identifyRune(player, itemStack, manual);
		}
		else if(itemName.contains("Skillgem")) {
			new WauzSkillgemIdentifier().identifySkillgem(player, itemStack, manual);
		}
	}

}
