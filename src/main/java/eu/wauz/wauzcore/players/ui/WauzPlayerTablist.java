package eu.wauz.wauzcore.players.ui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.WauzTitle;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

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
	 * The 1st scoreboard objective for showing the health of players.
	 */
	private Objective healthObjective1;
	
	/**
	 * The 2nd scoreboard objective for showing the health of players.
	 */
	private Objective healthObjective2;
	
	/**
	 * The prefix to apply to the next created team.
	 */
	private String teamPrefix;
	
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
		
		healthObjective1 = registerHealthObjective("health1", DisplaySlot.PLAYER_LIST);
		healthObjective2 = registerHealthObjective("health2", DisplaySlot.BELOW_NAME);
	}
	
	/**
	 * Registers a new health objective with the given name.
	 * 
	 * @param name The name of the objective.
	 * @param slot The display slot of the objective.
	 * 
	 * @return The created objective.
	 */
	private Objective registerHealthObjective(String name, DisplaySlot slot) {
		Objective healthObjective = scoreboard.registerNewObjective(name, "health", ChatColor.RED + UnicodeUtils.ICON_HEART);
		healthObjective.setDisplaySlot(slot);
		healthObjective.setRenderType(RenderType.HEARTS);
		return healthObjective;
	}
	
	/**
	 * Creates and shows the tablist.
	 * 
	 * @see WauzPlayerTablist#addGroupEntries()
	 * @see WauzPlayerTablist#addGuildEntries()
	 * @see WauzPlayerTablist#addFriendsEntries()
	 * @see WauzPlayerTablist#addRemainingEntries()
	 */
	public void createAndShow() {
		teamPrefix = ChatColor.LIGHT_PURPLE + "YOU ";
		addEntry(player);
		
		addGroupEntries();
		addGuildEntries();
		addFriendsEntries();
		addRemainingEntries();
		player.setPlayerListHeader(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Delseyria" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Reborn");
		player.setPlayerListFooter(ChatColor.LIGHT_PURPLE + "" + ChatColor.AQUA + "----------" + WauzDateUtils.getServerTime() + "----------");
		player.setScoreboard(scoreboard);
	}
	
	/**
	 * Adds all group members to the tablist.
	 * 
	 * @see WauzPlayerTablist#addEntry(Player)
	 */
	private void addGroupEntries() {
		teamPrefix = ChatColor.BLUE + "GRP ";
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || !playerData.isInGroup()) {
			return;
		}
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getGroupUuidString());
		for(Player member : playerGroup.getPlayers()) {
			if(player != member) {
				addEntry(member);
			}
		}
	}
	
	/**
	 * Adds all guild members to the tablist.
	 * 
	 * @see WauzPlayerTablist#addEntry(Player)
	 */
	private void addGuildEntries() {
		teamPrefix = ChatColor.GREEN + "GLD ";
		
		WauzPlayerGuild guild = PlayerConfigurator.getGuild(player);
		if(guild == null) {
			return;
		}
		for(String memberUuid : guild.getMemberUuidStrings()) {
			Player member = Bukkit.getPlayer(UUID.fromString(memberUuid));
			if(member == null) {
				continue;
			}
			Team team = scoreboard.getEntryTeam(member.getName());
			if(team == null) {
				addEntry(member);
			}
		}
	}
	
	/**
	 * Adds all friends of the player to the tablist.
	 * 
	 * @see WauzPlayerTablist#addEntry(Player)
	 */
	private void addFriendsEntries() {
		teamPrefix = ChatColor.YELLOW + "FRN ";
		
		for(String friendUuid : PlayerConfigurator.getFriendsList(player)) {
			Player friend = Bukkit.getPlayer(UUID.fromString(friendUuid));
			if(friend == null) {
				continue;
			}
			Team team = scoreboard.getEntryTeam(friend.getName());
			if(team == null) {
				addEntry(friend);
			}
		}
	}
	
	/**
	 * Adds all players to the list, that aren't already shown.
	 * 
	 * @see WauzPlayerTablist#addEntry(Player)
	 */
	private void addRemainingEntries() {
		teamPrefix = ChatColor.GRAY + "/// ";
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			Team team = scoreboard.getEntryTeam(player.getName());
			if(team == null) {
				addEntry(player);
			}
		}
	}
	
	/**
	 * Adds a player entry to the player tablist.
	 * 
	 * @param player The player to add as entry.
	 * 
	 * @see WauzPlayerTablist#generatePrefix(Team, Player)
	 * @see WauzPlayerTablist#generateSuffix(Team, Player)
	 */
	private void addEntry(Player player) {
		Team team = scoreboard.registerNewTeam(getNextTeamId());
		generatePrefix(team, player);
		team.addEntry(player.getName());
		
		Score health1 = healthObjective1.getScore(player.getName());
		Score health2 = healthObjective2.getScore(player.getName());
		health1.setScore((int) player.getHealth());
		health2.setScore((int) player.getHealth());
	}
	
	/**
	 * Adds a prefix, showing the players rank or title, to the given team.
	 * 
	 * @param team The team to receive the prefix.
	 * @param player The player whose rank should be shown.
	 */
	private void generatePrefix(Team team, Player player) {
		WauzRank rank = WauzRank.getRank(player);
		String title = WauzTitle.getTitle(player);
		
		String rankPrefix;
		if(StringUtils.isNotBlank(title)) {
			rankPrefix = rank.getRankColor() + title + " ";
		}
		else {
			rankPrefix = StringUtils.isBlank(rank.getRankPrefix()) ? "" : rank.getRankPrefix() + " ";
		}
		team.setPrefix(teamPrefix + rankPrefix);
		team.setColor(rank.getRankColor());
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
