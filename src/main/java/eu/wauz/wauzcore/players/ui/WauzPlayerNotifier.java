package eu.wauz.wauzcore.players.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerMailConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An UI class to show the player notifications and tips in the chat.
 * 
 * @author Wauzmons
 */
public class WauzPlayerNotifier {
	
	/**
	 * A list of all tip messages.
	 */
	private static List<String> tipMessages = new ArrayList<>(Arrays.asList(
			"You can type /hub to return to the Nexus.",
			"We are reachable under dev@wauz.eu",
			"Got lost? Open map.wauz.eu in your browser!",
			"The game was originally made by a single dev!",
			"Gabor Gehrig is ill today."));
	
	/**
	 * Shows a tip message to the given player.
	 * If in MMORPG mode, the player will also receive a clickable notifications,
	 * about unused skillpoints or unread mails, that can open the menu.
	 * 
	 * @param player The player that should receive the chat notifications.
	 * 
	 * @return If the messages were shown successfully.
	 * 
	 * @see UnicodeUtils#sendChatCommand(Player, String, String, boolean)
	 * @see PlayerSkillConfigurator#getUnusedStatpoints(Player)
	 * @see PlayerMailConfigurator#getPlayerMailNameList(Player)
	 */
	public static boolean execute(Player player) {
		String randomMessage = tipMessages.get(Chance.randomInt(tipMessages.size()));
		player.sendMessage(ChatColor.YELLOW + "Did you know? " + ChatColor.GOLD + randomMessage);
		
		try {
			if(WauzMode.isMMORPG(player) && !WauzMode.inHub(player)) {
				int unusedPoints = PlayerSkillConfigurator.getUnusedStatpoints(player);
				if(unusedPoints > 0) {
					UnicodeUtils.sendChatCommand(player, "menu skills",
							ChatColor.YELLOW + "You have " + unusedPoints +
							" unused Skillpoints! To allocate them:", false);
				}
				int unreadMails = PlayerMailConfigurator.getPlayerMailNameList(player).size();
				if(unreadMails > 0) {
					UnicodeUtils.sendChatCommand(player, "menu mails",
							ChatColor.YELLOW + "You have " + unreadMails +
							" unread Mails! To read them:", false);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
