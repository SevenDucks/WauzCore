package eu.wauz.wauzcore.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.players.WauzPlayerFriends;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Send a Friend Request to a Player</b></br>
 * - Usage: <b>/friend [player]</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdFriend implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "friend";
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
		
		WauzPlayerFriends.addFriend((Player) sender, args[0]);
		return true;
	}

}
