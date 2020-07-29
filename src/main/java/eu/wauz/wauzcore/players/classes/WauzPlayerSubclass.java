package eu.wauz.wauzcore.players.classes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * A subclass / mastery, that belongs to a player class.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 */
public interface WauzPlayerSubclass {
	
	/**
	 * @return The name of the subclass.
	 */
	public String getSubclassName();
	
	/**
	 * @return The description of the subclass.
	 */
	public String getSublassDescription();
	
	/**
	 * @return The color associated with the subclass.
	 */
	public ChatColor getSublassColor();
	
	/**
	 * @return The item stack representing the subclass.
	 */
	public ItemStack getSubclassItemStack();
	
}
