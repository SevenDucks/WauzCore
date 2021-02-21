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
			10L, // 01
			30L, // 02
			50L, // 03
			80L, // 04
			120L, // 05
			175L, // 06
			240L, // 07
			350L, // 08
			500L, // 09
			800L, // 10
			1500L, // 11
			3000L, // 12
			6000L, // 13
			9000L, // 14
			12000L, // 15
			15000L, // 16
			18000L, // 17
			21000L, // 18
			24000L, // 19
			30000L); // 20

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
		DamageCalculator.setHealth(player, WauzPlayerDataPool.getPlayer(player).getStats().getMaxHealth());
	}
	
}
