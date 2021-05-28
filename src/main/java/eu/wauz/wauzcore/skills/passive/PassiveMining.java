package eu.wauz.wauzcore.skills.passive;

import eu.wauz.wauzcore.system.annotations.PassiveSkill;

/**
 * A passive skill, that increases mining efficiency, leveled through collecting ore.
 * 
 * @author Wauzmons
 */
@PassiveSkill
public class PassiveMining extends AbstractPassiveJobSkill {

	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Mining";
	
	/**
	 * Creates an empty instance of this passive skill.
	 */
	public PassiveMining() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveMining(long exp) {
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
		return new PassiveMining(exp);
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
