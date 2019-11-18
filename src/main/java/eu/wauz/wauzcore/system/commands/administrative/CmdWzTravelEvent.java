package eu.wauz.wauzcore.system.commands.administrative;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import net.md_5.bungee.api.ChatColor;

public class CmdWzTravelEvent implements WauzCommand {

	private static Map<String, Location> eventTravelMap = new HashMap<String, Location>();
	
	@Override
	public String getCommandId() {
		return "wzTravelEvent";
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
		
		Player player = (Player) sender;
		WauzTeleporter.eventTeleport(player, eventTravelMap.get(args[0]));
		return true;
	}
	
	public static Map<String, Location> getEventTravelMap() {
		return eventTravelMap;
	}

}
