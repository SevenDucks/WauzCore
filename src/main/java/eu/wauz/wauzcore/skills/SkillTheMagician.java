package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

public class SkillTheMagician implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Magician I";

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
		return "Explosion Circle";
	}

	@Override
	public int getCooldownSeconds() {
		return 15;
	}

	@Override
	public int getManaCost() {
		return 5;
	}

	@Override
	public boolean executeSkill(final Player player) {
		Location location = null;
		for(Block block : player.getLineOfSight(null, 12)) {
			location = block.getLocation();
			if(!block.getType().equals(Material.AIR)) break;
		}
		
		List<Entity> targets = SkillUtils.getTargetsInRadius(location, 4);
		SkillUtils.createExplosion(location, 8);
		SkillUtils.callPlayerMagicDamageEvent(player, targets, 1.5);
		
		return targets.size() > 0;
	}

}
