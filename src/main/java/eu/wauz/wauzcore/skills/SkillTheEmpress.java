package eu.wauz.wauzcore.skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.SkillParticle;

public class SkillTheEmpress implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Empress III";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "Melee";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Lightning Chain";
	}

	@Override
	public int getCooldownSeconds() {
		return 20;
	}

	@Override
	public int getManaCost() {
		return 8;
	}

	@Override
	public boolean executeSkill(final Player player) {
		Entity target = SkillUtils.getTargetInLine(player, 5);
		
		if(target != null) {
			lightningChain(player, player, target, 3, new ArrayList<Entity>());
			return true;
		}
		return false;
	}
	
	private void lightningChain(Player player, Entity origin, Entity target, int remainingChains, List<Entity> excludes) {
		Location originLocation = origin.getLocation();
		Location targetLocation = target.getLocation();
		originLocation.setY(originLocation.getY() + 1);
		targetLocation.setY(targetLocation.getY() + 1);
		
		SkillUtils.spawnParticleLine(originLocation, targetLocation, new SkillParticle(Particle.SPELL_WITCH), 3);
		SkillUtils.callPlayerMagicDamageEvent(player, target, 2);
		
		if(remainingChains > 0) {
			excludes.add(target);
			List<Entity> nextTargets = SkillUtils.getTargetsInRadius(target.getLocation(), 5, excludes);
			if(nextTargets.size() > 0)
				lightningChain(player, target, nextTargets.get(0), remainingChains - 1, excludes);
		}
	}

}
