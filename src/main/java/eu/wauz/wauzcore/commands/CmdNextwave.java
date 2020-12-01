package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.instances.WauzInstanceType;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Start Next 5 Arena Waves</b></br>
 * - Usage: <b>/nextwave</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdNextwave implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("nextwave");
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
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(player);
		if(instance == null || !instance.getType().equals(WauzInstanceType.ARENA)) {
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
		}
		else {
			instance.getMobArena().tryToManuallyStartNewWave(player);
		}
		return true;
	}

}
