package eu.wauz.wauzcore.players.classes;

import java.util.List;

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
	
	/**
	 * @return A list of all learnables from the mastery.
	 */
	public List<Learnable> getLearnables();
	
	/**
	 * Gets all learned learnables at the given mastery level.
	 * 
	 * @param masteryLevel The level of the mastery.
	 * 
	 * @return The learned learnables.
	 */
	public List<Learnable> getLearned(int masteryLevel);
	
	/**
	 * Determines the next learnable to learn in the mastery.
	 * 
	 * @param masteryLevel The level of the mastery.
	 * 
	 * @return The next learnable to learn.
	 */
	public Learnable getNextLearnable(int masteryLevel);
	
}
