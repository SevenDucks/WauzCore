package eu.wauz.wauzcore.commands.builders;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.building.shapes.WauzBrush;
import eu.wauz.wauzcore.building.shapes.WauzBrushes;
import eu.wauz.wauzcore.commands.WauzCommand;
import eu.wauz.wauzcore.commands.WauzCommandExecutor;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Get Biome Brush</b><br>
 * - Usage: <b>/weBiomeBrush [shape] [biome] [radius]</b><br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWeBiomeBrush implements WauzCommand {
	
	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("weBiomeBrush", "bbrush");
	}

	/**
	 * Executes the command for given sender with arguments.
	 * 
	 * @param sender The sender of the command.
	 * @param args The arguments of the command.
	 * 
	 * @return If the command had correct syntax.
	 */
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length < 3) {
			return false;
		}
		
		WauzBrush brushTemplate = WauzBrushes.getBrush(args[0].toLowerCase());
		if(brushTemplate == null) {
			sender.sendMessage(ChatColor.RED + "Unknown brush shape specified!");
			return false;
		}
		
		Biome biome;
		try {
			biome = Biome.valueOf(args[1].toUpperCase());
		}
		catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Invalid biome specified!");
			return false;
		}
		
		int radius;
		try {
			radius = Integer.parseInt(args[2]);
			if(radius < 0 || radius > 50) {
				throw new IllegalArgumentException();
			}
		}
		catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Invalid radius specified!");
			return false;
		}
		
		WauzBrush brush = brushTemplate.getInstance(radius, -1).withBiome(biome);
		((Player) sender).getInventory().addItem(WauzBrushes.registerInstancedBrush(brush));
		return true;
	}

}
