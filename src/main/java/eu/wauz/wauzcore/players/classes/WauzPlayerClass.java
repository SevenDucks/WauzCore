package eu.wauz.wauzcore.players.classes;

import java.util.List;

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
	 * @return The description of the class.
	 */
	public String getClassDescription();
	
	/**
	 * @return The color associated with the class.
	 */
	public ChatColor getClassColor();
	
	/**
	 * @return The item stack representing the class.
	 */
	public ItemStack getClassItemStack();
	
	/**
	 * @return All subclasses of the class.
	 */
	public List<WauzPlayerSubclass> getSubclasses();
	
	/**
	 * @param subclass The name of a subclass.
	 * 
	 * @return The subclass with that name or null.
	 */
	public WauzPlayerSubclass getSubclass(String subclass);
	
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
