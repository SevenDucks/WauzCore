package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdWzSkill implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzSkill";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return WauzPlayerSkillExecutor.execute((Player) sender, null, args[0].replace("_", " "));
	}

}
