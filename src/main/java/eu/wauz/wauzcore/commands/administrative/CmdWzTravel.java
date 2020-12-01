package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Travel to Location</b></br>
 * - Usage: <b>/wzTravel [waypoint]</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzTravel implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzTravel", "warp");
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
		WauzTeleporter.waypointTeleport(player, args[0]);
		return true;
	}

}
