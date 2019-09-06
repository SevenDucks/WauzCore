package eu.wauz.wauzcore.skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

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
	public boolean executeSkill(final Player player, ItemStack weapon) {
		Entity target = SkillUtils.getTargetInLine(player, 3);
		
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
		
		player.getWorld().playSound(originLocation, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, (float) (0.8 + (remainingChains * 0.4)));
		ParticleSpawner.spawnParticleLine(originLocation, targetLocation, new SkillParticle(Particle.SPELL_WITCH), 3);
		SkillUtils.callPlayerMagicDamageEvent(player, target, 2.5);
		
		if(remainingChains > 0) {
			excludes.add(target);
			List<Entity> nextTargets = SkillUtils.getTargetsInRadius(target.getLocation(), 7, excludes);
			if(nextTargets.size() > 0)
				lightningChain(player, target, nextTargets.get(0), remainingChains - 1, excludes);
		}
	}

}
