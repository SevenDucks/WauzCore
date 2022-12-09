package eu.wauz.wauzcore.players.ui;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionStats;
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
	 * 
	 * @param player The player whose action bar should be updated.
	 * 
	 * @see WauzMode
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
			String healthString = ChatColor.RED + "" + stats.getHealth() + " / " + stats.getMaxHealth() + " " + UnicodeUtils.ICON_HEART + SEPERATOR;
			String manaString = ChatColor.LIGHT_PURPLE + "" + stats.getMana() + " / " + stats.getMaxMana() + " " + UnicodeUtils.ICON_STAR + SEPERATOR;
			String rageString = ChatColor.GOLD + "" + stats.getRage() + " / " + stats.getMaxRage() + " " + UnicodeUtils.ICON_SUN + SEPERATOR;
			String heatString = ChatColor.GREEN + "" + ((stats.getHeat()* 5 - 10) + stats.getHeatRandomizer()) + " " + UnicodeUtils.ICON_DEGREES + "C" + SEPERATOR;
			String actionBarMessage = healthString + manaString + rageString + heatString + locationString;
			Components.actionBar(player, actionBarMessage);
			return;
		}
	}

}
