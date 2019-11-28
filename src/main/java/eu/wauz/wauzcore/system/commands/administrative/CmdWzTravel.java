package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdWzTravel implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzTravel";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length < 3) {
			return false;
		}
		
		Player player = (Player) sender;
		WauzSigns.startTravelling(player, args[0],  args[1], args[2]);
		return true;
	}

}
