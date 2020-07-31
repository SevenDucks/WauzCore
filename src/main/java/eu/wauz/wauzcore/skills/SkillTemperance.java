package eu.wauz.wauzcore.skills;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A skill, that can be executed by a player.
 * "Mana Restore" instantly restores 8 of the player's Mana Points, but stuns them a short time.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillTemperance implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "Temperance XIV";

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
		return "Mana Restore";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 60;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 0;
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
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1.5f);
		SkillParticle particle = new SkillParticle(Particle.DRIP_WATER);
		ParticleSpawner.spawnParticleSphere(player.getLocation(), particle, 1.5);
		SkillUtils.addPotionEffect(player, PotionEffectType.SLOW, 2, 200);
		SkillUtils.addPotionEffect(player, PotionEffectType.JUMP, 2, 200);
		ManaCalculator.regenerateMana(player, 8);
		
		return true;
	}

}
