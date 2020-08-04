package eu.wauz.wauzcore.skills.execution;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * A class to interact with the quick slot skills of players.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillQuickSlots {
	
	/**
	 * Gets the display information of a castable from a player's quick slot.
	 * 
	 * @param player The player to get the castable info from.
	 * @param slot The quick slot the castable is placed in.
	 * 
	 * @return The display information of the castable in the quick slot.
	 * 
	 * @see PlayerSkillConfigurator#getQuickSlotSkill(Player, int)
	 * @see Castable#getCastableInfo()
	 */
	public static List<String> getCastableInfo(Player player, int slot) {
		List<String> skillLores = new ArrayList<String>();
		skillLores.add(ChatColor.WHITE + "" + ChatColor.BOLD + "Assigned Ability:");
		
		String castableKey = PlayerSkillConfigurator.getQuickSlotSkill(player, slot);
		Castable castable = WauzPlayerDataPool.getPlayer(player).getCastable(castableKey);
		if(castable != null) {
			skillLores.addAll(castable.getCastableInfo());
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
