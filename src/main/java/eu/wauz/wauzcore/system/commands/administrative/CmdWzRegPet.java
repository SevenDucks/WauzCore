package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;

public class CmdWzRegPet implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzRegPet";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		// TODO needs to work without commands in the future.
		return PetOverviewMenu.regPet(WauzCore.getOnlinePlayer(args[0]), args[1]);
	}

}
