package eu.wauz.wauzcore.system.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdGrp implements WauzCommand {

	@Override
	public String getCommandId() {
		return "grp";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return ChatFormatter.group((Player) sender, StringUtils.join(args, " "));
	}

}
