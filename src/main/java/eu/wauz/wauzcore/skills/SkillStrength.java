package eu.wauz.wauzcore.skills;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.execution.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

public class SkillStrength implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "Strength VIII";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "Self";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Damage Boost";
	}

	@Override
	public int getCooldownSeconds() {
		return 32;
	}

	@Override
	public int getManaCost() {
		return 7;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		SkillParticle particle = new SkillParticle(Particle.VILLAGER_ANGRY);
		SkillUtils.spawnParticleSphere(player.getLocation(), particle, 1.5);
		SkillUtils.addPotionEffect(player, PotionEffectType.INCREASE_DAMAGE, 15, 0);
		
		return true;
	}

}
