package eu.wauz.wauzcore.skills.execution;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.menu.abilities.SkillAssignMenu;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * A class to interact with the quick slot skills of players.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillQuickSlots {
	
	/**
	 * Tries to let a player use their selected quick slot.
	 * When using an empty slot, the skill assign menu is opened instead.
	 * 
	 * @param player The player who tries to use a quick slot.
	 * 
	 * @return If the usage was successful.
	 * 
	 * @see Cooldown#playerQuickSlotUse(Player)
	 * @see WauzPlayerData#getActionBar()
	 * @see WauzPlayerData#getSelectedCastables()
	 * @see Castable#cast(Player)
	 * @see SkillAssignMenu#open(Player)
	 */
	public static boolean tryToUse(Player player) {
		if(!Cooldown.playerQuickSlotUse(player)) {
			return true;
		}
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || playerData.getActionBar() == 0) {
			return false;
		}
		
		int slot = player.getInventory().getHeldItemSlot();
		if(slot < 0 || slot > 3) {
			return false;
		}
		if(playerData.getActionBar() == 2) {
			slot += 4;
		}
		
		List<Castable> selectedCastables = playerData.getSelectedCastables();
		Castable castable = selectedCastables.get(slot);
		if(castable != null) {
			WauzDebugger.log(player, "Use Quick Slot: " + (slot + 1));
			playerData.setActionBar(0);
			castable.cast(player);
		}
		else {
			SkillAssignMenu.open(player);
			playerData.setActionBar(0);
			WauzPlayerActionBar.update(player);
		}
		return true;
	}
	
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
