package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.WauzTeleporter;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Event-Travel to Location per UUID</b></br>
 * - Usage: <b>/wzTravelEvent [uuid]</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdWzTravelEvent implements WauzCommand {

	/**
	 * A mao with event specific travel locations.
	 */
	private static Map<String, Location> eventTravelMap = new HashMap<String, Location>();
	
	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzTravelEvent");
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
		
		Player player = (Player) sender;
		WauzTeleporter.eventTeleport(player, eventTravelMap.get(args[0]));
		return true;
	}
	
	/**
	 * @return A mao with event specific travel locations.
	 */
	public static Map<String, Location> getEventTravelMap() {
		return eventTravelMap;
	}

}
