package eu.wauz.wauzcore.commands.administrative;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Change Dungeon Key Status</b></br>
 * - Usage: <b>/wzKey [world (this)] [keyId] [status (0, 1, 2)]</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdWzKey implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "wzKey";
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
		if(args.length != 3) {
			return false;
		}
		
		World world = args[0].equals("this") ? ((Player) sender).getWorld() : Bukkit.getWorld(args[0]);
		if(world == null) {
			sender.sendMessage(ChatColor.RED + "Unknown world specified!");
			return false;
		}
		
		String keyId = args[1];
		String keyStatus;
		
		switch (args[2]) {
		case "1":
			keyStatus = InstanceConfigurator.KEY_STATUS_OBTAINED;
			break;
		case "2":
			keyStatus = InstanceConfigurator.KEY_STATUS_USED;
			break;
		default:
			keyStatus = InstanceConfigurator.KEY_STATUS_UNOBTAINED;
			break;
		}
		
		InstanceConfigurator.setInstanceWorldKeyStatus(world, keyId, keyStatus);
		for(Player player : world.getPlayers()) {
			WauzPlayerScoreboard.scheduleScoreboard(player);
			player.sendMessage(ChatColor.GREEN + "You obtained the Key \"" + ChatColor.DARK_AQUA + keyId + ChatColor.GREEN + "\"!");
		}
		return true;
	}

}
