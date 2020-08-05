package eu.wauz.wauzcore.skills;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A skill, that can be executed by a player.
 * "Damage Boost" makes the player deal 150% damage with all attacks for 10 seconds.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillStrength implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "Strength VIII";

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
		return WauzPlayerSkillType.SELF;
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Damage Boost";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "DmgeUp";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 32;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 14;
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
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 2f);
		SkillParticle particle = new SkillParticle(Particle.VILLAGER_ANGRY);
		ParticleSpawner.spawnParticleSphere(player.getLocation(), particle, 1.5);
		SkillUtils.addPotionEffect(player, PotionEffectType.INCREASE_DAMAGE, 10, 0);
		
		return true;
	}

}
