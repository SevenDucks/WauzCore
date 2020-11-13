package eu.wauz.wauzcore.commands.completion;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.commands.administrative.CmdWzSkill;
import eu.wauz.wauzcore.commands.administrative.CmdWzSkillWeapon;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;

/**
 * A completer for the chat, that suggests skill ids.
 * 
 * @author Wauzmons
 * 
 * @see CmdWzSkill
 * @see CmdWzSkillWeapon
 */
public class TabCompleterSkills implements TabCompleter {
	
	/**
	 * A list of available skill ids.
	 */
	private List<String> playerSkillList;

	/**
	 * Delivers a list of possible completions for a command argument.
	 * 
	 * @return A list of available skill ids.
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(playerSkillList == null) {
			playerSkillList = WauzPlayerSkillExecutor.getAllSkillIds().stream()
					.map(skillId -> skillId.replace(" ", "_"))
					.collect(Collectors.toList());
		}
		
		if(args.length == 1) {
			return playerSkillList.stream()
					.filter(skillId -> StringUtils.startsWith(skillId, args[0]))
					.collect(Collectors.toList());
		}
		
		if(args.length == 0) {
			return playerSkillList;
		}
		
		return null;
	}

}
