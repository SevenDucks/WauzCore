package eu.wauz.wauzcore.system.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.events.WauzPlayerEventHomeChange;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdSethome implements WauzCommand {

	@Override
	public String getCommandId() {
		return "sethome";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return new WauzPlayerEventHomeChange((Player) sender, true).execute((Player) sender);
	}

}
