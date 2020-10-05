package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Start Specific Arcade Game</b></br>
 * - Usage: <b>/wzStart [gamename]</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdWzStart implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzStart");
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
		Player player = (Player) sender;
		if(!WauzMode.isArcade(player)) {
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
		}
		else if(ArcadeLobby.getMinigame() != null) {
			player.sendMessage(ChatColor.RED + "There already is a game in progress!");
		}
		else if(ArcadeLobby.getWaitingCount() < 2) {
			player.sendMessage(ChatColor.RED + "There are not enough players for a game!");
		}
		else if(args.length < 1 || !ArcadeLobby.startGame(args[0])) {
			return false;
		}
		return true;
	}

}
