package eu.wauz.wauzcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.players.WauzPlayerMail;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Send your Hand Item to a Player</b></br>
 * - Usage: <b>/send.item [player] [text]</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdSendItem implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "send.item";
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
		if(!WauzPlayerMail.canSendMail(player)) {
			player.sendMessage(ChatColor.RED + "You already reached the limit of mails sent today!");
			return true;
		}
		WauzPlayerMail mail = new WauzPlayerMail();
		if(!mail.tryToSetReceiver(player, args[0]) || !mail.tryToSetItemStack(player)) {
			return true;
		}
		mail.joinAndSetTextContent(args, 1);
		mail.send(player);
		return true;
	}

}
