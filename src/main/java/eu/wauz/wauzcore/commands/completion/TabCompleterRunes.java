package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.admins.CmdWzGetRune;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;

/**
 * A completer for the chat, that suggests rune ids.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzGetRune
 */
public class TabCompleterRunes implements TabCompleter {
	
	/**
	 * A list of available rune ids.
	 */
	private List<String> runeTypesList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available rune ids.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "wzGetRune")) {
			return null;
		}
		
		if(runeTypesList == null) {
			runeTypesList = WauzRuneInserter.getAllRuneIds();
		}
		
		if(args.length == 1) {
			return runeTypesList.stream()
					.filter(runeId -> StringUtils.startsWith(runeId, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return runeTypesList;
		}
		
		return null;
	}

}
