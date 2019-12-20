package eu.wauz.wauzcore.skills;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;

/**
 * A skill, that can be executed by a player.
 * "Random" casts a random skill, while the cooldown and mana cost stays the same,
 * independant of the skill's own values.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillWheelOfFortune implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static String SKILL_NAME = "Wheel Of Fortune X";

	/**
	 * @return The id of the skill.
	 */
	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	/**
	 * @return The type of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionType() {
		return "Self";
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Random";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 20;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 5;
	}

	/**
	 * Executes the skill for the given player.
	 * 
	 * @param player The player who executes the skill.
	 * @param weapon The weapon that player uses for it.
	 * 
	 * @return If the skill hit something.
	 */
	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		List<WauzPlayerSkill> playerSkills = WauzPlayerSkillExecutor.getAllSkills();
		return playerSkills.get(new Random().nextInt(playerSkills.size())).executeSkill(player, weapon);
	}

}
