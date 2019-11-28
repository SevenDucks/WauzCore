package eu.wauz.wauzcore.system.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.menu.GroupMenu;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class CmdGroup implements WauzCommand {

	@Override
	public String getCommandId() {
		return "group";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if((WauzMode.isMMORPG(player) || WauzMode.isSurvival(player)) && !WauzMode.inHub(player)) {
			GroupMenu.open(player);
			return true;
		}
		else {
			player.sendMessage(ChatColor.RED + "You can't open that menu here!");
			return true;
		}
	}

}
