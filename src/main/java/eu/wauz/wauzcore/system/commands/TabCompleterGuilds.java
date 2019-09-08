package eu.wauz.wauzcore.system.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.players.WauzPlayerGuild;

public class TabCompleterGuilds implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "apply"))
			return null;
		
		List<String> guildNamesList = WauzPlayerGuild.getGuildNames();
		
		if(args.length == 1)
			return guildNamesList.stream()
					.filter(guildName -> StringUtils.startsWith(guildName, args[0]))
					.collect(Collectors.toList());
		
		if(args.length == 0)
			return guildNamesList;
		
		return null;
	}

}
