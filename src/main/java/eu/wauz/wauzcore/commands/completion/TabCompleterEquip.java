package eu.wauz.wauzcore.commands.completion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.administrative.CmdWzGetEquip;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;

/**
 * A completer for the chat, that suggests equipment types.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzGetEquip
 */
public class TabCompleterEquip implements TabCompleter {
	
	/**
	 * A list of available equipment types.
	 */
	private List<String> equipTypeList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available equipment types.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "wzGetEquip")) {
			return null;
		}
		
		if(equipTypeList == null) {
			equipTypeList = WauzEquipmentIdentifier.getAllEquipTypes().stream()
					.map(equipType -> equipType.getName().replace(" ", "_"))
					.collect(Collectors.toList());
		}
		
		if(args.length == 1) {
			return equipTypeList.stream()
					.filter(equipType -> StringUtils.startsWith(equipType, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 2) {
			return Arrays.asList("1", "2", "3", "4", "5", "6").stream()
					.filter(tier -> StringUtils.startsWith(tier, args[1]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return equipTypeList;
		}
		
		return null;
	}

}
