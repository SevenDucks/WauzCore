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
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null)
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
			String pvspResString = pd.getResistancePvsP() != 0 ? ChatColor.GREEN + "NoPvP " + (pd.getResistancePvsP() * 5) + "   " : "";
			String actionBarMessage = timeString + pvspResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
		
		if(WauzMode.isMMORPG(player)) {
			String healthString = ChatColor.RED + "" + pd.getHealth() + " / " + pd.getMaxHealth() + " " + ChatFormatter.ICON_HEART + "   ";
			String heatString = ChatColor.GREEN + "" + ((pd.getHeat()* 5 - 10) + pd.getHeatRandomizer()) + " " + ChatFormatter.ICON_DEGRS + "C   ";
			String heatResString = pd.getResistanceHeat() != 0 ? ChatColor.GREEN + "HtRes " + (pd.getResistanceHeat() * 5) + "   " : "";
			String coldResString = pd.getResistanceCold() != 0 ? ChatColor.GREEN + "CdRes " + (pd.getResistanceCold() * 5) + "   " : "";
			String actionBarMessage = healthString + timeString + heatString + heatResString + coldResString + locationString;
			WauzNmsClient.nmsActionBar(player, actionBarMessage);
			return;
		}
	}

}
