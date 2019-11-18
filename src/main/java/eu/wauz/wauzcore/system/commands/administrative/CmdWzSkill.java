package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import net.md_5.bungee.api.ChatColor;

public class CmdWzSkill implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzSkill";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
			return true;
		}
		return WauzPlayerSkillExecutor.execute((Player) sender, null, args[0].replace("_", " "));
	}

}
