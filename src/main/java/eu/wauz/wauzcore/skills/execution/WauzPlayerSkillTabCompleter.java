package eu.wauz.wauzcore.skills.execution;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class WauzPlayerSkillTabCompleter implements TabCompleter {
	
	private List<String> playerSkillList;

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(!StringUtils.startsWithIgnoreCase(command.getName(), "wzSkill"))
			return null;
		
		if(playerSkillList == null)
			playerSkillList = WauzPlayerSkillExecutor.getAllSkillIds().stream()
					.map(skillId -> skillId.replace(" ", "_"))
					.collect(Collectors.toList());
		
		if(args.length == 1)
			return playerSkillList.stream()
					.filter(skillId -> StringUtils.startsWith(skillId, args[0]))
					.collect(Collectors.toList());
		
		if(args.length == 0)
			return playerSkillList;
		
		return null;
	}

}
