package eu.wauz.wauzcore.players;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.menu.social.TradeMenu;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzMode;

public class WauzPlayerTrade {
	/**
	 * Players currently trading with other player
	 */
	public static Map<String, String> playersOnTrading = new HashMap<String, String>();
	/**
	 * Max Player range for each other to trade.
	 */
	/**
	 * A map of send friend requests as player uuids.
	 */
	private static Map<String, String> requestTradeMap = new HashMap<>();
	
	
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
    	Player requestedPlayer = (Player) requestedPlayerName;
    	double playersDistance = requestingPlayer.getLocation().distance(requestedPlayer.getLocation());
    	if(WauzMode.inHub(requestingPlayer) == true) {
    		requestingPlayer.sendMessage(ChatColor.RED + "Sorry, that command doesn't exist in this world.");
    		return false;
    	}
    	if(requestingPlayer.getUniqueId().equals(requestedPlayerName.getUniqueId())) {
    		requestingPlayer.sendMessage(ChatColor.RED + "You cannot send trade request to yourself.");
    		return false;	
    	}
    	if(!(requestingPlayer.getWorld() == requestedPlayer.getWorld())) {
    		requestingPlayer.sendMessage(ChatColor.RED + "Requested Player is in another World.");
    		return false;
    	}
    	if(playersDistance > 7) {
    		requestingPlayer.sendMessage(ChatColor.RED + "The requested player is too far.");
    		return false;
    	}
    	if(requestingPlayer.getUniqueId().equals(requestedPlayer.getUniqueId())) {
			requestingPlayer.sendMessage(ChatColor.RED + "You cannot be your trading partner.");
			return false;
		}
    	if(playersOnTrading.containsKey(requestedPlayer.getName())) {
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
		WauzNmsClient.nmsChatCommand(requestedPlayer.getPlayer(), "trade " + requestingPlayer.getName(),
				ChatColor.RED +""+ChatColor.BOLD + "[" + ChatColor.GOLD + "TRADE" + ChatColor.RED + "] " + ChatColor.GREEN + requestingPlayer.getName() + ChatColor.RESET +""+ChatColor.GOLD+" wants to trade with you." +
				"To accept:" + ChatColor.RESET, false);
		requestedPlayer.playSound(requestedPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1); 
		requestingPlayer.sendMessage(ChatColor.YELLOW + "A trade request was sent to " + requestedPlayer.getName() + "!");
		return true;
    }
    
    /**
     *  Sending trade
     * @param requestingPlayer
     * @param requestedPlayerName
     * @return 
     * @return
     */
    public static boolean Trading(Player requestingPlayer, String requestedPlayerName) {
    	Player requestedPlayer = (Player) WauzCore.getOnlinePlayer(requestedPlayerName);
    	OfflinePlayer requestedOfflinePlayer = WauzCore.getOfflinePlayer(requestedPlayerName);
		if(requestedOfflinePlayer == null) {
			requestingPlayer.sendMessage(ChatColor.RED + "The requested player is unknown!");
			
			return false;
		}
		if(!onTradeCheck(requestingPlayer, requestedPlayer)) {
			return false;
		}
		String requestingPlayerUuid = requestingPlayer.getUniqueId().toString();
		String requestedPlayerUuid = requestedPlayer.getUniqueId().toString();
		Player requestedOnlinePlayer = requestedPlayer.getPlayer();
	
		boolean isRequestAnswer = StringUtils.equals(requestTradeMap.get(requestedPlayerUuid), requestingPlayerUuid);
		if(!isRequestAnswer) {
			if(requestedOnlinePlayer == null) {
				requestingPlayer.sendMessage(ChatColor.RED + "The requested player is not online!");
				return false;
			}
			else {
				requestTradeMap.put(requestingPlayerUuid, requestedPlayerUuid);
                acceptTrade(requestingPlayer, requestedPlayer);
			}
		}
		else {
			if(requestedOnlinePlayer == null) {
				requestingPlayer.sendMessage(ChatColor.RED + "The Requested player is not Online!");
				return false;
			}
	    	playersOnTrading.put(requestingPlayer.getName(), requestedPlayer.getName());
	    	playersOnTrading.put(requestedPlayer.getName(), requestingPlayer.getName());
			TradeMenu.requestingPlayerName = requestingPlayer;
			TradeMenu.requestedPlayerName = requestedPlayer;
			TradeMenu.onTrade(requestingPlayer,requestedPlayer);
			requestTradeMap.remove(requestedPlayer.getUniqueId().toString());
			return false;
		}
		return false;
    	
    }
}
