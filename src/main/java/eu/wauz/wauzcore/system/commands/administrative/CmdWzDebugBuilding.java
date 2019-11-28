package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import net.md_5.bungee.api.ChatColor;

public class CmdWzDebugBuilding implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzDebug.building";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Player player = args.length == 0 ? (Player) sender : WauzCore.getOnlinePlayer(args[0]);
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		
		return WauzDebugger.toggleBuildingDebugMode(player);
	}

}
