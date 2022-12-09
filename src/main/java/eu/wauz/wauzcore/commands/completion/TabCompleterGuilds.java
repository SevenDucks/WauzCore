package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.commands.players.CmdApply;
import eu.wauz.wauzcore.players.WauzPlayerGuild;

/**
 * A completer for the chat, that suggests guild names.
 * 
 * @author Wauzmons
 * 
 * @see CmdApply
 */
public class TabCompleterGuilds implements TabCompleter {

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of existing guild names.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> guildNamesList = WauzPlayerGuild.getGuildNames();
		
		if(args.length == 1) {
			return guildNamesList.stream()
					.filter(guildName -> StringUtils.startsWith(guildName, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return guildNamesList;
		}
		
		return null;
	}

}
