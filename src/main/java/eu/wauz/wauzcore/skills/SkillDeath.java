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
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

public class SkillDeath implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "Death XIII";

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
		return "Blood Sacrifice";
	}

	@Override
	public int getCooldownSeconds() {
		return 60;
	}

	@Override
	public int getManaCost() {
		return 5;
	}

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
