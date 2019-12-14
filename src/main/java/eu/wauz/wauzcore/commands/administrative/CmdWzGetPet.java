package eu.wauz.wauzcore.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.md_5.bungee.api.ChatColor;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Get Pet from String</b></br>
 * - Usage: <b>/wzGetPet [player] [petname]</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdWzGetPet implements WauzCommand {
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "wzGetPet";
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
		if(args.length < 1) {
			return false;
		}
		
		Player player = null;
		String type = null;
		
		if(args.length > 1) {
			player = WauzCore.getOnlinePlayer(args[0]);
			type = args[1];
		}
		else {
			player = (Player) sender;
			type = args[0];
		}
		
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		
		if(type == null || mythicMobs.getMythicMob(type) == null) {
			sender.sendMessage(ChatColor.RED + "Unknown pet type specified!");
			return false;
		}
		
		PetOverviewMenu.addPet(player, null, type);
		return true;
	}

}
