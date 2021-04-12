package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.instances.WauzInstanceKeyStatus;

/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Change Dungeon Key Status</b><br>
 * - Usage: <b>/wzKey [world (this)] [keyId] [status (0, 1, 2)]</b><br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzKey implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzKey");
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
		if(args.length < 3) {
			return false;
		}
		
		WauzActiveInstance instance = args[0].equals("this")
				? WauzActiveInstancePool.getInstance((Player) sender)
				: WauzActiveInstancePool.getInstance(args[0]);
		if(instance == null) {
			sender.sendMessage(ChatColor.RED + "Unknown world specified!");
			return false;
		}
		
		String keyId = args[1];
		WauzInstanceKeyStatus keyStatus;
		
		switch (args[2]) {
		case "1":
			keyStatus = WauzInstanceKeyStatus.OBTAINED;
			break;
		case "2":
			keyStatus = WauzInstanceKeyStatus.USED;
			break;
		default:
			keyStatus = WauzInstanceKeyStatus.UNOBTAINED;
			break;
		}
		
		instance.setKeyStatus(keyId, keyStatus);
		for(Player player : instance.getWorld().getPlayers()) {
			WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
			player.sendMessage(ChatColor.GREEN + "You obtained the Key \"" + ChatColor.DARK_AQUA + keyId + ChatColor.GREEN + "\"!");
		}
		return true;
	}

}
