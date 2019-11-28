package eu.wauz.wauzcore.system.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdApply implements WauzCommand {

	@Override
	public String getCommandId() {
		return "apply";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return WauzPlayerGuild.applyForGuild((Player) sender, StringUtils.join(args, " "));
	}

}
