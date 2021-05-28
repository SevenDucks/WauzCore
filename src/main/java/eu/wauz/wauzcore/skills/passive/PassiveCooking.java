package eu.wauz.wauzcore.skills.passive;

import eu.wauz.wauzcore.system.annotations.PassiveSkill;

/**
 * A passive skill, that unlocks cooking recipes, leveled through crafting food and potions.
 * 
 * @author Wauzmons
 */
@PassiveSkill
public class PassiveCooking extends AbstractPassiveJobSkill {

	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Cooking";
	
	/**
	 * Creates an empty instance of this passive skill.
	 */
	public PassiveCooking() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveCooking(long exp) {
		super(exp);
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 * 
	 * @return The created instance.
	 */
	@Override
	public AbstractPassiveSkill getInstance(long exp) {
		return new PassiveCooking(exp);
	}
	
	/**
	 * Gets the name of the passive skill.
	 * 
	 * @return The name of the skill.
	 */
	@Override
	public String getPassiveName() {
		return PASSIVE_NAME;
	}

}
