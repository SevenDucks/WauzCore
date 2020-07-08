package eu.wauz.wauzcore.menu.heads;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.util.DeprecatedUtils;

/**
 * An util class to create item stacks of specific heads / skulls.
 * 
 * @author Wauzmons
 */
public class HeadUtils {
	
	/**
	 * Creates a player head item stack, based on a base64 string.
	 * 
	 * @param base64 A base64 string representing the data value of a skin.
	 * 
	 * @return A player head with the given skin.
	 */
	public static ItemStack getPlayerHead(String base64) {
		ItemStack headItemStack = new ItemStack(Material.PLAYER_HEAD);
		UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
		return DeprecatedUtils.addPlayerHeadTexture(headItemStack, base64, hashAsId);
	}

	/**
	 * Name based equals check for head menu items.
	 * 
	 * @param itemStack The item to check.
	 * @param itemName The name to check for.
	 * 
	 * @return If the item is a head and has the given name.
	 */
	public static boolean isHeadMenuItem(ItemStack itemStack, String itemName) {
		return ItemUtils.isMaterial(itemStack, Material.PLAYER_HEAD) && ItemUtils.isSpecificItem(itemStack, itemName);
	}
	
}
