package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A command, that can be executed by a player with fitting permissions.<br/>
 * - Description: <b>Toggle Creative Mode</b><br/>
 * - Usage: <b>/wzGamemode [player]</b><br/>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzGamemode implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzGamemode", "gm");
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
		Player player = args.length == 0 ? (Player) sender : WauzCore.getOnlinePlayer(args[0]);
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		
		if(!player.getGameMode().equals(GameMode.CREATIVE)) {
			player.setGameMode(GameMode.CREATIVE);
		}
		else {
			GameMode mode = WauzMode.isSurvival(player) ? GameMode.SURVIVAL : GameMode.ADVENTURE;
			player.setGameMode(mode);
		}
		return true;
	}

}
