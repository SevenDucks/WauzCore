package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils.TotemRunnable;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

public class SkillTheTower implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Tower XVI";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "Summon";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Projectile Totem";
	}

	@Override
	public int getCooldownSeconds() {
		return 45;
	}

	@Override
	public int getManaCost() {
		return 6;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		int damage = (int) ((double) ItemUtils.getBaseAtk(weapon) * (double) 1.50);
		
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1.5f);
		SkillUtils.spawnTotem(player, Material.BLAST_FURNACE, new TotemRunnable() {
			
			private SkillParticle particle = new SkillParticle(Color.SILVER);
			
			@Override
			public void run(Entity totem) {
				ParticleSpawner.spawnParticleCircle(totem.getLocation().add(0, 0.5, 0), particle, 1.5, 6);
				List<Entity> targets = SkillUtils.getTargetsInRadius(totem.getLocation(), 10);
				int hitTargets = 0;
				
				for(Entity target : targets) {
					ParticleSpawner.spawnParticleLine(totem.getLocation(), target.getLocation(), particle, 1);
					SkillUtils.callPlayerFixedDamageEvent(player, target, damage);
					if(++hitTargets == 3) {
						return;
					}
				}
			}
			
		}, 5, 40);
		
		return true;
	}

}
