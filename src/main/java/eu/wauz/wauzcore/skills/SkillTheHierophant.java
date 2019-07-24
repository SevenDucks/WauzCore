package eu.wauz.wauzcore.skills;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.skills.execution.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

public class SkillTheHierophant implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Hierophant V";

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
		return "Poison Touch";
	}

	@Override
	public int getCooldownSeconds() {
		return 18;
	}

	@Override
	public int getManaCost() {
		return 6;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		final Entity target = SkillUtils.getTargetInLine(player, 5);
		
		if(target != null) {
			int damage = (int) ((double) ItemUtils.getBaseAtk(weapon) * (double) 1.20);
			SkillParticle particle = new SkillParticle(Particle.SLIME);
			SkillUtils.spawnParticleWave(target.getLocation(), particle, 3);
			SkillUtils.callPlayerDamageOverTimeEvent(player, target, Color.LIME, damage, 5, 20);
			return true;
		}
		return false;
	}

}