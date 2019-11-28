package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdWzSkillWeapon implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzSkill.weapon";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return WauzDebugger.getSkillgemWeapon((Player) sender, args[0].replace("_", " "));
	}

}
