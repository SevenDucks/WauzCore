package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;

public class WauzPlayerTrade {
	/**
	 * Players currently trading with other player
	 */
	private static ArrayList<String> playersOnTrading = new ArrayList<String>();
	/**
	 * Max Player range for each other to trade.
	 */
	public static final int MAX_PLAYER_BLOCK_RANGE_FOR_TRADE = 5;
	/**
	 * Checks if a player can add another player as friend.
	 * 
	 * @param requestingPlayer The player who wants to send the trade request.
	 * @param requestedPlayer The player that is requested.
	 * 
	 * @return If a request is possible.
	 * 
	 * 
	 */
	public static boolean onTradeCheck(Player requestingPlayer, OfflinePlayer requestedPlayerName) {
    	if(requestingPlayer.getUniqueId().equals(requestedPlayerName.getUniqueId())) {
    		requestingPlayer.sendMessage(ChatColor.RED + "You cannot send trade request to yourself.");
    		return false;
    	}
    	Player requestedPlayer = (Player) requestedPlayerName;
    	double playersDistance = requestingPlayer.getLocation().distance(requestedPlayer.getLocation());
    	if(playersDistance > 7) {
    		requestingPlayer.sendMessage(ChatColor.RED + "The requested player is too far.");
    	}
    	if(playersOnTrading.contains(requestedPlayer.getName())) {
    		requestingPlayer.sendMessage(ChatColor.RED + "The player you requested for trade is currently trading with someone. Please, Try again later.");
    		return false;
    	}

		return true;
    }
    /**
     * Accept a trade from requesting player
     * @param requestingPlayer
     * @param requestedPlayer
     * @return
     */
    public static boolean acceptTrade(Player requestingPlayer, Player requestedPlayer) {
    	playersOnTrading.add(requestingPlayer.getName());
    	playersOnTrading.add(requestedPlayer.getName());
		WauzNmsClient.nmsChatCommand(requestedPlayer.getPlayer(), "trade " + requestingPlayer.getName(),
				ChatColor.YELLOW + requestingPlayer.getName() + " wants to trade! " +
				"To accept:", false);
		requestingPlayer.sendMessage(ChatColor.YELLOW + "A trade request was sent to " + requestedPlayer.getName() + "!");
		return true;
    }
    /**
     * decline a trade from requestingPlayer
     * @param requestingPlayer
     * @param requestedPlayer
     * @return
     */
    public static boolean denyTrade(Player requestingPlayer, Player requestedPlayer) {
    	playersOnTrading.add(requestingPlayer.getName());
    	playersOnTrading.add(requestedPlayer.getName());
		WauzNmsClient.nmsChatCommand(requestedPlayer.getPlayer(), "trade " + requestingPlayer.getName(),
				ChatColor.YELLOW + requestingPlayer.getName() + " wants to trade! " +
				"To accept:", false);
		requestingPlayer.sendMessage(ChatColor.YELLOW + "A trade request was sent to " + requestedPlayer.getName() + "!");
		return true;
    }
    
    public static boolean Trading(Player requestingPlayer, Player requestedPlayer) {

		if(!onTradeCheck(requestingPlayer, requestedPlayer)) {
			return false;
		}
		Player requestedOnlinePlayer = requestedPlayer.getPlayer();
		if(requestedOnlinePlayer == null) {
			requestingPlayer.sendMessage(ChatColor.RED + "The requested player is not online!");
			return false;
		}else {
			acceptTrade(requestingPlayer, requestedPlayer);
			denyTrade(requestingPlayer, requestedPlayer);
		}
		return true;
    	
    }
}
