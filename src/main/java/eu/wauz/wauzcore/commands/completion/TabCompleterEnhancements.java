package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.commands.administrative.CmdWzEnhanced;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;

/**
 * A completer for the chat, that suggests enhancement ids.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzEnhanced
 */
public class TabCompleterEnhancements implements TabCompleter {
	
	/**
	 * A list of available enhancement ids.
	 */
	private List<String> enhancementTypesList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available enhancement ids.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "wzEnhanced")) {
			return null;
		}
		
		if(enhancementTypesList == null) {
			enhancementTypesList = WauzEquipmentEnhancer.getAllEnhancementIds();
		}
		
		if(args.length == 1) {
			return enhancementTypesList.stream()
					.filter(enhancementId -> StringUtils.startsWith(enhancementId, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return enhancementTypesList;
		}
		
		return null;
	}

}
