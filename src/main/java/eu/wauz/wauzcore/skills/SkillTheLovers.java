package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

/**
 * A skill, that can be executed by a player.
 * "Binding Vacuum" pulls all enemies in a radius of 5 blocks to the targeted location.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillTheLovers implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Lovers VI";

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
		return "Binding Vacuum";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "BndVac";
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
		return 4;
	}
	
	/**
	 * @return If rage should be used for this skill instead of mana.
	 */
	@Override
	public boolean isPhysical() {
		return false;
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
		Location location = null;
		for(Block block : player.getLineOfSight(null, 12)) {
			location = block.getLocation();
			if(!block.getType().equals(Material.AIR)) break;
		}
		
		List<Entity> targets = SkillUtils.getTargetsInRadius(location, 5);
		location.getWorld().playSound(location, Sound.ENTITY_GHAST_WARN, 1, 0.5f);
		ParticleSpawner.spawnParticleHelix(location, new SkillParticle(Particle.SMOKE_NORMAL), 1.5, 3.5);
		for(Entity target : targets) {
			ParticleSpawner.spawnParticleLine(target.getLocation(), location, new SkillParticle(Particle.CRIT_MAGIC), 3);
			SkillUtils.throwEntity(target, location);
		}
		return targets.size() > 0;
	}

}
