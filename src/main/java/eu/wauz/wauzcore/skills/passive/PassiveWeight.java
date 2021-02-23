package eu.wauz.wauzcore.skills.passive;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.menu.Backpack;
import eu.wauz.wauzcore.system.annotations.PassiveSkill;

/**
 * A passive skill, that increases backpack size, leveled through picking up materials.
 * 
 * @author Wauzmons
 */
@PassiveSkill
public class PassiveWeight extends AbstractPassiveSkill {

	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Weight";
	
	/**
	 * The static list of experience milestones.
	 */
	private static final List<Long> MILESTONES = Arrays.asList(
			40L, // 01
			120L, // 02
			200L, // 03
			320L, // 04
			480L, // 05
			700L, // 06
			960L, // 07
			1400L, // 08
			2000L, // 09
			3200L, // 10
			6000L, // 11
			12000L, // 12
			24000L, // 13
			36000L, // 14
			48000L, // 15
			60000L, // 16
			72000L, // 17
			84000L, // 18
			96000L, // 19
			120000L); // 20

	/**
	 * Creates an empty instance of this passive skill.
	 */
	public PassiveWeight() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveWeight(long exp) {
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
		return new PassiveWeight(exp);
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
