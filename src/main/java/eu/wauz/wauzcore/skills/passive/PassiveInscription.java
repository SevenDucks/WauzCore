package eu.wauz.wauzcore.skills.passive;

import eu.wauz.wauzcore.system.annotations.PassiveSkill;

/**
 * A passive skill, that unlocks inscription recipes, leveled through crafting scrolls and runes.
 * 
 * @author Wauzmons
 */
@PassiveSkill
public class PassiveInscription extends AbstractPassiveJobSkill {

	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Inscription";
	
	/**
	 * Creates an empty instance of this passive skill.
	 */
	public PassiveInscription() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveInscription(long exp) {
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
		return new PassiveInscription(exp);
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
