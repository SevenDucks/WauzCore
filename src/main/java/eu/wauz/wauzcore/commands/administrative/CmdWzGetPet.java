package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Get Pet from String</b></br>
 * - Usage: <b>/wzGetPet [petname] [player]</b></br>
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
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzGetPet");
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
		
		String type = args[0];
		if(type == null || mythicMobs.getMythicMob(type) == null) {
			sender.sendMessage(ChatColor.RED + "Unknown pet type specified!");
			return false;
		}
		
		Player player = args.length < 2 ? (Player) sender : WauzCore.getOnlinePlayer(args[1]);
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		
		PetOverviewMenu.addPet(player, null, type);
		return true;
	}

}
