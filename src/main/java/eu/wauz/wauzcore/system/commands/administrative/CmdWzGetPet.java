package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import net.md_5.bungee.api.ChatColor;

public class CmdWzGetPet implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzGetPet";
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
		
		Player player = null;
		String type = null;
		
		if(args.length > 1) {
			player = WauzCore.getOnlinePlayer(args[0]);
			type = args[1];
		}
		else {
			player = (Player) sender;
			type = args[0];
		}
		
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		
		// TODO Check if valid pet type is given.
		PetOverviewMenu.addPet(player, null, type);
		return true;
	}

}
