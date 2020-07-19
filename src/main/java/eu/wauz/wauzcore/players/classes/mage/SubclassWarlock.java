package eu.wauz.wauzcore.players.classes.mage;

import eu.wauz.wauzcore.players.classes.ClassMage;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;

/**
 * A subclass / mastery, that belongs to a player class.
 * Specialization: Support, Life Leech and Frost Magic
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 * @see ClassMage
 */
public class SubclassWarlock implements WauzPlayerSubclass {
	
	/**
	 * The static name of the subclass.
	 */
	public static String CLASS_NAME = "Warlock";
	
	/**
	 * @return The name of the subclass.
	 */
	@Override
	public String getSubclassName() {
		return CLASS_NAME;
	}

}
