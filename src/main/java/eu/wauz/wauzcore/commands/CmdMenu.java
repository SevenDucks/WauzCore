package eu.wauz.wauzcore.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.menu.AchievementsMenu;
import eu.wauz.wauzcore.menu.CraftingMenu;
import eu.wauz.wauzcore.menu.GroupMenu;
import eu.wauz.wauzcore.menu.GuildOverviewMenu;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.QuestMenu;
import eu.wauz.wauzcore.menu.SkillMenu;
import eu.wauz.wauzcore.menu.TravellingMenu;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Open a Menu</b></br>
 * - Usage: <b>/menu [name]</b></br>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdMenu implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "menu";
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
		if(args.length < 1) {
			return false;
		}
		
		Player player = (Player) sender;
		String menuType = args[0];
		boolean gamemodeMMORPG = WauzMode.isMMORPG(player) && !WauzMode.inHub(player);
		boolean gamemodeSurvivalOrMMORPG = WauzMode.isSurvival(player) || gamemodeMMORPG;
		
		if(gamemodeMMORPG && StringUtils.equalsIgnoreCase(menuType, "Main")) {
			WauzMenu.open(player);
			return true;
		}
		else if(gamemodeMMORPG && StringUtils.equalsIgnoreCase(menuType, "Travelling")) {
			TravellingMenu.open(player);
			return true;
		}
		else if(gamemodeMMORPG && StringUtils.equalsIgnoreCase(menuType, "Guild")) {
			GuildOverviewMenu.open(player);
			return true;
		}
		else if(gamemodeSurvivalOrMMORPG && StringUtils.equalsIgnoreCase(menuType, "Group")) {
			GroupMenu.open(player);
			return true;
		}
		else if(gamemodeMMORPG && StringUtils.equalsIgnoreCase(menuType, "Achievements")) {
			AchievementsMenu.open(player);
			return true;
		}
		else if(gamemodeMMORPG && StringUtils.equalsIgnoreCase(menuType, "Questlog")) {
			QuestMenu.open(player);
			return true;
		}
		else if(gamemodeMMORPG && StringUtils.equalsIgnoreCase(menuType, "Crafting")) {
			CraftingMenu.open(player);
			return true;
		}
		else if(gamemodeMMORPG && StringUtils.equalsIgnoreCase(menuType, "Pets")) {
			PetOverviewMenu.open(player, -1);
			return true;
		}
		else if(gamemodeMMORPG && StringUtils.equalsIgnoreCase(menuType, "Skills")) {
			SkillMenu.open(player);
			return true;
		}
		else {
			player.sendMessage(ChatColor.RED + "This menu does not exist or you can't open it here!");
			return true;
		}
	}

}
