package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A player group to save session scoped group information.
 * 
 * @author Wauzmons
 */
public class WauzPlayerGroup {
	
	/**
	 * Creates a new group with given leader.
	 * 
	 * @param leader The leader of the group.
	 */
	public WauzPlayerGroup(Player leader) {
		groupUuidString = UUID.randomUUID().toString();
		adminUuidString = leader.getUniqueId().toString();
		wauzMode = WauzMode.getMode(leader.getWorld().getName()).toString();
		players.add(leader);
	}

	/**
	 * The uuid of the group.
	 */
	String groupUuidString;
	
	/**
	 * The uuid of the leader.
	 */
	String adminUuidString;
	
	/**
	 * The mode of this group's world.
	 */
	String wauzMode;
	
	/**
	 * The players inside the group.
	 */
	List<Player> players = new ArrayList<>();
	
	/**
	 * The password of the group,
	 */
	String groupPassword;
	
	/**
	 * The description of the group.
	 */
	String groupDescription = "No Description";

	/**
	 * @return The uuid of the group.
	 */
	public String getGroupUuidString() {
		return groupUuidString;
	}

	/**
	 * @param groupUuidString The new uuid of the group.
	 */
	public void setGroupUuidString(String groupUuidString) {
		this.groupUuidString = groupUuidString;
	}

	/**
	 * @return The uuid of the leader.
	 */
	public String getAdminUuidString() {
		return adminUuidString;
	}

	/**
	 * @param adminUuidString The new uuid of the leader.
	 */
	public void setAdminUuidString(String adminUuidString) {
		this.adminUuidString = adminUuidString;
	}
	
	/**
	 * @param player A group member.
	 * 
	 * @return If the given member is the leader.
	 */
	public boolean isGroupAdmin(Player player) {
		return adminUuidString.equals(player.getUniqueId().toString());
	}

	/**
	 * @return The mode of this group's world.
	 */
	public String getWauzMode() {
		return wauzMode;
	}

	/**
	 * @param wauzMode The new mode of this group's world.
	 */
	public void setWauzMode(String wauzMode) {
		this.wauzMode = wauzMode;
	}

	/**
	 * @return The players inside the group.
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players The new players inside the group.
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	/**
	 * @return The amount of players inside the group.
	 */
	public int getPlayerAmount() {
		return players.size();
	}
	
	/**
	 * @return If the group has 5 or more members.
	 */
	public boolean isFull() {
		return getPlayerAmount() >= 5;
	}
	
	/**
	 * @return If the group has 0 members.
	 */
	public boolean isEmpty() {
		return getPlayerAmount() == 0;
	}
	
	/**
	 * Adds a new player and announces it to the group members.
	 * 
	 * @param player The player to add.
	 */
	public void addPlayer(Player player) {
		players.add(player);
		for(Player member : players) {
			member.sendMessage(ChatColor.GREEN + player.getName() + " joined your Group!");
		}
	}
	
	/**
	 * Removes a player  and announces it to the group members.
	 * Unregisters the group or changes leader, if necessary.
	 * 
	 * @param player The player to remove.
	 */
	public void removePlayer(Player player) {
		players.remove(player);
		player.sendMessage(ChatColor.RED + "You left the Group!");
		
		for(Player member : players) {
			member.sendMessage(ChatColor.RED + player.getName() + " left your Group!");
		}
		
		if(isEmpty()) {
			WauzPlayerGroupPool.unregGroup(groupUuidString);
		}
		else if(isGroupAdmin(player)) {
			Player leader = players.get(0);
			adminUuidString = leader.getUniqueId().toString();
			for(Player member : players) {
				member.sendMessage(ChatColor.GREEN + leader.getName() + " is now the Group-Leader!");
			}
		}
	}

	/**
	 * @return The password of the group.
	 */
	public String getGroupPassword() {
		return groupPassword;
	}

	/**
	 * @param groupPassword The new password of the group.
	 */
	public void setGroupPassword(String groupPassword) {
		this.groupPassword = groupPassword;
	}
	
	/**
	 * @return If the group is password protected.
	 */
	public boolean isPasswordProtected() {
		return StringUtils.isBlank(groupPassword);
	}

	/**
	 * @return The description of the group.
	 */
	public String getGroupDescription() {
		return groupDescription;
	}
	
	/**
	 * @return The wrapped description of the group.
	 */
	public String[] getWrappedGroupDescription() {
		String doubleParagraph = UnicodeUtils.ICON_PARAGRAPH + UnicodeUtils.ICON_PARAGRAPH;
		return WordUtils.wrap(groupDescription, 42, doubleParagraph, true).split(doubleParagraph);
	}

	/**
	 * @param player The player that changed the description.
	 * @param groupDescription The new description of the group.
	 */
	public void setGroupDescription(Player player, String groupDescription) {
		this.groupDescription = groupDescription;
		for(Player member : players) {
			member.sendMessage(ChatColor.GREEN + player.getName() + " set the group description to: " + groupDescription);
		}
	}
	
}
