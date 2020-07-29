package eu.wauz.wauzcore.players.classes.rogue;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.heads.CharacterIconHeads;
import eu.wauz.wauzcore.players.classes.ClassRogue;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;

/**
 * A subclass / mastery, that belongs to a player class.
 * Specialization: Support, Buffing and Debuffing
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 * @see ClassRogue
 */
public class SubclassBard implements WauzPlayerSubclass {
	
	/**
	 * The static name of the subclass.
	 */
	public static String CLASS_NAME = "Bard";
	
	/**
	 * @return The name of the subclass.
	 */
	@Override
	public String getSubclassName() {
		return CLASS_NAME;
	}
	
	/**
	 * @return The description of the subclass.
	 */
	@Override
	public String getSublassDescription() {
		return "Support, Buffing and Debuffing";
	}
	
	/**
	 * @return The color associated with the subclass.
	 */
	@Override
	public ChatColor getSublassColor() {
		return ChatColor.GREEN;
	}
	
	/**
	 * @return The item stack representing the subclass.
	 */
	@Override
	public ItemStack getSubclassItemStack() {
		return CharacterIconHeads.getBardItem();
	}
	
}
