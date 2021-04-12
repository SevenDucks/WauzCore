package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Change Rank of a Player</b><br>
 * - Usage: <b>/wzRank [rank] [player]</b><br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzRank implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzRank");
	}
	
	/**
	 * @return If the command can be executed from the console. Default is false.
	 */
	@Override
	public boolean allowConsoleExecution() {
		return true;
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
		
		String rankName = args[0];
		if(!WauzRank.isValidRank(rankName)) {
			sender.sendMessage(ChatColor.RED + "Unknown rank specified!");
			return false;
		}
		
		OfflinePlayer player;
		try{
		    UUID uuid = UUID.fromString(args[1]);
		    player = Bukkit.getOfflinePlayer(uuid);
		}
		catch (IllegalArgumentException exception){
			player = WauzCore.getOfflinePlayer(args[1]);
		}
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		
		int senderLevel = sender instanceof Player ? WauzRank.getRank((Player) sender).getRankPermissionLevel() : Integer.MAX_VALUE;
		int oldRankLevel = WauzRank.getRank(player).getRankPermissionLevel();
		WauzRank newRank = WauzRank.getRank(rankName);
		if(senderLevel <= oldRankLevel || senderLevel <= newRank.getRankPermissionLevel()) {
			sender.sendMessage(ChatColor.RED + "Insufficient permissions to change that rank!");
			return true;
		}
		
		PlayerConfigurator.setRank(player, newRank);
		sender.sendMessage(ChatColor.GREEN + "Changed the rank of " + player.getName() + " to " + newRank.getRankName() + "!");
		Player onlinePlayer = player.getPlayer();
		if(onlinePlayer != null) {
			Components.kick(onlinePlayer, "Your rank was updated! Please re-join the game!");
		}
		return true;
	}

}
