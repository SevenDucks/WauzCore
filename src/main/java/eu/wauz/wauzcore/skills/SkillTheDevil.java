package eu.wauz.wauzcore.skills;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A skill, that can be executed by a player.
 * "Demonic Grasp" levitates an enemy within 15 blocks into the air
 * and causes 700% damage over the course of 5 seconds.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillTheDevil implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Devil XV";

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
		return WauzPlayerSkillType.RANGED;
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Demonic Grasp";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 28;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 7;
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
		Entity target = SkillUtils.getTargetInLine(player, 15);
		
		if(target != null) {
			Location originLocation = player.getLocation().clone().add(0, 1, 0);
			Location targetLocation = target.getLocation().clone().add(0, 1, 0);
			int damage = (int) ((double) EquipmentUtils.getBaseAtk(weapon) * (double) 1.40);
			player.getWorld().playSound(originLocation, Sound.ENTITY_RAVAGER_ROAR, 1, 0.5f);
			SkillParticle particle = new SkillParticle(Particle.CRIT);
			ParticleSpawner.spawnParticleLine(originLocation, targetLocation, particle, 1, 0.25);
			SkillUtils.callPlayerDamageOverTimeEvent(player, target, Color.GRAY, damage, 5, 20);
			SkillUtils.addPotionEffect(target, PotionEffectType.LEVITATION, 5, 0);
			return true;
		}
		return false;
	}

}
