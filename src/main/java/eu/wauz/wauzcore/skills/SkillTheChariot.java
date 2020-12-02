package eu.wauz.wauzcore.skills;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Skillgem;

/**
 * A skill, that can be executed by a player.
 * "Knockback Charge" can boost you up to 10 blocks forward
 * and knocks back all enemies from the origin and target spots.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
@Skillgem
public class SkillTheChariot implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Chariot VII";

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
		return "Knockback Charge";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "Charge";
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
		return 8;
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
		Location origin = player.getLocation();
		Location target = origin.clone().add(origin.getDirection().multiply(10).setY(0));
		
		player.getWorld().playSound(origin, Sound.ENTITY_BLAZE_SHOOT, 1, 0.5f);
		player.getWorld().playSound(origin, Sound.ENTITY_HORSE_ANGRY, 1, 1.5f);
		SkillParticle particle = new SkillParticle(Particle.CLOUD);
		ParticleSpawner.spawnParticleLine(origin, target, particle, 3);
		SkillUtils.throwEntity(player, target);

		Set<Entity> targets = new HashSet<>();
		targets.addAll(SkillUtils.getTargetsInRadius(origin, 2.5));
		targets.addAll(SkillUtils.getTargetsInRadius(target, 2.5));
		for(Entity entity : targets) {
			SkillUtils.throwBackEntity(entity, origin, 1.6);
		}
		return targets.size() > 0;
	}

}
