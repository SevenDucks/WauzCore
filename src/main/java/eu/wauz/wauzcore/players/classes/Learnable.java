package eu.wauz.wauzcore.players.classes;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

/**
 * A skill or similar, that can be learned through a subclass / mastery.
 * 
 * @author Wauzmons
 */
public class Learnable {
	
	/**
	 * The skill to be learned.
	 */
	private WauzPlayerSkill skill;
	
	/**
	 * The mastery level, at which the skill is learned.
	 */
	private int level;
	
	/**
	 * Constructs a learnable skill.
	 * 
	 * @param skill The skill to be learned.
	 * @param level The mastery level, at which the skill is learned.
	 */
	public Learnable(WauzPlayerSkill skill, int level) {
		this.skill = skill;
		this.level = level;
	}

	/**
	 * @return The skill to be learned.
	 */
	public WauzPlayerSkill getSkill() {
		return skill;
	}

	/**
	 * @return The mastery level, at which the skill is learned.
	 */
	public int getLevel() {
		return level;
	}
	
}
