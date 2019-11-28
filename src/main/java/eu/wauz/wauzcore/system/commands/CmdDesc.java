package eu.wauz.wauzcore.system.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import net.md_5.bungee.api.ChatColor;

public class CmdDesc implements WauzCommand {

	@Override
	public String getCommandId() {
		return "desc";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String message = StringUtils.join(args, " ");
		
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
		if(playerGroup == null) {
			player.sendMessage(ChatColor.RED + "You are not in a group!");
			return true;
		}
		if(!playerGroup.isGroupAdmin(player)) {
			player.sendMessage(ChatColor.RED + "You are not the group-leader!");
			return true;
		}
		if(StringUtils.isBlank(message)) {
			player.sendMessage(ChatColor.RED + "Please specify the text to set!");
			return false;
		}
		else {
			playerGroup.setGroupDescription(player, message);
			return true;
		}
	}

}
