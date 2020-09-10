package eu.wauz.wauzcore.players;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.menu.TradeMenu;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A class to let two players trade with each other.
 * 
 * @author Eddshine
 */
public class WauzPlayerTrade {
	
	/**
	 * The maximum distance for players to trade with eachother.
	 */
	public static final int MAX_TRADE_RANGE = 5;
	
	/**
	 * A map of sent trade requests as player uuids.
	 */
	private static Map<String, String> requestMap = new HashMap<>();
	
	/**
	 * Checks if a player can trade with another player.
	 * 
	 * @param requestingPlayer The player who wants to send the trade request.
	 * @param requestedPlayer The player that is requested.
	 * 
	 * @return If a trade request is possible.
	 */
	public static boolean canTrade(Player requestingPlayer, Player requestedPlayer) {
    	if(requestingPlayer.getUniqueId().equals(requestedPlayer.getUniqueId())) {
    		requestingPlayer.sendMessage(ChatColor.RED + "You cannot send a trade request to yourself!");
    		return false;	
    	}
    	if(WauzMode.inHub(requestingPlayer)) {
    		requestingPlayer.sendMessage(ChatColor.RED + "You cannot trade in this world!");
    		return false;
    	}
    	if(!(requestingPlayer.getWorld().equals(requestedPlayer.getWorld()))) {
    		requestingPlayer.sendMessage(ChatColor.RED + "You cannot trade with a player in another world!");
    		return false;
    	}
    	if(requestingPlayer.getLocation().distance(requestedPlayer.getLocation()) > MAX_TRADE_RANGE) {
    		requestingPlayer.sendMessage(ChatColor.RED + "The requested player is too far away!");
    		return false;
    	}
    	return true;
    }
    
    /**
     * Tries to trade with another player.
	 * If no request was sent before, one will be sent and must be confirmed first.
	 * 
     * @param requestingPlayer The player who wants to trade.
     * @param requestedPlayerName The name of the player that should be traded with.
     * 
     * @return If the trade window was opened or a request was sent.
     * 
     * @see WauzPlayerTrade#canTrade(Player, Player)
     * @see WauzNmsClient#nmsChatCommand(Player, String, String, boolean)
     * @see TradeMenu#open(Player, Player)
     */
    public static boolean tryToTrade(Player requestingPlayer, String requestedPlayerName) {
    	OfflinePlayer requestedPlayer = WauzCore.getOfflinePlayer(requestedPlayerName);
		if(requestedPlayer == null) {
			requestingPlayer.sendMessage(ChatColor.RED + "The requested player is unknown!");
			return false;
		}
		Player requestedOnlinePlayer = requestedPlayer.getPlayer();
		if(requestedOnlinePlayer == null) {
			requestingPlayer.sendMessage(ChatColor.RED + "The requested player is not online!");
			return false;
		}
		if(!canTrade(requestingPlayer, requestedOnlinePlayer)) {
			return false;
		}
		
		String requestingPlayerUuid = requestingPlayer.getUniqueId().toString();
		String requestedPlayerUuid = requestedPlayer.getUniqueId().toString();
		
		boolean isRequestAnswer = StringUtils.equals(requestMap.get(requestedPlayerUuid), requestingPlayerUuid);
		if(!isRequestAnswer) {
			requestMap.put(requestingPlayerUuid, requestedPlayerUuid);
			WauzNmsClient.nmsChatCommand(requestedOnlinePlayer, "trade " + requestingPlayer.getName(),
					ChatColor.YELLOW + requestingPlayer.getName() + " wants to trade! " +
					"To accept:", false);
			requestedOnlinePlayer.playSound(requestedOnlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1); 
			requestingPlayer.sendMessage(ChatColor.YELLOW + "A trade request was sent to " + requestedPlayer.getName() + "!");
		}
		else {
			TradeMenu.open(requestingPlayer, requestedOnlinePlayer);
			requestMap.remove(requestedPlayer.getUniqueId().toString());
		}
		return true;
    }
}
