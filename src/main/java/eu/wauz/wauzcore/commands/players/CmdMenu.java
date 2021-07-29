package eu.wauz.wauzcore.commands.players;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.WauzCommand;
import eu.wauz.wauzcore.commands.WauzCommandExecutor;
import eu.wauz.wauzcore.menu.util.MenuRegister;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Open a Menu</b><br>
 * - Usage: <b>/menu [name]</b><br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdMenu implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("menu");
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
		
		Player player = (Player) sender;
		String menuType = args[0].toLowerCase();
		
		if(!WauzMode.inHub(player) && !WauzMode.inOneBlock(player)) {
			WauzInventory menu = MenuRegister.getInventory(menuType);
			if(menu != null && menu.getGamemodes().contains(WauzMode.getMode(player))) {
				menu.openInstance(player);
				return true;
			}
		}
		player.sendMessage(ChatColor.RED + "This menu does not exist or you can't open it here!");
		return true;
	}

}
