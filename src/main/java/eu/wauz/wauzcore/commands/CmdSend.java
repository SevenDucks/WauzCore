package eu.wauz.wauzcore.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.players.WauzPlayerMail;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Send a Text Mail to a Player</b></br>
 * - Usage: <b>/send [player] [text]</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdSend implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "send";
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
		if(args.length < 2) {
			return false;
		}
		
		Player player = (Player) sender;
		WauzPlayerMail mail = new WauzPlayerMail();
		if(!mail.tryToSetReceiver(player, args[0])) {
			return true;
		}
		mail.joinAndSetTextContent(args, 1);
		mail.send(player);
		return true;
	}

}
