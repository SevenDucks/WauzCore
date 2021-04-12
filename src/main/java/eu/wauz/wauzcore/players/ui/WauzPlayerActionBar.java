package eu.wauz.wauzcore.players.ui;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionSkills;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionStats;
import eu.wauz.wauzcore.skills.Castable;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An UI class to show the player custom stats like health in their action bar.
 * 
 * @author Wauzmons
 */
public class WauzPlayerActionBar {
	
	/**
	 * The string to seperate action bar segments.
	 */
	public static final String SEPERATOR = " " + ChatColor.WHITE + "|" + ChatColor.RESET + " ";
	
	/**
	 * Updates the player's action bar, depending on the gamemode.<br>
	 * Hub: DOUBLE-JUMP-MESSAGE<br>
	 * Survival: LOCATION<br>
	 * MMORPG: HEALTH | MANA | RAGE | HEAT | LOCATION<br>
	 * MMORPG-Casting: ABILITY-DISPLAY
	 * 
	 * @param player The player whose action bar should be updated.
	 * 
	 * @see WauzMode
	 * @see WauzPlayerActionBar#showCastingBar(Player, WauzPlayerData)
	 */
	public static void update(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		WauzPlayerDataSectionStats stats = playerData.getStats();
		
		if(WauzMode.inHub(player)) {
			String actionBarMessage = ChatColor.LIGHT_PURPLE + "Try to Double-Jump!";
			Components.actionBar(player, actionBarMessage);
			return;
		}
		
		Location location = player.getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		String locationString = "" + ChatColor.AQUA + x + " " + y + " " + z;
		
		if(WauzMode.isSurvival(player)) {
			String actionBarMessage = locationString;
			Components.actionBar(player, actionBarMessage);
			return;
		}
		
		if(WauzMode.isMMORPG(player)) {
			if(playerData.getSkills().getActionBar() > 0 && !player.getGameMode().equals(GameMode.CREATIVE)) {
				showCastingBar(player, playerData);
				return;
			}
			String healthString = ChatColor.RED + "" + stats.getHealth() + " / " + stats.getMaxHealth() + " " + UnicodeUtils.ICON_HEART + SEPERATOR;
			String manaString = ChatColor.LIGHT_PURPLE + "" + stats.getMana() + " / " + stats.getMaxMana() + " " + UnicodeUtils.ICON_STAR + SEPERATOR;
			String rageString = ChatColor.GOLD + "" + stats.getRage() + " / " + stats.getMaxRage() + " " + UnicodeUtils.ICON_SUN + SEPERATOR;
			String heatString = ChatColor.GREEN + "" + ((stats.getHeat()* 5 - 10) + stats.getHeatRandomizer()) + " " + UnicodeUtils.ICON_DEGREES + "C" + SEPERATOR;
			String actionBarMessage = healthString + manaString + rageString + heatString + locationString;
			Components.actionBar(player, actionBarMessage);
			return;
		}
	}
	
	/**
	 * Shows the casting bar, filled with quick slots, for the given player.
	 * 
	 * @param player The player viewing the casting bar.
	 * @param playerData The data of the player.
	 * 
	 * @see WauzPlayerDataSectionSkills#getSelectedCastables()
	 * @see WauzPlayerActionBar#getQuickSlot(WauzPlayerData, List, int)
	 */
	private static void showCastingBar(Player player, WauzPlayerData playerData) {
		String actionBarMessage = "";
		List<Castable> selectedCastables = playerData.getSkills().getSelectedCastables();
		int startSlot = playerData.getSkills().getActionBar() == 1 ? 1 : 5;
		for(int slot = startSlot; slot <= startSlot + 3; slot++) {
			actionBarMessage += getQuickSlot(playerData, selectedCastables, slot) + SEPERATOR;
		}
		Components.actionBar(player, actionBarMessage + "(F) " + ChatColor.AQUA + "Next");
	}
	
	/**
	 * Gets the action bar segment for a quick slot.
	 * 
	 * @param playerData The data of the player, viewing the casting bar.
	 * @param selectedCastables The castables in the quick slots.
	 * @param number The number of the quick slot.
	 * 
	 * @return The generated action bar segment.
	 * 
	 * @see Castable#getQuickSlotMessage(WauzPlayerData)
	 */
	private static String getQuickSlot(WauzPlayerData playerData, List<Castable> selectedCastables, int number) {
		Castable castable = selectedCastables.get(number - 1);
		String quickSlot = "(" + (playerData.getSkills().getActionBar() == 2 ? number - 4 : number) +") ";
		if(castable != null) {
			quickSlot += castable.getQuickSlotMessage(playerData);
		}
		else {
			quickSlot += ChatColor.GRAY + "Empty";
		}
		return quickSlot;
	}

}
