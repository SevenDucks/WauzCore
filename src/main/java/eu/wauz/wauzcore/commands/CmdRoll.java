package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * A command, that can be executed by a player with fitting permissions.<br/>
 * - Description: <b>Roll a Random Number</b><br/>
 * - Usage: <b>/roll [maximum]</b><br/>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdRoll implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("roll");
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
		
		int maximum = 100;
		if(args.length > 0) {
			try {
				maximum = Integer.parseInt(args[0]);
				maximum = maximum > 0 ? maximum : 100;
			}
			catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Invalid maximum specified!");
				return true;
			}
		}
		
		int value = Chance.minMax(1, maximum);
		Bukkit.broadcastMessage(player.getName() + " rolls " + value + " (1-" + maximum + ")");
		return true;
	}

}
