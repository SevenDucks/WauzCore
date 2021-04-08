package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.data.players.GuildConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.<br/>
 * - Description: <b>Set the Guild Message of the Day</b><br/>
 * - Usage: <b>/modt [text]</b><br/>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdMotd implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("motd");
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
