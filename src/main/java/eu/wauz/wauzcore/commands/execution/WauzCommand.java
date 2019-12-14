package eu.wauz.wauzcore.commands.execution;

import org.bukkit.command.CommandSender;

/**
 * A command, that can be executed by a player with fitting permissions.
 * 
 * @author Wauzmons
 * 
 * @see WauzCommandExecutor
 */
public interface WauzCommand {
	
	/**
	 * @return The id of the command.
	 */
	public String getCommandId();
	
	/**
	 * @return If the command can be executed from the console. Default is false.
	 */
	public default boolean allowConsoleExecution() {
		return false;
	}
	
	/**
	 * Executes the command for given sender with arguments.
	 * 
	 * @param sender The sender of the command.
	 * @param args The arguments of the command.
	 * 
	 * @return If the command had correct syntax.
	 */
	public boolean executeCommand(CommandSender sender, String[] args);

}
