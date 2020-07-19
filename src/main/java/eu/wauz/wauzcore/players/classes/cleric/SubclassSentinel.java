package eu.wauz.wauzcore.players.classes.cleric;

import eu.wauz.wauzcore.players.classes.ClassCleric;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;

/**
 * A subclass / mastery, that belongs to a player class.
 * Specialization: Defensive, Energy Shielding
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 * @see ClassCleric
 */
public class SubclassSentinel implements WauzPlayerSubclass {
	
	/**
	 * The static name of the subclass.
	 */
	public static String CLASS_NAME = "Sentinel";
	
	/**
	 * @return The name of the subclass.
	 */
	@Override
	public String getSubclassName() {
		return CLASS_NAME;
	}

}
