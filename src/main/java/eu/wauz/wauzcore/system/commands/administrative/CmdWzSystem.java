package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdWzSystem implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzSystem";
	}
	
	@Override
	public boolean allowConsoleExecution() {
		return true;
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return WauzCore.printSystemAnalytics(sender);
	}

}
