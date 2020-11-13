package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.administrative.CmdWzRank;
import eu.wauz.wauzcore.system.WauzRank;

/**
 * A completer for the chat, that suggests rank ids.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzRank
 */
public class TabCompleterRanks implements TabCompleter {
	
	/**
	 * A list of available rank ids.
	 */
	private List<String> rankTypesList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available rank ids.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(rankTypesList == null) {
			rankTypesList = WauzRank.getAllRankIds();
		}
		
		if(args.length == 1) {
			return rankTypesList.stream()
					.filter(runeId -> StringUtils.startsWith(runeId, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return rankTypesList;
		}
		
		return null;
	}

}
