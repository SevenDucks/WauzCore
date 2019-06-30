package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

public class SkillJudgement implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "Judgement XX";

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
		return "Shockwave";
	}

	@Override
	public int getCooldownSeconds() {
		return 25;
	}

	@Override
	public int getManaCost() {
		return 6;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		Location location = player.getLocation();
		List<Entity> targets = SkillUtils.getTargetsInRadius(location, 8);
		
		SkillParticle particle = new SkillParticle(Color.TEAL);
		SkillUtils.spawnParticleWave(location, particle, 12);
		SkillUtils.callPlayerMagicDamageEvent(player, targets, 1);
		SkillUtils.throwEntitiesIntoAir(targets, 1.4);
		
		return targets.size() > 0;
	}

}
