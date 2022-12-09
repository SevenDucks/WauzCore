package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.commands.admins.CmdWzStart;

/**
 * A completer for the chat, that suggests minigame names.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzStart
 */
public class TabCompleterMinigames implements TabCompleter {
	
	/**
	 * A list of available minigame names.
	 */
	private List<String> minigameList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available minigame names.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(minigameList == null) {
			minigameList = ArcadeLobby.getAllMinigameNames();
		}
		
		if(args.length == 1) {
			return minigameList.stream()
					.filter(game -> StringUtils.startsWith(game, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return minigameList;
		}
		
		return null;
	}

}
