package eu.wauz.wauzcore.commands.administrative;

import org.bukkit.command.CommandSender;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Print System Analytics</b></br>
 * - Usage: <b>/wzSystem</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdWzSystem implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "wzSystem";
	}
	
	/**
	 * @return If the command can be executed from the console. Default is false.
	 */
	@Override
	public boolean allowConsoleExecution() {
		return true;
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
		return WauzCore.printSystemAnalytics(sender);
	}

}
