package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerFriends;

/**
 * An event that lets a player remove a friend from their list.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventUnfriend implements WauzPlayerEvent {

	/**
	 * The friend that should get removed.
	 */
	private OfflinePlayer friend;
	
	/**
	 * Creates an event to let a player remove a friend from their list.
	 * 
	 * @param friend The friend that should get removed.
	 */
	public WauzPlayerEventUnfriend(OfflinePlayer friend) {
		this.friend = friend;
	}
	
	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see WauzPlayerFriends#removeFriend(Player, OfflinePlayer)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			WauzPlayerFriends.removeFriend(player, friend);
			player.closeInventory();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while unfriending the Player!");
			player.closeInventory();
			return false;
		}
	}

}
