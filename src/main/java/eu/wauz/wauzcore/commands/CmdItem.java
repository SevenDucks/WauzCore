package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.events.ShareItemEvent;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Send Item in Chat</b></br>
 * - Usage: <b>/item</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdItem implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("item");
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
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(!ItemUtils.isNotAir(itemStack)) {
			player.sendMessage(ChatColor.RED + "Invalid item selected (in hand)!");
			return true;
		}
		ChatFormatter.share(player, itemStack);
		ShareItemEvent event = new ShareItemEvent(player, itemStack);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return true;
	}

}
