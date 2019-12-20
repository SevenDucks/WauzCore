package eu.wauz.wauzcore.skills;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A skill, that can be executed by a player.
 * "Poison Touch" hits an enemy within 3 blocks and causes 600% damage over the course of 5 seconds.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillTheHierophant implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static String SKILL_NAME = "The Hierophant V";

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
		return "Melee";
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Poison Touch";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 18;
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
		final Entity target = SkillUtils.getTargetInLine(player, 3);
		
		if(target != null) {
			int damage = (int) ((double) EquipmentUtils.getBaseAtk(weapon) * (double) 1.20);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1, 0.5f);
			SkillParticle particle = new SkillParticle(Particle.SLIME);
			ParticleSpawner.spawnParticleWave(target.getLocation(), particle, 3);
			SkillUtils.callPlayerDamageOverTimeEvent(player, target, Color.LIME, damage, 5, 20);
			return true;
		}
		return false;
	}

}
