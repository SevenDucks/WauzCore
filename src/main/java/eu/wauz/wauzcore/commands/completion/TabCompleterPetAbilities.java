package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.admins.CmdWzGetPet;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbilities;

/**
 * A completer for the chat, that suggests pet and ability ids.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzGetPet
 */
public class TabCompleterPetAbilities implements TabCompleter {
	
	/**
	 * A list of available pet ids.
	 */
	private List<String> playerPetList;
	
	/**
	 * A list of available pet ability ids.
	 */
	private List<String> playerPetAbilityList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available pet ids.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(playerPetList == null) {
			playerPetList = WauzPet.getAllPetKeys().stream()
					.map(petId -> petId.replace(" ", "_"))
					.collect(Collectors.toList());
		}
		
		if(playerPetAbilityList == null) {
			playerPetAbilityList = WauzPetAbilities.getAllAbilityKeys().stream()
					.map(abilityId -> abilityId.replace(" ", "_"))
					.collect(Collectors.toList());
		}
		
		if(args.length == 1) {
			return playerPetList.stream()
					.filter(petId -> StringUtils.startsWith(petId, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 2) {
			return playerPetAbilityList.stream()
					.filter(abilityId -> StringUtils.startsWith(abilityId, args[1]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return playerPetList;
		}
		
		return null;
	}

}
