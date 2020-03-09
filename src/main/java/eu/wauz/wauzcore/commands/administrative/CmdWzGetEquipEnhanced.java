package eu.wauz.wauzcore.commands.administrative;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.WauzDebugger;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Get Enhanced Equip from String</b></br>
 * - Usage: <b>/wzGetEquip.enhanced [enname] [enlvl] [player]</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdWzGetEquipEnhanced implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "wzGetEquip.enhanced";
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
		
		String type = args[0];
		int level = Integer.parseInt(args[1]);
		Player player = args.length < 3 ? (Player) sender : WauzCore.getOnlinePlayer(args[2]);
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}

		return WauzDebugger.getEnhancedEquipment(player, type, level);
	}

}
