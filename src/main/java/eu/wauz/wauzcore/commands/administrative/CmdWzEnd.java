package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A command, that can be executed by a player with fitting permissions.<br/>
 * - Description: <b>End Arcade Game</b><br/>
 * - Usage: <b>/wzEnd</b><br/>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzEnd implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzEnd", "end");
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
		else if(ArcadeLobby.getMinigame() == null) {
			player.sendMessage(ChatColor.RED + "There is no game in progress!");
		}
		else {
			ArcadeLobby.endGame();
		}
		return true;
	}

}
