package eu.wauz.wauzcore.system.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;

public class TabCompleterRunes implements TabCompleter {
	
	private List<String> runeTypesList;

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "wzGetRune"))
			return null;
		
		if(runeTypesList == null)
			runeTypesList = WauzRuneInserter.getAllRuneIds();
		
		if(args.length == 1)
			return runeTypesList.stream()
					.filter(runeId -> StringUtils.startsWith(runeId, args[0]))
					.collect(Collectors.toList());
		
		if(args.length == 0)
			return runeTypesList;
		
		return null;
	}

}
