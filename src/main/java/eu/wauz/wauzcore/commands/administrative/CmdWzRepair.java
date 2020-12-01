package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Repair Equipment of Player</b></br>
 * - Usage: <b>/wzRepair [player]</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzRepair implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzRepair", "repair");
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
		
		tryToRepair(player, player.getEquipment().getItemInMainHand());
		tryToRepair(player, player.getEquipment().getItemInOffHand());
		tryToRepair(player, player.getEquipment().getHelmet());
		tryToRepair(player, player.getEquipment().getChestplate());
		tryToRepair(player, player.getEquipment().getLeggings());
		tryToRepair(player, player.getEquipment().getBoots());
		return true;
	}
	
	/**
	 * Tries to repair the given item.
	 * 
	 * @param player The player, that owns the item.
	 * @param itemToRepair The item to repair.
	 */
	private void tryToRepair(Player player, ItemStack itemToRepair) {
		if(itemToRepair != null) {
			DurabilityCalculator.repairItem(player, itemToRepair);
		}
	}

}
