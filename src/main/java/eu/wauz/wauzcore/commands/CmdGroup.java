package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.menu.social.GroupMenu;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Open the Group Menu</b></br>
 * - Usage: <b>/group</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdGroup implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("group");
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
		if((WauzMode.isMMORPG(player) && !WauzMode.inHub(player)) || (WauzMode.isSurvival(player)) && !WauzMode.inOneBlock(player)) {
			GroupMenu.open(player);
			return true;
		}
		else {
			player.sendMessage(ChatColor.RED + "You can't open that menu here!");
			return true;
		}
	}

}
