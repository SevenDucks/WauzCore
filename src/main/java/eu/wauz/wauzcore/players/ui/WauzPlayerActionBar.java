package eu.wauz.wauzcore.players.ui;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * An UI class to show the player custom stats like health in their action bar.
 * 
 * @author Wauzmons
 */
public class WauzPlayerActionBar {
	
	/**
	 * Updates the player's action bar, depending on the gamemode.</br>
	 * Hub: TIME | DOUBLE-JUMP-MESSAGE</br>
	 * Survival: TIME | PVP-RES | LOCATION</br>
	 * MMORPG: HEALTH | MANA | TIME | HEAT-AND-RES | LOCATION
	 * 
	 * @param player The player whose action bar should be updated.
	 * 
	 * @see WauzMode
	 * @see WauzNmsClient#nmsActionBar(Player, String)
	 */
	public static void update(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		String seperatorString = " " + ChatColor.WHITE + "|" + ChatColor.RESET + " ";
		String timeString = ChatColor.AQUA + UnicodeUtils.ICON_CARET + " " + WauzDateUtils.getServerTime() + seperatorString;
		
		if(WauzMode.inHub(player)) {
			String jumpString = ChatColor.LIGHT_PURPLE + "Try to Double-Jump!";
			String actionBarMessage = timeString + jumpString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
		
		Location location = player.getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		String locationString = "" + ChatColor.BLUE + x + " " + y + " " + z;
		
		if(WauzMode.isSurvival(player)) {
			String pvspResString = playerData.getResistancePvP() != 0 ? ChatColor.GREEN + "NoPvP " + (playerData.getResistancePvP() * 5) + seperatorString : "";
			String actionBarMessage = timeString + pvspResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
		
		if(WauzMode.isMMORPG(player)) {
			String healthString = ChatColor.RED + "" + playerData.getHealth() + " / " + playerData.getMaxHealth() + " " + UnicodeUtils.ICON_HEART + seperatorString;
			String manaString = ChatColor.LIGHT_PURPLE + "" + playerData.getMana() + " / " + playerData.getMaxMana() + " " + UnicodeUtils.ICON_STAR + seperatorString;
			String heatString = ChatColor.GREEN + "" + ((playerData.getHeat()* 5 - 10) + playerData.getHeatRandomizer()) + " " + UnicodeUtils.ICON_DEGREES + "C" + seperatorString;
			String heatResString = playerData.getResistanceHeat() != 0 ? ChatColor.GREEN + "HtRes " + (playerData.getResistanceHeat() * 5) + seperatorString : "";
			String coldResString = playerData.getResistanceCold() != 0 ? ChatColor.GREEN + "CdRes " + (playerData.getResistanceCold() * 5) + seperatorString : "";
			String actionBarMessage = healthString + manaString + timeString + heatString + heatResString + coldResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
	}

}
