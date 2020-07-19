package eu.wauz.wauzcore.players.classes.mage;

import eu.wauz.wauzcore.players.classes.ClassMage;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;

/**
 * A subclass / mastery, that belongs to a player class.
 * Specialization: Offensive, Undead Summoner
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 * @see ClassMage
 */
public class SubclassNecromancer implements WauzPlayerSubclass {
	
	/**
	 * The static name of the subclass.
	 */
	public static String CLASS_NAME = "Necromancer";
	
	/**
	 * @return The name of the subclass.
	 */
	@Override
	public String getSubclassName() {
		return CLASS_NAME;
	}

}
