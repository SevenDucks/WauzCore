package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import net.md_5.bungee.api.ChatColor;

public class CmdWzEnhanced implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzEnhanced";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
			return true;
		}
		
		if(args.length < 2) {
			return false;
		}
		
		Player player = null;
		String type = null;
		int level = 1;
		
		if(args.length > 2) {
			player = WauzCore.getOnlinePlayer(args[0]);
			type = args[1];
			level = Integer.parseInt(args[2]);
		}
		else {
			player = (Player) sender;
			type = args[0];
			level = Integer.parseInt(args[1]);
		}
		
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}

		return WauzDebugger.getEnhancedEquipment(player, type, level);
	}

}
