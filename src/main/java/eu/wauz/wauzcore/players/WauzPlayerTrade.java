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

@SuppressWarnings("unused")
public class WauzPlayerTrade {
	/**
	 * Players currently trading with other player
	 */
	private static ArrayList<String> PlayerCurrentlyTrading = new ArrayList<String>();
	/**
	 * Max Player range for each other to trade.
	 */
	public static final int MAX_PLAYER_BLOCK_RANGE_FOR_TRADE = 5;
	/**
	 * A map of send trade requests as player uuids.
	 */
	private static Map<String,String> TradeRequestMap = new HashMap<>();
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
    @SuppressWarnings("unlikely-arg-type")
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
    	if(PlayerCurrentlyTrading.contains(requestedPlayer.getUniqueId())) {
    		requestingPlayer.sendMessage(ChatColor.RED + "The player you requested for trade is currently trading with someone. Please, Try again later.");
    		return false;
    	}

		return true;
    }
    
    public static boolean Trading(Player requestingPlayer, String requestedPlayerName) {
		OfflinePlayer requestedPlayer = WauzCore.getOfflinePlayer(requestedPlayerName);
		if(requestedPlayer == null) {
			requestingPlayer.sendMessage(ChatColor.RED + "The requested player is unknown!");
			return false;
		}
		if(!onTradeCheck(requestingPlayer, requestedPlayer)) {
			return false;
		}
		
		return true;
    	
    }
}
