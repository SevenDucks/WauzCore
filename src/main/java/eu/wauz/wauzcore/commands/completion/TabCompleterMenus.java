package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.CmdMenu;
import eu.wauz.wauzcore.menu.util.MenuRegister;

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
			menuList = MenuRegister.getAllInventoryIds();
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
