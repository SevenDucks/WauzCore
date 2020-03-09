package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.CmdApply;
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
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "apply")) {
			return null;
		}
		
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
