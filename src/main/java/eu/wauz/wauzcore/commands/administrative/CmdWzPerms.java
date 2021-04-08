package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.<br/>
 * - Description: <b>Show Permissions</b><br/>
 * - Usage: <b>/wzPerms [filter]</b><br/>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzPerms implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzPerms");
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
		String filter = args.length == 0 ? "" : args[0];
		List<PermissionAttachmentInfo> infos = player.getEffectivePermissions().stream()
			.filter(info -> info.getPermission().contains(filter))
			.sorted((info1, info2) -> info1.getPermission().compareTo(info2.getPermission()))
			.collect(Collectors.toList());
		for(PermissionAttachmentInfo info : infos) {
			ChatColor color = info.getValue() ? ChatColor.GREEN : ChatColor.RED;
			player.sendMessage(ChatColor.YELLOW + info.getPermission() + " " + color + info.getValue());
		}
		return true;
	}

}
