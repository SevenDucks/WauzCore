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
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

public class SkillTheLovers implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Lovers VI";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "AoE";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Binding Vacuum";
	}

	@Override
	public int getCooldownSeconds() {
		return 12;
	}

	@Override
	public int getManaCost() {
		return 4;
	}

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
