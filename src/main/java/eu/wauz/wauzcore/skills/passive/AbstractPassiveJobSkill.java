package eu.wauz.wauzcore.skills.passive;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * An abstract passive skill, meant for a job.
 * 
 * @author Wauzmons
 */
public abstract class AbstractPassiveJobSkill extends AbstractPassiveSkill {
	
	/**
	 * The static list of experience milestones.
	 */
	private static final List<Long> MILESTONES = Arrays.asList(
			0L, // 01
			5L, // 02
			15L, // 03
			30L, // 04
			50L, // 05
			75L, // 06
			105L, // 07
			140L, // 08
			180L, // 09
			225L, // 10
			275L, // 11
			330L, // 12
			390L, // 13
			455L, // 14
			525L, // 15
			600L, // 16
			680L, // 17
			765L, // 18
			855L, // 19
			950L, // 20
			1050L, // 21
			1155L, // 22
			1265L, // 23
			1380L, // 24
			1500L, // 25
			1625L, // 26
			1755L, // 27
			1990L, // 28
			2130L, // 29
			2275L); // 30
	
	/**
	 * Creates an empty instance of this passive skill.
	 */
	public AbstractPassiveJobSkill() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public AbstractPassiveJobSkill(long exp) {
		super(exp);
	}
	
	/**
	 * Gets all experience milestones, marking where new levels are reached.
	 * 
	 * @return A list of experience milestones.
	 */
	@Override
	protected List<Long> getMilestones() {
		return MILESTONES;
	}

	/**
	 * Method that gets called when a new milestone has been reached.
	 * 
	 * @param player The player who reached the milestone.
	 */
	@Override
	protected void onLevelUp(Player player) {
		
	}

}
