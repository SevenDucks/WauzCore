package eu.wauz.wauzcore.system.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.GuildConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import net.md_5.bungee.api.ChatColor;

public class CmdMotd implements WauzCommand {

	@Override
	public String getCommandId() {
		return "motd";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
			return true;
		}
		
		Player player = (Player) sender;
		String message = StringUtils.join(args, " ");
		
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		if(playerGuild == null) {
			player.sendMessage(ChatColor.RED + "You are not in a guild!");
			return true;
		}
		if(!playerGuild.isGuildOfficer(player)) {
			player.sendMessage(ChatColor.RED + "You are no guild-officer!");
			return true;
		}
		if(StringUtils.isBlank(message)) {
			player.sendMessage(ChatColor.RED + "Please specify the text to set!");
			return false;
		}
		else {
			GuildConfigurator.setGuildDescription(playerGuild.getGuildUuidString(), message);
			playerGuild.setGuildDescription(player, message);
			return true;
		}
	}

}
