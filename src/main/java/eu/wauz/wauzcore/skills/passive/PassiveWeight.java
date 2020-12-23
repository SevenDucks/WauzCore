package eu.wauz.wauzcore.skills.passive;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.menu.Backpack;

/**
 * A passive skill, that increases backpack size, leveled through picking up materials.
 * 
 * @author Wauzmons
 */
public class PassiveWeight extends AbstractPassiveSkill {

	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Weight";
	
	/**
	 * The static list of experience milestones.
	 */
	private static final List<Long> MILESTONES = Arrays.asList(
			40l, // 01
			120l, // 02
			200l, // 03
			320l, // 04
			480l, // 05
			700l, // 06
			960l, // 07
			1400l, // 08
			2000l, // 09
			3200l, // 10
			6000l, // 11
			12000l, // 12
			24000l, // 13
			36000l, // 14
			48000l, // 15
			60000l, // 16
			72000l, // 17
			84000l, // 18
			96000l, // 19
			120000l); // 20

	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveWeight(long exp) {
		super(exp);
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
		Backpack.updateLockedSlots(player);
	}
	
}
