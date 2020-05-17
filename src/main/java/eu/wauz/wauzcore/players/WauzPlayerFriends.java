package eu.wauz.wauzcore.players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;

/**
 * A class to interact with the friend list of a player.
 * 
 * @author Wauzmons
 */
public class WauzPlayerFriends {

	/**
	 * The maximum amount of friends a player can have at a time.
	 */
	public static final int MAX_FRIEND_AMOUNT = 13;
	
	/**
	 * A map of send friend requests as player uuids.
	 */
	private static Map<String, String> requestMap = new HashMap<>();
	
	/**
	 * Checks if a player can add another player as friend.
	 * 
	 * @param requestingPlayer The player who wants to send the friend request.
	 * @param requestedPlayer The player that is requested.
	 * 
	 * @return If a request is possible.
	 * 
	 * @see WauzPlayerFriends#getFriendsList(OfflinePlayer)
	 */
	public static boolean canAddFriend(Player requestingPlayer, OfflinePlayer requestedPlayer) {
		if(requestingPlayer.getUniqueId().equals(requestedPlayer.getUniqueId())) {
			requestingPlayer.sendMessage(ChatColor.RED + "You (sadly) cannot be your own friend!");
			return false;
		}
		List<String> requestingPlayerFriends = getFriendsList(requestingPlayer);
		List<String> requestedPlayerFriends = getFriendsList(requestedPlayer);
		if(requestingPlayerFriends.contains(requestedPlayer.getUniqueId().toString())) {
			requestingPlayer.sendMessage(ChatColor.RED + "You already have this player on your friends list!");
			return false;
		}
		if(requestedPlayerFriends.contains(requestingPlayer.getUniqueId().toString())) {
			requestingPlayer.sendMessage(ChatColor.RED + "This player already has you on their friends list!");
			return false;
		}
		if(requestingPlayerFriends.size() >= MAX_FRIEND_AMOUNT) {
			requestingPlayer.sendMessage(ChatColor.RED + "You already have the maximum amount of friends!");
			return false;
		}
		if(requestedPlayerFriends.size() >= MAX_FRIEND_AMOUNT) {
			requestingPlayer.sendMessage(ChatColor.RED + requestedPlayer.getName() + " already has the maximum amount of friends!");
			return false;
		}
		return true;
	}
	
	/**
	 * Adds a friend to the player's friend list.
	 * If no request was send before, one is sent and must be confirmed first.
	 * 
	 * @param requestingPlayer The player who wants to add a friend.
	 * @param requestedPlayerName The name of the player that is added.
	 * 
	 * @return If the friend was added or a request was sent.
	 * 
	 * @see WauzPlayerFriends#canAddFriend(Player, OfflinePlayer)
	 * @see WauzPlayerFriends#getFriendsList(OfflinePlayer)
	 * @see WauzNmsClient#nmsChatCommand(Player, String, String, boolean)
	 * @see PlayerConfigurator#setFriendsList(OfflinePlayer, List)
	 */
	public static boolean addFriend(Player requestingPlayer, String requestedPlayerName) {
		OfflinePlayer requestedPlayer = WauzCore.getOfflinePlayer(requestedPlayerName);
		if(requestedPlayer == null) {
			requestingPlayer.sendMessage(ChatColor.RED + "The requested player is unknown!");
			return false;
		}
		if(!canAddFriend(requestingPlayer, requestedPlayer)) {
			return false;
		}
		
		String requestingPlayerUuid = requestingPlayer.getUniqueId().toString();
		String requestedPlayerUuid = requestedPlayer.getUniqueId().toString();
		
		Player requestedOnlinePlayer = requestedPlayer.getPlayer();
		boolean isRequestAnswer = StringUtils.equals(requestMap.get(requestedPlayerUuid), requestingPlayerUuid);
		if(!isRequestAnswer) {
			if(requestedOnlinePlayer == null) {
				requestingPlayer.sendMessage(ChatColor.RED + "The requested player is not online!");
				return false;
			}
			else {
				requestMap.put(requestingPlayerUuid, requestedPlayerUuid);
				WauzNmsClient.nmsChatCommand(requestedOnlinePlayer, "friend " + requestingPlayer.getName(),
						ChatColor.YELLOW + requestingPlayer.getName() + " wants to be friends! " +
						"To accept:", false);
				requestingPlayer.sendMessage(ChatColor.YELLOW + "A friend request was sent to " + requestedPlayer.getName() + "!");
				return true;
			}
		}
		else {
			List<String> requestingPlayerFriends = getFriendsList(requestingPlayer);
			requestingPlayerFriends.add(requestedPlayerUuid);
			PlayerConfigurator.setFriendsList(requestingPlayer, requestingPlayerFriends);
			requestingPlayer.sendMessage(ChatColor.GREEN + "You are now friends with " + requestedPlayer.getName() + "!");
			
			List<String> requestedPlayerFriends = getFriendsList(requestedPlayer);
			requestedPlayerFriends.add(requestingPlayerUuid);
			PlayerConfigurator.setFriendsList(requestedPlayer, requestedPlayerFriends);
			if(requestedOnlinePlayer != null) {
				requestedOnlinePlayer.sendMessage(ChatColor.GREEN + "You are now friends with " + requestingPlayer.getName() + "!");
			}
			requestMap.remove(requestedPlayer.getUniqueId().toString());
			return true;
		}
	}
	
	/**
	 * Removes a friend from the player's friend list.
	 * Also tries to send a notification to the unfriended player.
	 * 
	 * @param player The player who wants to remove a friend.
	 * @param friend The friend that is removed.
	 * 
	 * @see WauzPlayerFriends#getFriendsList(OfflinePlayer)
	 * @see PlayerConfigurator#setFriendsList(OfflinePlayer, List)
	 */
	public static void removeFriend(Player player, OfflinePlayer friend) {
		String playerUuid = player.getUniqueId().toString();
		String friendUuid = friend.getUniqueId().toString();
		
		List<String> playerFriends = getFriendsList(player);
		playerFriends.remove(friendUuid);
		PlayerConfigurator.setFriendsList(player, playerFriends);
		
		List<String> friendFriends = getFriendsList(friend);
		friendFriends.remove(playerUuid);
		PlayerConfigurator.setFriendsList(friend, friendFriends);
		
		player.sendMessage(ChatColor.YELLOW + "You unfriended " + friend.getName() + "!");
		Player onlineFriend = friend.getPlayer();
		if(onlineFriend != null) {
			onlineFriend.sendMessage(ChatColor.YELLOW + player.getName() + " unfriended you!");
		}
	}
	
	/**
	 * Gets the number of friends from a player.
	 * 
	 * @param player The player to get the friends from.
	 * 
	 * @return The number of friends.
	 * 
	 * @see WauzPlayerFriends#getFriendsList(OfflinePlayer)
	 */
	public static int getFriendCount(OfflinePlayer player) {
		return getFriendsList(player).size();
	}
	
	/**
	 * Gets the list of uuids from the player's friends.
	 * 
	 * @param player The player to get the friends from.
	 * 
	 * @return The list of friend uuids.
	 * 
	 * @see PlayerConfigurator#getFriendsList(OfflinePlayer)
	 */
	public static List<String> getFriendsList(OfflinePlayer player) {
		return PlayerConfigurator.getFriendsList(player);
	}
	
}
