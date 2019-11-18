package eu.wauz.wauzcore.system.commands.execution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzDebugger;

public class WauzCommandExecutor {
	
	private static Map<String, WauzCommand> commandMap = new HashMap<>();
	
	public static WauzCommand getCommand(String commandId) {
		return commandMap.get(commandId);
	}
	
	public static void registerCommand(WauzCommand command) {
		commandMap.put(command.getCommandId().toLowerCase(), command);
	}
	
	public static boolean execute(CommandSender sender, Command cmd, String[] args) {
		WauzCommand command = getCommand(cmd.getName().toLowerCase());
		if(command == null) {
			return false;
		}
		if(sender instanceof Player) {
			WauzDebugger.log((Player) sender, "Execute Command: " + command.getCommandId() + " " + Arrays.asList(args));
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
