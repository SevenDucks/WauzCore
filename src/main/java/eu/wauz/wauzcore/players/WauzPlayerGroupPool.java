package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

/**
 * A static cache of player groups.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerGroup
 */
public class WauzPlayerGroupPool {
	
	/**
	 * A map of cached player groups by uuid.
	 */
	private static HashMap<String, WauzPlayerGroup> storage = new HashMap<String, WauzPlayerGroup>();
	
	/**
	 * Fetches a cached player group.
	 * 
	 * @param player A player that is part of the player group.
	 * 
	 * @return The requested player group.
	 */
	public static WauzPlayerGroup getGroup(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || StringUtils.isBlank(playerData.getGroupUuidString())) {
			return null;
		}
		else {
			return getGroup(playerData.getGroupUuidString());
		}
	}
	
	/**
	 * Fetches a cached player group.
	 * 
	 * @param groupUuidString The group uuid.
	 * 
	 * @return The requested player group.
	 */
	public static WauzPlayerGroup getGroup(String groupUuidString) {
		return storage.get(groupUuidString);
	}
	
	/**
	 * Lists all groups for the group overview menu.
	 * 
	 * @return A list of all gorups.
	 */
	public static List<WauzPlayerGroup> getGroups() {
		return new ArrayList<>(storage.values());
	}
	
	/**
	 * Registers a group to save it inthe group cache.
	 * 
	 * @param wauzPlayerGroup The group to register.
	 * 
	 * @return The registered player group.
	 */
	public static WauzPlayerGroup regGroup(WauzPlayerGroup wauzPlayerGroup) {
		storage.put(wauzPlayerGroup.getGroupUuidString(), wauzPlayerGroup);
		return getGroup(wauzPlayerGroup.getGroupUuidString());
	}
	
	/**
	 * Removes a player group for a group that was deleted.
	 * 
	 * @param groupUuidString The uuid of the group to remove.
	 */
	public static void unregGroup(String groupUuidString) {
		storage.remove(groupUuidString);
	}

}
