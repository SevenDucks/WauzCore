package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.administrative.CmdWzGetPet;
import eu.wauz.wauzcore.mobs.pets.WauzPet;

/**
 * A completer for the chat, that suggests pet ids.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzGetPet
 */
public class TabCompleterPets implements TabCompleter {
	
	/**
	 * A list of available pet ids.
	 */
	private List<String> playerPetList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available pet ids.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "wzGetPet")) {
			return null;
		}
		
		if(playerPetList == null) {
			playerPetList = WauzPet.getAllPetKeys().stream()
					.map(petId -> petId.replace(" ", "_"))
					.collect(Collectors.toList());
		}
		
		if(args.length == 1) {
			return playerPetList.stream()
					.filter(petId -> StringUtils.startsWith(petId, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return playerPetList;
		}
		
		return null;
	}

}
