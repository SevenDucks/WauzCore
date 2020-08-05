package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A skill, that can be executed by a player.
 * "Shockwave" damages all enemies in a radius of 8 blocks and throws them into the air.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillJudgement implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "Judgement XX";

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
	public WauzPlayerSkillType getSkillDescriptionType() {
		return WauzPlayerSkillType.AOE;
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Shockwave";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "ShckWv";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 25;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 12;
	}
	
	/**
	 * @return If rage should be used for this skill instead of mana.
	 */
	@Override
	public boolean isPhysical() {
		return true;
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
		List<Entity> targets = SkillUtils.getTargetsInRadius(location, 8);
		
		player.getWorld().playSound(location, Sound.ITEM_SHIELD_BREAK, 1, 2.0f);
		player.getWorld().playSound(location, Sound.ITEM_SHIELD_BREAK, 1, 1.5f);
		player.getWorld().playSound(location, Sound.ITEM_SHIELD_BREAK, 1, 1.0f);
		player.getWorld().playSound(location, Sound.ITEM_SHIELD_BREAK, 1, 0.5f);
		SkillParticle particle = new SkillParticle(Color.TEAL);
		ParticleSpawner.spawnParticleWave(location, particle, 12);
		SkillUtils.callPlayerMagicDamageEvent(player, targets, 1);
		SkillUtils.throwEntitiesIntoAir(targets, 1.4);
		
		return targets.size() > 0;
	}

}
