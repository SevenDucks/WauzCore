package eu.wauz.wauzcore.commands.administrative;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.items.WauzRewards;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Gain Experience Points</b></br>
 * - Usage: <b>/wzExp [player] [tier] [amount]</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdWzExp implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "wzExp";
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
		if(args.length < 3) {
			return false;
		}
		
		if(args.length < 4) {
			WauzRewards.level(WauzCore.getOnlinePlayer(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]));
		}
		else {
			String[] splitLocation = args[3].split(";");
			Double x = Double.parseDouble(splitLocation[1]);
			Double y = Double.parseDouble(splitLocation[2]);
			Double z = Double.parseDouble(splitLocation[3]);
			Location location = new Location(Bukkit.getWorld(splitLocation[0]), x, y, z);
			WauzRewards.level(WauzCore.getOnlinePlayer(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]), location);
		}
		return true;
	}

}
