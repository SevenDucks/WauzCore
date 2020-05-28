package eu.wauz.wauzcore.commands;

import org.bukkit.command.CommandSender;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Send a Trade Request to a Player</b></br>
 * - Usage: <b>/trade [player]</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdTrade implements WauzCommand {
	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
	  return"trade";
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
		if(args.length < 1) {
			return false;
		}
		return false;
	}

}
