package eu.wauz.wauzcore.players.ui;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerActionBar {
	
	public static void update(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		String timeString = ChatColor.AQUA + ChatFormatter.ICON_CARET + " " + WauzDateUtils.getServerTime() + "   ";
		
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
			String pvspResString = playerData.getResistancePvsP() != 0 ? ChatColor.GREEN + "NoPvP " + (playerData.getResistancePvsP() * 5) + "   " : "";
			String actionBarMessage = timeString + pvspResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
		
		if(WauzMode.isMMORPG(player)) {
			String healthString = ChatColor.RED + "" + playerData.getHealth() + " / " + playerData.getMaxHealth() + " " + ChatFormatter.ICON_HEART + "   ";
			String heatString = ChatColor.GREEN + "" + ((playerData.getHeat()* 5 - 10) + playerData.getHeatRandomizer()) + " " + ChatFormatter.ICON_DEGRS + "C   ";
			String heatResString = playerData.getResistanceHeat() != 0 ? ChatColor.GREEN + "HtRes " + (playerData.getResistanceHeat() * 5) + "   " : "";
			String coldResString = playerData.getResistanceCold() != 0 ? ChatColor.GREEN + "CdRes " + (playerData.getResistanceCold() * 5) + "   " : "";
			String actionBarMessage = healthString + timeString + heatString + heatResString + coldResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
	}

}
