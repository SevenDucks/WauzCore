package eu.wauz.wauzcore.players.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerNotifier {
	
	private static List<String> tipMessages = new ArrayList<>(Arrays.asList(
			"You can type /hub to return to the Nexus.",
			"We are reachable under dev@wauz.eu",
			"Got lost? Open map.wauz.eu in your browser!",
			"Wauzland was originally made by a single dev!",
			"Gabor Gehrig is ill today."));
	
	public static boolean execute(Player player) {
		String randomMessage = tipMessages.get(new Random().nextInt(tipMessages.size()));
		player.sendMessage(ChatColor.YELLOW + "Did you know? " + ChatColor.GOLD + randomMessage);
		
		try {
			if(WauzMode.isMMORPG(player) && !WauzMode.inHub(player)) {
				int unusedPoints = PlayerConfigurator.getCharacterUnusedStatpoints(player);
				if(unusedPoints > 0) {
					WauzNmsClient.nmsChatCommand(player, "menu skills",
							ChatColor.YELLOW + "You have " + unusedPoints +
							" unused Skillpoints! To allocate them:", false);
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
