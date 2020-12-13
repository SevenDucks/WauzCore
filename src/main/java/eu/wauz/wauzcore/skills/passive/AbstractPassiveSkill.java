package eu.wauz.wauzcore.skills.passive;

import java.util.List;

/**
 * An abstract passive skill, that can be leveled.
 * 
 * @author Wauzmons
 */
public abstract class AbstractPassiveSkill {
	
	/**
	 * The current experience in this skill.
	 */
	private int exp;
	
	/**
	 * Creates a new instance of this skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public AbstractPassiveSkill(int exp) {
		this.exp = exp;
	}
	
	/**
	 * Get all experience milestones, marking where new levels are reached.
	 * 
	 * @return A list of experience milestones.
	 */
	public abstract List<Integer> getMilestones();

}
