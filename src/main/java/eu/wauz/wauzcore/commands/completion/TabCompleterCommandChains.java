package eu.wauz.wauzcore.commands.completion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.building.WauzCommandChain;
import eu.wauz.wauzcore.commands.admins.CmdWzTravel;

/**
 * A completer for the chat, that suggests command chain keys.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzTravel
 */
public class TabCompleterCommandChains implements TabCompleter {

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available waypoint keys.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> commandChainKeyList = WauzCommandChain.getAllCommandChainKeys();
		
		if(args.length == 1) {
			return commandChainKeyList.stream()
					.filter(commandChainKey -> StringUtils.startsWith(commandChainKey, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 2) {
			return Arrays.asList("5", "10", "15", "20", "25", "30", "35", "40", "45").stream()
					.filter(timeout -> StringUtils.startsWith(timeout, args[1]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return commandChainKeyList;
		}
		
		return null;
	}

}
