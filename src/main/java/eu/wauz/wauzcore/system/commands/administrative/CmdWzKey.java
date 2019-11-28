package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import net.md_5.bungee.api.ChatColor;

public class CmdWzKey implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzKey";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length != 3) {
			return false;
		}
		
		World world = args[0].equals("this") ? ((Player) sender).getWorld() : Bukkit.getWorld(args[0]);
		if(world == null) {
			sender.sendMessage(ChatColor.RED + "Unknown world specified!");
			return false;
		}
		
		String keyId = args[1];
		String keyStatus;
		
		switch (args[2]) {
		case "1":
			keyStatus = InstanceConfigurator.KEY_STATUS_OBTAINED;
			break;
		case "2":
			keyStatus = InstanceConfigurator.KEY_STATUS_USED;
			break;
		default:
			keyStatus = InstanceConfigurator.KEY_STATUS_UNOBTAINED;
			break;
		}
		
		InstanceConfigurator.setInstanceWorldKeyStatus(world, keyId, keyStatus);
		for(Player player : world.getPlayers()) {
			WauzPlayerScoreboard.scheduleScoreboard(player);
			player.sendMessage(ChatColor.GREEN + "You obtained the Key \"" + ChatColor.DARK_AQUA + keyId + ChatColor.GREEN + "\"!");
		}
		return true;
	}

}
