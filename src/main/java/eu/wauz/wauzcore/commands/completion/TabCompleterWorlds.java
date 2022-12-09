package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.commands.admins.CmdWzEnterDev;

/**
 * A completer for the chat, that suggests world names.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzEnterDev
 */
public class TabCompleterWorlds implements TabCompleter {
	
	/**
	 * A list of available world names.
	 */
	private List<String> worldNameList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available world names.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		worldNameList = Bukkit.getWorlds().stream()
				.map(world -> world.getName().replace(" ", "_"))
				.collect(Collectors.toList());
		worldNameList.add("Survival_Overworld");
		worldNameList.add("Survival_Nether");
		worldNameList.add("Survival_End");
		
		if(args.length == 1) {
			return worldNameList.stream()
					.filter(worldName -> StringUtils.startsWith(worldName, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return worldNameList;
		}
		
		return null;
	}

}
