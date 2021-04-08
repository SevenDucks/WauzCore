package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.players.WauzPlayerSit;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.<br/>
 * - Description: <b>Sit Down</b><br/>
 * - Usage: <b>/sit</b><br/>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Eddshine
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdSit implements WauzCommand {
	
	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("sit");
	}

	/**
	 * Executes the command for given sender with arguments.
	 * 
	 * @param sender The sender of the command.
	 * @param args The arguments of the command.
	 * 
	 * @return If the command had correct syntax.
	 */
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		WauzPlayerSit.sit((Player) sender);
		return true;
	}

}
