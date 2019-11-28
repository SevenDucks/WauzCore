package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdWzExp implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzExp";
	}
	
	@Override
	public boolean allowConsoleExecution() {
		return true;
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length < 3) {
			return false;
		}
		
		if(args.length < 4) {
			WauzRewards.level(WauzCore.getOnlinePlayer(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]));
		}
		else {
			String[] splitLocation = args[3].split(";");
			Double x = Double.parseDouble(splitLocation[1]);
			Double y = Double.parseDouble(splitLocation[2]);
			Double z = Double.parseDouble(splitLocation[3]);
			Location location = new Location(Bukkit.getWorld(splitLocation[0]), x, y, z);
			WauzRewards.level(WauzCore.getOnlinePlayer(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]), location);
		}
		return true;
	}

}
