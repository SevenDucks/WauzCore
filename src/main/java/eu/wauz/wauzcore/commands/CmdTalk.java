package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.menu.CitizenInteractionMenu;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizen;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Talk to Nearest Citizen</b></br>
 * - Usage: <b>/talk</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdTalk implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("talk");
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
		List<WauzCitizen> citizens = WauzCitizen.getCitizensNearPlayer(player, 1);
		WauzCitizen nearestCitizen = null;
		int nearestDistance = 8;
		for(WauzCitizen citizen : citizens) {
			double distance = citizen.getLocation().distance(player.getLocation());
			if(distance < nearestDistance) {
				nearestCitizen = citizen;
			}
		}
		
		if(nearestCitizen == null) {
			player.sendMessage(ChatColor.RED + "There are no citizens near you!");
		}
		else {
			CitizenInteractionMenu.open(player, nearestCitizen);
		}
		return true;
	}

}
