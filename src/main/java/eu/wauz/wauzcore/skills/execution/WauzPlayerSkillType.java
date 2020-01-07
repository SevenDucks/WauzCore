package eu.wauz.wauzcore.skills.execution;

/**
 * The targeting type of a player skill.
 * 
 * @author Wauzmons
 */
public enum WauzPlayerSkillType {

	/**
	 * Type of close ranged skills.
	 */
	MELEE("Melee"),
	
	/**
	 * Type of far ranged skills.
	 */
	RANGED("Ranged"),
	
	/**
	 * Type of skills that affect the caster.
	 */
	SELF("Self"),
	
	/**
	 * Type of skills with an area of effect.
	 */
	AOE("AoE"),
	
	/**
	 * Type of skills that summon minions.
	 */
	SUMMON("Summon"),
	
	/**
	 * Type of other skills.
	 */
	OTHER("Other");
	
	/**
	 * The name of the skill type.
	 */
	private String name;
	
	/**
	 * Creates a new skill type with given name.
	 * 
	 * @param name The name of the skill type.
	 */
	private WauzPlayerSkillType(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the skill type in a description friendly format.
	 * 
	 * @return The name of the skill type.
	 */
	@Override
	public String toString() {
		return name;
	}
	
}
