package eu.wauz.wauzcore.players.classes;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.ArmorCategory;

/**
 * A class, that can be chosen by a player.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClassPool
 */
public interface WauzPlayerClass {
	
	/**
	 * @return The name of the class.
	 */
	public String getClassName();
	
	/**
	 * @return The color associated with the class.
	 */
	public ChatColor getClassColor();
	
	/**
	 * @return The item stack representing the class.
	 */
	public ItemStack getClassItemStack();
	
	/**
	 * @return The highest weight armor category the class can wear.
	 */
	public ArmorCategory getArmorCategory();
	
	/**
	 * @return The starting stats and passive skills of the class.
	 */
	public WauzPlayerClassStats getStartingStats();
	
	/**
	 * @return The weapon item stack the class will start with.
	 */
	public ItemStack getStartingWeapon();

}
