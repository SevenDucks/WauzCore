package eu.wauz.wauzcore.skills.passive;

import eu.wauz.wauzcore.system.annotations.PassiveSkill;

/**
 * A passive skill, that increases herbalism efficiency, leveled through herbalism.
 * 
 * @author Wauzmons
 */
@PassiveSkill
public class PassiveHerbalism extends AbstractPassiveJobSkill {

	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Herbalism";
	
	/**
	 * Creates an empty instance of this passive skill.
	 */
	public PassiveHerbalism() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveHerbalism(long exp) {
		super(exp);
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 * 
	 * @return The created instance.
	 */
	public AbstractPassiveSkill getInstance(long exp) {
		return new PassiveHerbalism(exp);
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
