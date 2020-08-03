package eu.wauz.wauzcore.players.ui;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
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
	 * Updates the player's action bar, depending on the gamemode.</br>
	 * Hub: DOUBLE-JUMP-MESSAGE</br>
	 * Survival: PVP-RES | LOCATION</br>
	 * MMORPG: HEALTH | MANA | RAGE | HEAT-AND-COLD-RES | LOCATION
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
		
		if(WauzMode.inHub(player)) {
			String actionBarMessage = ChatColor.LIGHT_PURPLE + "Try to Double-Jump!";
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
			String actionBarMessage = pvspResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
		
		if(WauzMode.isMMORPG(player)) {
			String healthString = ChatColor.RED + "" + playerData.getHealth() + " / " + playerData.getMaxHealth() + " " + UnicodeUtils.ICON_HEART + seperatorString;
			String manaString = ChatColor.LIGHT_PURPLE + "" + playerData.getMana() + " / " + playerData.getMaxMana() + " " + UnicodeUtils.ICON_STAR + seperatorString;
			String rageString = ChatColor.GOLD + "" + playerData.getRage() + " / " + playerData.getMaxRage() + " " + UnicodeUtils.ICON_SUN + seperatorString;
			String heatString = ChatColor.GREEN + "" + ((playerData.getHeat()* 5 - 10) + playerData.getHeatRandomizer()) + " " + UnicodeUtils.ICON_DEGREES + "C" + seperatorString;
			String heatResString = playerData.getResistanceHeat() != 0 ? ChatColor.GREEN + "HtRes " + (playerData.getResistanceHeat() * 5) + seperatorString : "";
			String coldResString = playerData.getResistanceCold() != 0 ? ChatColor.GREEN + "CdRes " + (playerData.getResistanceCold() * 5) + seperatorString : "";
			String actionBarMessage = healthString + manaString + rageString + heatString + heatResString + coldResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
	}

}
