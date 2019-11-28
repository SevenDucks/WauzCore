package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdWzLeave implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzLeave";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return WauzTeleporter.exitInstanceTeleportSystem((Player) sender);
	}

}
