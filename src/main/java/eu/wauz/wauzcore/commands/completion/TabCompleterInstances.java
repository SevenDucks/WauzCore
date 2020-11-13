package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.administrative.CmdWzEnter;
import eu.wauz.wauzcore.system.instances.WauzInstance;

/**
 * A completer for the chat, that suggests instance names.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzEnter
 */
public class TabCompleterInstances implements TabCompleter {
	
	/**
	 * A list of available instance names.
	 */
	private List<String> instanceNameList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available instance names.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(instanceNameList == null) {
			instanceNameList = WauzInstance.getAllInstanceNames().stream()
					.map(instanceName -> instanceName.replace(" ", "_"))
					.collect(Collectors.toList());
		}
		
		if(args.length == 1) {
			return instanceNameList.stream()
					.filter(instanceName -> StringUtils.startsWith(instanceName, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return instanceNameList;
		}
		
		return null;
	}

}
