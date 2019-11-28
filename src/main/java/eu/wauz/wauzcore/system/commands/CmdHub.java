package eu.wauz.wauzcore.system.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdHub implements WauzCommand {

	@Override
	public String getCommandId() {
		return "hub";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return WauzTeleporter.hubTeleportManual((Player) sender);
	}

}
