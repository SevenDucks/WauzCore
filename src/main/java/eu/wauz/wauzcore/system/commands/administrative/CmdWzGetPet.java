package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.md_5.bungee.api.ChatColor;

public class CmdWzGetPet implements WauzCommand {
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();

	@Override
	public String getCommandId() {
		return "wzGetPet";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
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
		
		if(type == null || mythicMobs.getMythicMob(type) == null) {
			sender.sendMessage(ChatColor.RED + "Unknown pet type specified!");
			return false;
		}
		
		PetOverviewMenu.addPet(player, null, type);
		return true;
	}

}
