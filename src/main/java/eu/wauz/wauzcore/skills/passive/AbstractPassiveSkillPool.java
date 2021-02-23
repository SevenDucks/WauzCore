package eu.wauz.wauzcore.skills.passive;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store all abstract passive skills.
 * 
 * @author Wauzmons
 *
 * @see AbstractPassiveSkill
 */
public class AbstractPassiveSkillPool {
	
	/**
	 * A list of all registered passives.
	 */
	private static List<AbstractPassiveSkill> passives = new ArrayList<>();
	
	/**
	 * @return A list of all registered passives.
	 */
	public static List<AbstractPassiveSkill> getPassives() {
		return new ArrayList<>(passives);
	}
	
	/**
	 * Registers a passive.
	 * 
	 * @param passive The passive to register.
	 */
	public static void registerPassive(AbstractPassiveSkill passive) {
		passives.add(passive);
	}

}
