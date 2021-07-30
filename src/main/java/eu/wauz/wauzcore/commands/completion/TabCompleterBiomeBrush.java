package eu.wauz.wauzcore.commands.completion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.building.shapes.WauzBrushes;
import eu.wauz.wauzcore.commands.builders.CmdWeBiomeBrush;

/**
 * A completer for the chat, that suggests biome brushes.
 * 
 * @author Wauzmons
 * 
 * @see CmdWeBiomeBrush
 */
public class TabCompleterBiomeBrush implements TabCompleter {
	
	/**
	 * A list of available biomes.
	 */
	private List<String> biomeList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available equipment types.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(biomeList == null) {
			biomeList = Arrays.asList(Biome.values()).stream()
					.map(biome -> biome.toString().toLowerCase())
					.collect(Collectors.toList());
		}
		
		if(args.length == 1) {
			return WauzBrushes.getAllBrushNames().stream()
					.filter(brushName -> StringUtils.startsWith(brushName, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 2) {
			return biomeList.stream()
					.filter(biome -> StringUtils.startsWith(biome, args[1]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 3) {
			return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9").stream()
					.filter(size -> StringUtils.startsWith(size, args[2]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return WauzBrushes.getAllBrushNames();
		}
		
		return null;
	}

}
