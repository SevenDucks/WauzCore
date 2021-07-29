package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.admins.CmdWzTravel;
import eu.wauz.wauzcore.system.WauzWaypoint;

/**
 * A completer for the chat, that suggests waypoint keys.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzTravel
 */
public class TabCompleterWaypoints implements TabCompleter {

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available waypoint keys.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> waypointKeyList = WauzWaypoint.getAllWaypointKeys();
		
		if(args.length == 1) {
			return waypointKeyList.stream()
					.filter(waypointKey -> StringUtils.startsWith(waypointKey, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return waypointKeyList;
		}
		
		return null;
	}

}
