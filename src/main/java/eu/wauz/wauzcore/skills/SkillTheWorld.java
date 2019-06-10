package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillMechanics;

public class SkillTheWorld implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The World XXI";

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
		return "Time Freeze";
	}

	@Override
	public int getCooldownSeconds() {
		return 12;
	}

	@Override
	public int getManaCost() {
		return 6;
	}

	@Override
	public boolean executeSkill(final Player player) {
		Location location = player.getLocation();
		WauzPlayerSkillMechanics.throwEntityIntoAir(player, 0.6);
		
		List<Entity> targets = WauzPlayerSkillMechanics.getTargetsInRadius(location, 6);
		WauzPlayerSkillMechanics.addPotionEffect(targets, PotionEffectType.SLOW, 5, 200);
		WauzPlayerSkillMechanics.addPotionEffect(targets, PotionEffectType.JUMP, 5, 200);
		WauzPlayerSkillMechanics.addPotionEffect(targets, PotionEffectType.GLOWING, 5, 200);
		
		return targets.size() > 0;
	}

}
