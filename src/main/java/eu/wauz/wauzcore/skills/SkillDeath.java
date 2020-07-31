package eu.wauz.wauzcore.skills;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A skill, that can be executed by a player.
 * "Blood Sacrifice" deals 1500% damge to a single target, but decreases your own HP to 1 Point.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillDeath implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "Death XIII";

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
		return WauzPlayerSkillType.MELEE;
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Blood Sacrifice";
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
		Entity target = SkillUtils.getTargetInLine(player, 3);
		
		if(target != null) {
			DamageCalculator.setHealth(player, 1);
			SkillUtils.callPlayerMagicDamageEvent(player, target, 15);
			
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 0.5f);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 2);
			SkillParticle flashParticle = new SkillParticle(Particle.FLASH);
			flashParticle.spawn(player.getLocation(), 1);
			flashParticle.spawn(target.getLocation(), 1);
			
			SkillParticle bloodParticle = new SkillParticle(Color.RED);
			ParticleSpawner.spawnParticleCircle(player.getLocation().clone().add(0, 0.5, 0), bloodParticle, 1.5, 6);
			ParticleSpawner.spawnParticleCircle(target.getLocation().clone().add(0, 0.5, 0), bloodParticle, 1.5, 6);
			return true;
		}
		return false;
	}

}
