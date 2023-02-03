package eu.wauz.wauzcore.commands.builders;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.building.WauzCommandChain;
import eu.wauz.wauzcore.commands.WauzCommand;
import eu.wauz.wauzcore.commands.WauzCommandExecutor;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Execute Command Chain</b><br>
 * - Usage: <b>/weCommandChain [chainname]</b><br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWeCommandChain implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("weCommandChain", "chain");
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
		
		WauzCommandChain commandChain = WauzCommandChain.getCommandChain(args[0]);
		if(commandChain == null) {
			return false;
		}
		
		Player player = (Player) sender;
		player.sendMessage(ChatColor.GREEN + "Starting command chain, do not move!");
		scheduleNextCommand(player, commandChain.getCommands(), commandChain.getTimeout(), true);
		return true;
	}
	
	/**
	 * Schedules the next command in a command chain.
	 * 
	 * @param player The player who is executing the commands.
	 * @param commands The list of remaining commands to be exectued.
	 * @param timeout The timeout between commands.
	 * @param instant If the next command should be executed instantly.
	 */
	private void scheduleNextCommand(Player player, List<String> commands, int timeout, boolean instant) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        @Override
			public void run() {
	        	try {
	        		if(player != null && player.isValid()) {
	        			if(commands.isEmpty()) {
	        				player.sendMessage(ChatColor.GREEN + "Command chain finished!");
	        				return;
	        			}
	        			String command = commands.get(0);
	        			player.performCommand(command);
	        			commands.remove(0);
	        			scheduleNextCommand(player, commands, timeout, false);
	        		}
	        	}
	        	catch (NullPointerException e) {
	        		WauzDebugger.catchException(getClass(), e);
	        	}
	        }
	        
		}, instant ? 0 : timeout * 20);
	}

}
