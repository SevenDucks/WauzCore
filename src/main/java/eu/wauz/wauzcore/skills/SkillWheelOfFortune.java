package eu.wauz.wauzcore.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;

public class SkillWheelOfFortune implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "Wheel Of Fortune X";

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
		return "Random";
	}

	@Override
	public int getCooldownSeconds() {
		return 20;
	}

	@Override
	public int getManaCost() {
		return 5;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		Map<String, WauzPlayerSkill> playerSkillMap = WauzPlayerSkillExecutor.playerSkillMap;
		List<WauzPlayerSkill> playerSkills = new ArrayList<>(playerSkillMap.values());
		
		return playerSkills.get(new Random().nextInt(playerSkills.size())).executeSkill(player, weapon);
	}

}
