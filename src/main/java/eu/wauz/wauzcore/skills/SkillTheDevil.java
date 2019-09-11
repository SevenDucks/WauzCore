package eu.wauz.wauzcore.skills;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

public class SkillTheDevil implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Devil XV";

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
		return "Demonic Grasp";
	}

	@Override
	public int getCooldownSeconds() {
		return 28;
	}

	@Override
	public int getManaCost() {
		return 7;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		Entity target = SkillUtils.getTargetInLine(player, 15);
		
		if(target != null) {
			Location originLocation = player.getLocation().clone().add(0, 1, 0);
			Location targetLocation = target.getLocation().clone().add(0, 1, 0);
			int damage = (int) ((double) ItemUtils.getBaseAtk(weapon) * (double) 1.40);
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
