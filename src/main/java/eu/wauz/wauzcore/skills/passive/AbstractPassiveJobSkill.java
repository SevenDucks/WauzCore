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
			5L, // 01
			15L, // 02
			30L, // 03
			50L, // 04
			75L, // 05
			105L, // 06
			140L, // 07
			180L, // 08
			225L, // 09
			275L, // 10
			330L, // 11
			390L, // 12
			455L, // 13
			525L, // 14
			600L, // 15
			680L, // 16
			765L, // 17
			855L, // 18
			950L, // 19
			1050L, // 20
			1155L, // 21
			1265L, // 22
			1380L, // 23
			1500L, // 24
			1625L, // 25
			1755L, // 26
			1990L, // 27
			2130L, // 28
			2275L, // 29
			2425L); // 30
	
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
	 * Method that gets called when skill experience is gained.
	 * 
	 * @param player The player who earned the experience.
	 */
	@Override
	protected void onExperienceGain(Player player) {
		player.sendTitle("", getProgressString(), 2, 32, 4);
	}

}
