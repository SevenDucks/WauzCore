package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

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
	public boolean executeSkill(final Player player, ItemStack weapon) {
		Location location = player.getLocation();
		SkillUtils.throwEntityIntoAir(player, 0.6);
		player.getWorld().playSound(location, Sound.ENTITY_WITHER_DEATH, 1, 1);
		
		List<Entity> targets = SkillUtils.getTargetsInRadius(location, 6);
		SkillUtils.addPotionEffect(targets, PotionEffectType.SLOW, 5, 200);
		SkillUtils.addPotionEffect(targets, PotionEffectType.JUMP, 5, 200);
		SkillUtils.addPotionEffect(targets, PotionEffectType.GLOWING, 5, 200);
		
		return targets.size() > 0;
	}

}
