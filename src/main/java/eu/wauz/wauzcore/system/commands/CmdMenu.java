package eu.wauz.wauzcore.system.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.menu.SkillMenu;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class CmdMenu implements WauzCommand {

	@Override
	public String getCommandId() {
		return "menu";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
			return true;
		}
		if(args.length < 1) {
			return false;
		}
		
		// TODO Add other menus.
		Player player = (Player) sender;
		if(WauzMode.isMMORPG(player) && !WauzMode.inHub(player)) {
			if(args[0].equals("main")) {
				WauzMenu.open((Player) sender);
				return true;
			}
			else if(args[0].equals("skills")) {
				SkillMenu.open((Player) sender);
				return true;
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You can't open that menu here!");
			return true;
		}
		return false;
	}

}
