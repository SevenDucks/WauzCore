package eu.wauz.wauzcore.skills.passive;

import eu.wauz.wauzcore.system.annotations.PassiveSkill;

/**
 * A passive skill, that unlocks smithing recipes, leveled through crafting equipment and gems.
 * 
 * @author Wauzmons
 */
@PassiveSkill
public class PassiveSmithing extends AbstractPassiveJobSkill {

	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Smithing";
	
	/**
	 * Creates an empty instance of this passive skill.
	 */
	public PassiveSmithing() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveSmithing(long exp) {
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
		return new PassiveSmithing(exp);
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
