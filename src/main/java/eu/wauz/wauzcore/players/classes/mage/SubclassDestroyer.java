package eu.wauz.wauzcore.players.classes.mage;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.heads.CharacterIconHeads;
import eu.wauz.wauzcore.players.classes.ClassMage;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;

/**
 * A subclass / mastery, that belongs to a player class.
 * Specialization: 	Offensive, Arcane and Fire Magic
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 * @see ClassMage
 */
public class SubclassDestroyer implements WauzPlayerSubclass {
	
	/**
	 * The static name of the subclass.
	 */
	public static String CLASS_NAME = "Destroyer";
	
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
		return "Offensive, Arcane and Fire Magic";
	}
	
	/**
	 * @return The color associated with the subclass.
	 */
	@Override
	public ChatColor getSublassColor() {
		return ChatColor.RED;
	}
	
	/**
	 * @return The item stack representing the subclass.
	 */
	@Override
	public ItemStack getSubclassItemStack() {
		return CharacterIconHeads.getDestroyerItem();
	}

}
