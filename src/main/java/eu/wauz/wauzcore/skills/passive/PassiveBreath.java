package eu.wauz.wauzcore.skills.passive;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.calc.SpeedCalculator;
import eu.wauz.wauzcore.system.annotations.PassiveSkill;

/**
 * A passive skill, that increases movement speed, leveled through sprinting.
 * 
 * @author Wauzmons
 */
@PassiveSkill
public class PassiveBreath extends AbstractPassiveSkill {
	
	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Breath";
	
	/**
	 * The static list of experience milestones.
	 */
	private static final List<Long> MILESTONES = Arrays.asList(
			TimeUnit.MINUTES.toMillis(5), // 01
			TimeUnit.MINUTES.toMillis(12), // 02
			TimeUnit.MINUTES.toMillis(30), // 03
			TimeUnit.HOURS.toMillis(1), // 04
			TimeUnit.HOURS.toMillis(2), // 05
			TimeUnit.HOURS.toMillis(3), // 06
			TimeUnit.HOURS.toMillis(5), // 07
			TimeUnit.HOURS.toMillis(7), // 08
			TimeUnit.HOURS.toMillis(10), // 09
			TimeUnit.HOURS.toMillis(15), // 10
			TimeUnit.HOURS.toMillis(20), // 11
			TimeUnit.HOURS.toMillis(25), // 12
			TimeUnit.HOURS.toMillis(30), // 13
			TimeUnit.DAYS.toMillis(2), // 14
			TimeUnit.DAYS.toMillis(3), // 15
			TimeUnit.DAYS.toMillis(4), // 16
			TimeUnit.DAYS.toMillis(6), // 17
			TimeUnit.DAYS.toMillis(8), // 18
			TimeUnit.DAYS.toMillis(10), // 19
			TimeUnit.DAYS.toMillis(12)); // 20

	/**
	 * Creates an empty instance of this passive skill.
	 */
	public PassiveBreath() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveBreath(long exp) {
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
		return new PassiveBreath(exp);
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
		SpeedCalculator.resetWalkSpeed(player);
	}

}
