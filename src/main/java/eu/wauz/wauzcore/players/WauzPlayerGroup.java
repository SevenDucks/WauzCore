package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerGroup {
	
	public WauzPlayerGroup(Player leader) {
		groupUuidString = UUID.randomUUID().toString();
		adminUuidString = leader.getUniqueId().toString();
		wauzMode = WauzMode.getMode(leader.getWorld().getName()).toString();
		players.add(leader);
	}

	String groupUuidString;
	
	String adminUuidString;
	
	String wauzMode;
	
	List<Player> players = new ArrayList<>();
	
	String groupPassword;
	
	String groupDescription = "No Description";

	public String getGroupUuidString() {
		return groupUuidString;
	}

	public void setGroupUuidString(String groupUuidString) {
		this.groupUuidString = groupUuidString;
	}

	public String getAdminUuidString() {
		return adminUuidString;
	}

	public void setAdminUuidString(String adminUuidString) {
		this.adminUuidString = adminUuidString;
	}
	
	public boolean isGroupAdmin(Player player) {
		return adminUuidString.equals(player.getUniqueId().toString());
	}

	public String getWauzMode() {
		return wauzMode;
	}

	public void setWauzMode(String wauzMode) {
		this.wauzMode = wauzMode;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public int getPlayerAmount() {
		return players.size();
	}
	
	public boolean isFull() {
		return getPlayerAmount() >= 5;
	}
	
	public boolean isEmpty() {
		return getPlayerAmount() == 0;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		for(Player member : players)
			member.sendMessage(ChatColor.GREEN + player.getName() + " joined your Group!");
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
		player.sendMessage(ChatColor.RED + "You left the Group!");
		
		for(Player member : players)
			member.sendMessage(ChatColor.RED + player.getName() + " left your Group!");
		
		if(isEmpty()) {
			WauzPlayerGroupPool.unregGroup(groupUuidString);
		}
		else if(isGroupAdmin(player)) {
			Player leader = players.get(0);
			adminUuidString = leader.getUniqueId().toString();
			for(Player member : players)
				member.sendMessage(ChatColor.GREEN + leader.getName() + " is now the Group-Leader!");
		}
	}

	public String getGroupPassword() {
		return groupPassword;
	}

	public void setGroupPassword(String groupPassword) {
		this.groupPassword = groupPassword;
	}
	
	public boolean isPasswordProtected() {
		return groupPassword != null;
	}

	public String getGroupDescription() {
		return groupDescription;
	}
	
	public String[] getWrappedGroupDescription() {
		String doubleParagraph = ChatFormatter.ICON_PARAGRAPH + ChatFormatter.ICON_PARAGRAPH;
		return WordUtils.wrap(groupDescription, 42, doubleParagraph, true).split(doubleParagraph);
	}

	public void setGroupDescription(Player player, String groupDescription) {
		this.groupDescription = groupDescription;
		for(Player member : players)
			member.sendMessage(ChatColor.GREEN + player.getName() + " set the group description to: " + groupDescription);
	}
	
}
