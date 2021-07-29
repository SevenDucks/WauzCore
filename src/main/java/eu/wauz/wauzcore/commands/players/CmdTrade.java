package eu.wauz.wauzcore.commands.players;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.WauzCommand;
import eu.wauz.wauzcore.commands.WauzCommandExecutor;
import eu.wauz.wauzcore.players.WauzPlayerTrade;
import eu.wauz.wauzcore.system.annotations.Command;
/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Send a Trade Request to a Player</b><br>
 * - Usage: <b>/trade [player]</b><br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Eddshine
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdTrade implements WauzCommand {
	
	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
	  return Arrays.asList("trade");
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
		
		WauzPlayerTrade.tryToTrade((Player) sender, args[0]);
		return true;
	}

}
