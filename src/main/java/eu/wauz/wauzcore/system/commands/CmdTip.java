package eu.wauz.wauzcore.system.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.ui.WauzPlayerNotifier;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdTip implements WauzCommand {

	@Override
	public String getCommandId() {
		return "tip";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return WauzPlayerNotifier.execute((Player) sender);
	}

}
