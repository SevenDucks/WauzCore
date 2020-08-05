package eu.wauz.wauzcore.players.ui;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.skills.execution.Castable;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
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
	 * Updates the player's action bar, depending on the gamemode.</br>
	 * Hub: DOUBLE-JUMP-MESSAGE</br>
	 * Survival: PVP-RES | LOCATION</br>
	 * MMORPG: HEALTH | MANA | RAGE | HEAT-AND-COLD-RES | LOCATION</br>
	 * MMORPG-Casting: ABILITY-DISPLAY
	 * 
	 * @param player The player whose action bar should be updated.
	 * 
	 * @see WauzMode
	 * @see WauzNmsClient#nmsActionBar(Player, String)
	 * @see WauzPlayerActionBar#showCastingBar(Player, WauzPlayerData)
	 */
	public static void update(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		if(WauzMode.inHub(player)) {
			String actionBarMessage = ChatColor.LIGHT_PURPLE + "Try to Double-Jump!";
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
		
		Location location = player.getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		String locationString = "" + ChatColor.AQUA + x + " " + y + " " + z;
		
		if(WauzMode.isSurvival(player)) {
			String pvspResString = playerData.getResistancePvP() != 0 ? ChatColor.GREEN + "NoPvP " + (playerData.getResistancePvP() * 5) + SEPERATOR : "";
			String actionBarMessage = pvspResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
		
		if(WauzMode.isMMORPG(player)) {
			if(playerData.getActionBar() > 0 && !player.getGameMode().equals(GameMode.CREATIVE)) {
				showCastingBar(player, playerData);
				return;
			}
			String healthString = ChatColor.RED + "" + playerData.getHealth() + " / " + playerData.getMaxHealth() + " " + UnicodeUtils.ICON_HEART + SEPERATOR;
			String manaString = ChatColor.LIGHT_PURPLE + "" + playerData.getMana() + " / " + playerData.getMaxMana() + " " + UnicodeUtils.ICON_STAR + SEPERATOR;
			String rageString = ChatColor.GOLD + "" + playerData.getRage() + " / " + playerData.getMaxRage() + " " + UnicodeUtils.ICON_SUN + SEPERATOR;
			String heatString = ChatColor.GREEN + "" + ((playerData.getHeat()* 5 - 10) + playerData.getHeatRandomizer()) + " " + UnicodeUtils.ICON_DEGREES + "C" + SEPERATOR;
			String heatResString = playerData.getResistanceHeat() != 0 ? ChatColor.GREEN + "HtRes " + (playerData.getResistanceHeat() * 5) + SEPERATOR : "";
			String coldResString = playerData.getResistanceCold() != 0 ? ChatColor.GREEN + "CdRes " + (playerData.getResistanceCold() * 5) + SEPERATOR : "";
			String actionBarMessage = healthString + manaString + rageString + heatString + heatResString + coldResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
	}
	
	/**
	 * Shows the casting bar, filled with quick slots, for the given player.
	 * 
	 * @param player The player viewing the casting bar.
	 * @param playerData The data of the player.
	 * 
	 * @see WauzPlayerData#getSelectedCastables()
	 * @see WauzPlayerActionBar#getQuickSlot(WauzPlayerData, List, int)
	 * @see WauzNmsClient#nmsActionBar(Player, String)
	 */
	private static void showCastingBar(Player player, WauzPlayerData playerData) {
		String actionBarMessage = "";
		List<Castable> selectedCastables = playerData.getSelectedCastables();
		int startSlot = playerData.getActionBar() == 1 ? 1 : 5;
		for(int slot = startSlot; slot <= startSlot + 3; slot++) {
			actionBarMessage += getQuickSlot(playerData, selectedCastables, slot) + SEPERATOR;
		}
		actionBarMessage += "(F) " + ChatColor.AQUA + "Next" + SEPERATOR;
		actionBarMessage += "(9) " + ChatColor.DARK_AQUA + "Assign";
		WauzNmsClient.nmsActionBar(player, actionBarMessage);
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
		String quickSlot = "(" + (playerData.getActionBar() == 2 ? number - 4 : number) +") ";
		if(castable != null) {
			quickSlot += castable.getQuickSlotMessage(playerData);
		}
		else {
			quickSlot += ChatColor.GRAY + "Empty";
		}
		return quickSlot;
	}

}
