package eu.wauz.wauzcore.skills;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

public class SkillTheMoon implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Moon XVIII";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "Ranged";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Lunaris Beam";
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
		Entity target = SkillUtils.getTargetInLine(player, 15);
		
		if(target != null) {
			Location originLocation = player.getLocation().clone().add(0, 1, 0);
			Location targetLocation = target.getLocation().clone().add(0, 1, 0);
			player.getWorld().playSound(originLocation, Sound.BLOCK_GLASS_BREAK, 1, 0.5f);
			SkillParticle particle = new SkillParticle(Particle.DRIP_WATER);
			ParticleSpawner.spawnParticleLine(originLocation, targetLocation, particle, 1, 0.25);
			ParticleSpawner.spawnParticleWave(target.getLocation(), particle, 2);
			SkillUtils.callPlayerMagicDamageEvent(player, target, 2);
			SkillUtils.addPotionEffect(target, PotionEffectType.SLOW, 3, 200);
			SkillUtils.addPotionEffect(target, PotionEffectType.JUMP, 3, 200);
			return true;
		}
		return false;
	}

}
