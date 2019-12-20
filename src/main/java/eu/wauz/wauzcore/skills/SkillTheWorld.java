package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

/**
 * A skill, that can be executed by a player.
 * "Time Freeze" leaps the player into the air, to freeze all enemies in a radius of 6 blocks for 5 seconds,
 * making them unable to move in that timeframe.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillTheWorld implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static String SKILL_NAME = "The World XXI";

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
		return "AoE";
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Time Freeze";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 12;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 6;
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
		Location location = player.getLocation();
		SkillUtils.throwEntityIntoAir(player, 0.6);
		player.getWorld().playSound(location, Sound.ENTITY_WITHER_DEATH, 1, 1);
		
		List<Entity> targets = SkillUtils.getTargetsInRadius(location, 6);
		SkillUtils.addPotionEffect(targets, PotionEffectType.SLOW, 5, 200);
		SkillUtils.addPotionEffect(targets, PotionEffectType.JUMP, 5, 200);
		SkillUtils.addPotionEffect(targets, PotionEffectType.GLOWING, 5, 200);
		
		return targets.size() > 0;
	}

}
