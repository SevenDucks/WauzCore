package eu.wauz.wauzcore.skills.execution;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;

/**
 * A class to interact with the quick slot skills of players.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillQuickSlots {
	
	/**
	 * Gets the display information of a skill from a player's quick slot.
	 * 
	 * @param player The player to get the skill info from.
	 * @param slot The quick slot the skill is placed in.
	 * 
	 * @return The display information of the skill in the quick slot.
	 */
	public static List<String> getSkillInfo(Player player, int slot) {
		List<String> skillLores = new ArrayList<String>();
		skillLores.add(ChatColor.WHITE + "" + ChatColor.BOLD + "Assigned Ability:");
		
		String skillName = PlayerSkillConfigurator.getQuickSlotSkill(player, slot);
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillName);
		if(skill != null) {
			skillLores.addAll(getSkillInfo(skill));
		}
		else {
			skillLores.add(ChatColor.GRAY + "None");
		}
		return skillLores;
	}
	
	/**
	 * Gets the display information of the given skill.
	 * 
	 * @param skill The skill to get the info from.
	 * 
	 * @return The display information of the skill.
	 */
	public static List<String> getSkillInfo(WauzPlayerSkill skill) {
		List<String> skillLores = new ArrayList<String>();
		skillLores.add(ChatColor.WHITE + "Skill (" + ChatColor.RED + skill.getSkillId() + ChatColor.WHITE + ")");
		skillLores.add(ChatColor.WHITE + skill.getSkillDescription());
		skillLores.add(ChatColor.WHITE + skill.getSkillStats());
		return skillLores;
	}

}
