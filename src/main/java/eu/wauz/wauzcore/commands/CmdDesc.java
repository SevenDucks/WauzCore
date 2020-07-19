package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Set the Group Description</b></br>
 * - Usage: <b>/desc [text]</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdDesc implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("desc");
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
		String message = StringUtils.join(args, " ");
		
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
		if(playerGroup == null) {
			player.sendMessage(ChatColor.RED + "You are not in a group!");
			return true;
		}
		if(!playerGroup.isGroupAdmin(player)) {
			player.sendMessage(ChatColor.RED + "You are not the group-leader!");
			return true;
		}
		if(StringUtils.isBlank(message)) {
			player.sendMessage(ChatColor.RED + "Please specify the text to set!");
			return false;
		}
		else {
			playerGroup.setGroupDescription(player, message);
			return true;
		}
	}

}
