package eu.wauz.wauzcore.system.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;

public class TabCompleterEnhancements implements TabCompleter {
	
	private List<String> enhancementTypesList;

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "wzEnhanced"))
			return null;
		
		if(enhancementTypesList == null)
			enhancementTypesList = WauzEquipmentEnhancer.getAllEnhancementIds();
		
		if(args.length == 1)
			return enhancementTypesList.stream()
					.filter(runeId -> StringUtils.startsWith(runeId, args[0]))
					.collect(Collectors.toList());
		
		if(args.length == 0)
			return enhancementTypesList;
		
		return null;
	}

}
