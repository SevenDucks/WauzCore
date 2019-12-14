package eu.wauz.wauzcore.commands.execution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

/**
 * This class is used to register, find and execute commands.
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 */
public class WauzCommandExecutor {
	
	/**
	 * A map of all registered commands.
	 */
	private static Map<String, WauzCommand> commandMap = new HashMap<>();
	
	/**
	 * Gets a command for given id from the map.
	 * 
	 * @param commandId The id of the command.
	 * 
	 * @return The command or null, if not found.
	 */
	public static WauzCommand getCommand(String commandId) {
		return commandMap.get(commandId);
	}
	
	/**
	 * Registers a command.
	 * 
	 * @param command The command to register.
	 */
	public static void registerCommand(WauzCommand command) {
		commandMap.put(command.getCommandId().toLowerCase(), command);
	}
	
	/**
	 * Executes the given command, if it is valid
	 * 
	 * @param sender The sender of the command.
	 * @param cmd The name of the command.
	 * @param args The arguments of the command.
	 * 
	 * @return If the command existed and had correct syntax.
	 */
	public static boolean execute(CommandSender sender, Command cmd, String[] args) {
		WauzCommand command = getCommand(cmd.getName().toLowerCase());
		if(command == null) {
			return false;
		}
		
		if(sender instanceof Player) {
			WauzDebugger.log((Player) sender, "Execute Command: " + command.getCommandId() + " " + Arrays.asList(args));
		}
		else if(!command.allowConsoleExecution()) {
			sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
			return true;
		}
		
		try {
			return command.executeCommand(sender, args);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
