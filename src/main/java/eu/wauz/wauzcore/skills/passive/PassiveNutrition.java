package eu.wauz.wauzcore.skills.passive;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;

/**
 * A passive skill, that increases maximum health, leveled through eating.
 * 
 * @author Wauzmons
 */
public class PassiveNutrition extends AbstractPassiveSkill {

	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Nutrition";
	
	/**
	 * The static list of experience milestones.
	 */
	private static final List<Long> MILESTONES = Arrays.asList(
			10l, // 01
			30l, // 02
			50l, // 03
			80l, // 04
			120l, // 05
			175l, // 06
			240l, // 07
			350l, // 08
			500l, // 09
			800l, // 10
			1500l, // 11
			3000l, // 12
			6000l, // 13
			9000l, // 14
			12000l, // 15
			15000l, // 16
			18000l, // 17
			21000l, // 18
			24000l, // 19
			30000l); // 20

	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveNutrition(long exp) {
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
		DamageCalculator.setHealth(player, WauzPlayerDataPool.getPlayer(player).getMaxHealth());
	}
	
}
