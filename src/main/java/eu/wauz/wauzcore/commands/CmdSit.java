package eu.wauz.wauzcore.commands;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.players.WauzPlayerSit;
import net.md_5.bungee.api.ChatColor;
/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Player sit</b></br>
 * - Usage: <b>/sit</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author eddshine
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdSit implements WauzCommand {
	public static ArrayList<String> sittingPlayers = new ArrayList<String>();
	@Override
	public String getCommandId() {
		return "sit";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if(sittingPlayers.contains(player.getName())) {
			player.sendMessage(ChatColor.RED + "You are already sitting.");
		}else if(player.getGameMode() == GameMode.SPECTATOR) {
			player.sendMessage(ChatColor.RED + "Sorry, You can't sit while on Spectator Mode.");
		}else if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
			player.sendMessage(ChatColor.RED + "Sorry, You can't sit while on Mid-air.");
		}else if(player.getLocation().getBlock().getType() == Material.WATER) {
			player.sendMessage(ChatColor.RED + "Sorry, You can't sit while on Water.");
		}else if(player.getLocation().getBlock().getType() == Material.LAVA) {
			player.sendMessage(ChatColor.RED + "Sorry, You can't sit on Lava.");
		}else {
			WauzPlayerSit.sit(player);
			sittingPlayers.add(player.getName());
		}
		return true;
	}

}
