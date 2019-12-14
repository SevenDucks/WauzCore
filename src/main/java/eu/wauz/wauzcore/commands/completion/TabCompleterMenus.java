package eu.wauz.wauzcore.commands.completion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.commands.CmdMenu;

/**
 * A completer for the chat, that suggests menu ids.
 * 
 * @author Wauzmons
 * 
 * @see CmdMenu
 */
public class TabCompleterMenus implements TabCompleter {
	
	/**
	 * A list of available menu ids.
	 */
	private List<String> menuList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available menu ids.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "menu")) {
			return null;
		}
		
		if(menuList == null) {
			menuList = Arrays.asList(
					"main", "travelling", "guild", "group", "achievements",
					"questlog", "crafting", "pets", "skills");
		}
		
		if(args.length == 1) {
			return menuList.stream()
					.filter(menuId -> StringUtils.startsWith(menuId, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return menuList;
		}
		
		return null;
	}

}
