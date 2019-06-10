package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillMechanics;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillParticle;

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
	public boolean executeSkill(final Player player) {
		Location location = null;
		for(Block block : player.getLineOfSight(null, 12)) {
			location = block.getLocation();
			if(!block.getType().equals(Material.AIR)) break;
		}
		
		List<Entity> targets = WauzPlayerSkillMechanics.getTargetsInRadius(location, 5);
		WauzPlayerSkillMechanics.spawnParticleHelix(location, new WauzPlayerSkillParticle(Particle.SMOKE_NORMAL), 1.5, 3.5);
		for(Entity target : targets) {
			WauzPlayerSkillMechanics.spawnParticleLine(target.getLocation(), location, new WauzPlayerSkillParticle(Particle.CRIT_MAGIC), 3);
			WauzPlayerSkillMechanics.throwEntity(target, location);
		}
		return targets.size() > 0;
	}

}
