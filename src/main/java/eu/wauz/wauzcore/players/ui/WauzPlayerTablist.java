package eu.wauz.wauzcore.players.ui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An UI class to show your group, friends, health, rank and more in the tablist.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerScoreboard
 */
public class WauzPlayerTablist {
	
	/**
	 * The player who should view the tablist.
	 */
	private Player player;
	
	/**
	 * The scoreboard that should store the tablist.
	 */
	private Scoreboard scoreboard;
	
	/**
	 * The last used empty slot in the scoreboard.
	 */
	private String emptySlot = "";
	
	/**
	 * The last used team id in the scoreboard.
	 */
	private int teamId = 0;
	
	/**
	 * Creates a new player tablist instance.
	 * 
	 * @param player The player who should view the tablist.
	 * @param scoreboard The scoreboard that should store the tablist.
	 */
	public WauzPlayerTablist(Player player, Scoreboard scoreboard) {
		this.player = player;
		this.scoreboard = scoreboard;
	}
	
	/**
	 * Creates and shows the tablist.
	 * 
	 * @see WauzPlayerTablist#addGroupEntries()
	 * @see WauzPlayerTablist#addFriendsEntries()
	 * @see WauzPlayerTablist#addRemainingEntries()
	 */
	public void createAndShow() {
		addGroupEntries();
		addFriendsEntries();
		addRemainingEntries();
		String seperatorString = " " + ChatColor.WHITE + "|" + ChatColor.RESET + " ";
		String titleString = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Delseyria" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Reborn";
		String ipString = ChatColor.GREEN + "IP: play.wauz.eu";
		String webString = ChatColor.AQUA + "WEB: drpg.wauz.eu";
		String timeString = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Server Time: " + WauzDateUtils.getServerTime();
		player.setPlayerListHeader(titleString + seperatorString + ipString + seperatorString + webString);
		player.setPlayerListFooter("-~-~-~-" + seperatorString + timeString + seperatorString + "-~-~-~-");
		player.setScoreboard(scoreboard);
	}
	
	/**
	 * Adds all group members, including your, to the tablist.
	 * Also shows health displays for all members.
	 * 
	 * @see WauzPlayerTablist#addEntry(OfflinePlayer, boolean)
	 */
	private void addGroupEntries() {
		addEntry("GROUP", ChatColor.BLUE);
		int addedEntries = 1;
		
		addEntry(player, true);
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData != null && playerData.isInGroup()) {
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getGroupUuidString());
			for(Player member : playerGroup.getPlayers()) {
				if(player != member) {
					addEntry(member, true);
					addedEntries++;
				}
			}
		}
		while(addedEntries < 6) {
			addEntry(getNextEmptySlot(), ChatColor.WHITE);
		}
	}
	
	/**
	 * Adds all friends of the player to the tablist.
	 * If the friend list is not full, it is filled up with empty slots.
	 * 
	 * @see WauzPlayerTablist#addEntry(OfflinePlayer, boolean)
	 */
	private void addFriendsEntries() {
		addEntry("FRIENDS --------------------", ChatColor.YELLOW);
		int addedEntries = 1;
		
		for(String friendUuid : PlayerConfigurator.getFriendsList(player)) {
			OfflinePlayer friend = Bukkit.getOfflinePlayer(UUID.fromString(friendUuid));
			Team team = scoreboard.getEntryTeam(friend.getName());
			if(team != null) {
				addEntry("(" + addedEntries + ") In your Group", ChatColor.BLACK);
				return;
			}
			addEntry(friend, false);
			addedEntries++;
			if(addedEntries >= 14) {
				return;
			}
		}
		while(addedEntries < 14) {
			addEntry(getNextEmptySlot(), ChatColor.WHITE);
		}
	}
	
	/**
	 * Adds all players to the list, that aren't already shown.
	 * The maximum amount of visible players is 20.
	 * 
	 * @see WauzPlayerTablist#addEntry(OfflinePlayer, boolean)
	 */
	private void addRemainingEntries() {
		addEntry("ONLINE PLAYERS --------------------", ChatColor.LIGHT_PURPLE);
		int addedEntries = 1;
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			Team team = scoreboard.getEntryTeam(player.getName());
			if(team != null) {
				return;
			}
			addEntry(player, false);
			addedEntries++;
			if(addedEntries >= 20) {
				return;
			}
		}
		while(addedEntries < 20) {
			addEntry(getNextEmptySlot(), ChatColor.WHITE);
		}
	}
	
	/**
	 * Adds a custom entry to the player tablist.
	 * 
	 * @param entryName The name of the entry to add.
	 * @param color The color of the entry to add.
	 */
	private void addEntry(String entryName, ChatColor color) {
		Team team = scoreboard.registerNewTeam(getNextTeamId());
		team.setColor(color);
		team.addEntry(entryName);
	}
	
	/**
	 * Adds a player entry to the player tablist.
	 * 
	 * @param offlinePlayer The player to add as entry.
	 * @param showHealth If a health suffix should be shown.
	 * 
	 * @see WauzPlayerTablist#generatePrefix(Team, Player)
	 * @see WauzPlayerTablist#generateSuffix(Team, Player)
	 */
	private void addEntry(OfflinePlayer offlinePlayer, boolean showHealth) {
		Team team = scoreboard.registerNewTeam(getNextTeamId());
		Player player = offlinePlayer.getPlayer();
		generatePrefix(team, player, WauzPlayerGuild.doShareGuild(this.player, offlinePlayer, false));
		if(showHealth) {
			generateSuffix(team, player);
		}
	}
	
	/**
	 * Adds a prefix, showing the players rank or "offline", to the given team.
	 * 
	 * @param team The team to receive the prefix.
	 * @param player The player whose rank should be shown.
	 * @param sameGuild If the player is in the same guild.
	 */
	private void generatePrefix(Team team, Player player, boolean sameGuild) {
		String guildPrefix = sameGuild ? ChatColor.GREEN + "" + ChatColor.BOLD + "G " : "";
		if(player == null) {
			team.setPrefix(guildPrefix + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "OFF ");
			team.setColor(ChatColor.GRAY);
		}
		else if(player.hasPermission(WauzPermission.SYSTEM.toString())) {
			team.setPrefix(guildPrefix + ChatColor.DARK_RED + "" + ChatColor.BOLD + "ADMIN ");
			team.setColor(ChatColor.GOLD);
		}
		else {
			team.setPrefix(guildPrefix);
			team.setColor(ChatColor.GREEN);
		}
	}
	
	/**
	 * Adds a suffix, showing the player's health, to the given team.
	 * 
	 * @param team The team to receive the suffix.
	 * @param player The player whose health should be shown.
	 */
	private void generateSuffix(Team team, Player player) {
		if(player == null) {
			return;
		}
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || WauzMode.isSurvival(player)) {
			team.setSuffix(ChatColor.RED + " " + ((int) player.getHealth()) + " / 20 " + UnicodeUtils.ICON_HEART);
		}
		else {
			team.setSuffix(ChatColor.RED + " " + playerData.getHealth() + " / " + playerData.getMaxHealth() + " " + UnicodeUtils.ICON_HEART);
		}
	}
	
	/**
	 * Gets the next unqiue empty slot, by adding an additional space to the previous one.
	 * 
	 * @return The next empty slot.
	 */
	private String getNextEmptySlot() {
		return emptySlot += " ";
	}
	
	/**
	 * Gets the next unique team id, by increasing the previous one by 1.
	 * 
	 * @return The next team id.
	 */
	private String getNextTeamId() {
		return Formatters.INT_THREE.format(teamId += 1);
	}

}
