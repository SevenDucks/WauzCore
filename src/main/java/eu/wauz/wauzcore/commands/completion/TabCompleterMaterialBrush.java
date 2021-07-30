package eu.wauz.wauzcore.commands.completion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.building.shapes.WauzBrushes;
import eu.wauz.wauzcore.commands.builders.CmdWeMaterialBrush;

/**
 * A completer for the chat, that suggests material brushes.
 * 
 * @author Wauzmons
 * 
 * @see CmdWeMaterialBrush
 */
public class TabCompleterMaterialBrush implements TabCompleter {
	
	/**
	 * A list of available materials.
	 */
	private List<String> materialList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available equipment types.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(materialList == null) {
			materialList = Arrays.asList(Material.values()).stream()
					.filter(material -> material.isBlock())
					.map(material -> material.toString().toLowerCase())
					.collect(Collectors.toList());
		}
		
		if(args.length == 1) {
			return WauzBrushes.getAllBrushNames().stream()
					.filter(brushName -> StringUtils.startsWith(brushName, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 2) {
			return materialList.stream()
					.filter(material -> StringUtils.startsWith(material, args[1]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 3) {
			return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9").stream()
					.filter(size -> StringUtils.startsWith(size, args[2]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 4) {
			return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9").stream()
					.filter(size -> StringUtils.startsWith(size, args[3]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return WauzBrushes.getAllBrushNames();
		}
		
		return null;
	}

}
