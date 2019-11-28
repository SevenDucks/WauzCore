package eu.wauz.wauzcore.system.commands.execution;

import org.bukkit.command.CommandSender;

public interface WauzCommand {
	
	public String getCommandId();
	
	public default boolean allowConsoleExecution() {
		return false;
	}
	
	public boolean executeCommand(CommandSender sender, String[] args);

}
