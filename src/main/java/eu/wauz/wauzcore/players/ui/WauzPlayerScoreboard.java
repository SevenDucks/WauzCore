package eu.wauz.wauzcore.players.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.WauzQuest;
import eu.wauz.wauzcore.system.quests.QuestRequirementChecker;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An UI class to show a player quests and the like in the sidebar, based on their current world.
 * 
 * @author Wauzmons
 */
public class WauzPlayerScoreboard {
	
	/**
	 * Schedules a task to update the sidebar of the player, based on the world they are in.
	 * The scoreboard will be refeshed in 0.5 seconds from then.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @see WauzPlayerScoreboard#scoreboardHub(Player)
	 * @see WauzPlayerScoreboard#scoreboardDungeon(Player)
	 * @see WauzPlayerScoreboard#scoreboardQuests(Player)
	 * @see WauzPlayerScoreboard#scoreboardSurvival(Player)
	 */
	public static void scheduleScoreboard(final Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
            	String worldName = player.getWorld().getName();
            	
            	if(WauzMode.isMMORPG(player)) {
            		if(worldName.startsWith("Hub")) {
            			scoreboardHub(player);
            		}
            		else if(worldName.startsWith("WzInstance")) {
            			scoreboardDungeon(player);
            		}
            		else {
            			scoreboardQuests(player);
            		}
            	}
            	else if(WauzMode.isSurvival(player)) {
            		scoreboardSurvival(player);
            	}
            	else {
            		scoreboardHub(player);
            	}
            }
		}, 10);
	}
	
	/**
	 * Shows a hub sidebar with the ip, aswell as their rank and tokens to a player.
	 * Also adds online player team prefixes to the scoreboard.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @see WauzPlayerScoreboard#addScoreboardTeams(Player, Scoreboard)
	 */
	private static void scoreboardHub(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard hubBoard = scoreboardManager.getNewScoreboard();
		Objective objective = hubBoard.registerNewObjective("row", "dummy", "displayName");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("-*-*-*-" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "WAUZLAND" + ChatColor.RESET + "-*-*-*-");
		List<String> rowStrings = new ArrayList<>();
		
		rowStrings.add("");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "The Minecraft MMORPG");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "IP: play.wauz.eu");
		rowStrings.add(" ");
		rowStrings.add("Rank: " + ChatColor.GREEN + PlayerConfigurator.getRank(player));
		rowStrings.add("Tokens: " + ChatColor.GOLD + Formatters.INT.format(PlayerConfigurator.getTokens(player)));
		
		for(int index =  0; index != rowStrings.size(); index++) {
			Score score = objective.getScore(rowStrings.get(index));
			score.setScore(rowStrings.size() - index);
		}
		
		addScoreboardTeams(player, hubBoard);
		player.setScoreboard(hubBoard);
	}
	
	/**
	 * Shows a survival sidebar with the ip, season, commands, aswell as their score and tokens to a player.
	 * Also adds online player team prefixes to the scoreboard.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @see WauzPlayerScoreboard#addScoreboardTeams(Player, Scoreboard)
	 */
	private static void scoreboardSurvival(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard survivalBoard = scoreboardManager.getNewScoreboard();
		Objective objective = survivalBoard.registerNewObjective("row", "dummy", "displayName");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("-*-*-*-" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "WAUZLAND" + ChatColor.RESET + "-*-*-*-");
		List<String> rowStrings = new ArrayList<>();
		
		rowStrings.add("");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "Survival Season " + WauzDateUtils.getSurvivalSeason());
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "IP: play.wauz.eu");
		rowStrings.add(" ");
		rowStrings.add("Survival Score: " + ChatColor.AQUA + Formatters.INT.format(PlayerConfigurator.getSurvivalScore(player)));
		rowStrings.add("One point and a free Token");
		rowStrings.add("for each Level beyond 30");
		rowStrings.add("  ");
		rowStrings.add("Tokens: " + ChatColor.GOLD + Formatters.INT.format(PlayerConfigurator.getTokens(player)));
		rowStrings.add("Use an Ender Chest to spend");
		rowStrings.add("   ");
		rowStrings.add(ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "hub " + ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "spawn");
		rowStrings.add(ChatColor.RED + "/" + ChatColor.WHITE + "home " + ChatColor.RED + "/" + ChatColor.WHITE + "sethome");
		rowStrings.add(ChatColor.RED + "/" + ChatColor.WHITE + "group");
		
		for(int index =  0; index != rowStrings.size(); index++) {
			Score score = objective.getScore(rowStrings.get(index));
			score.setScore(rowStrings.size() - index);
		}
		
		addScoreboardTeams(player, survivalBoard);
		player.setScoreboard(survivalBoard);
	}
	
	/**
	 * Shows a dungeon sidebar with the name and keys of the instance to a player.
	 * Also adds online player team prefixes to the scoreboard.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @see WauzPlayerScoreboard#addScoreboardTeams(Player, Scoreboard)
	 */
	private static void scoreboardDungeon(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard dungeonBoard = scoreboardManager.getNewScoreboard();
		Objective objective = dungeonBoard.registerNewObjective("row", "dummy", "displayName");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("-*-*-*-" + ChatColor.DARK_PURPLE + ChatColor.BOLD + ChatColor.UNDERLINE + "DUNGEON" + ChatColor.RESET + "-*-*-*-");
		List<String> rowStrings = new ArrayList<>();
		
		World world = player.getWorld();
		rowStrings.add("");
		rowStrings.add(ChatColor.WHITE + InstanceConfigurator.getInstanceWorldName(world));
		
		String instanceType = InstanceConfigurator.getInstanceWorldType(world);
		if(instanceType.equals("Keys")) {
			rowStrings.add(" ");
			rowStrings.add(ChatColor.DARK_AQUA + UnicodeUtils.ICON_BULLET + " Dungeon Keys");
			for(String keyId : InstanceConfigurator.getInstanceWorldKeyIds(world))
				rowStrings.add(ChatColor.WHITE + "  > " + keyId + ": " + InstanceConfigurator.getInstanceKeyStatus(world, keyId));
		}
		
		for(int index =  0; index != rowStrings.size(); index++) {
			Score score = objective.getScore(rowStrings.get(index));
			score.setScore(rowStrings.size() - index);
		}
		
		addScoreboardTeams(player, dungeonBoard);
		player.setScoreboard(dungeonBoard);
	}
	
	/**
	 * Shows a quest sidebar with all running quests and their objectives to a player.
	 * Also adds online player team prefixes to the scoreboard.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @see WauzPlayerScoreboard#generateQuestObjectiveList(Player, String, String, int, ChatColor)
	 * @see WauzPlayerScoreboard#addScoreboardTeams(Player, Scoreboard)
	 */
	private static void scoreboardQuests(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard questBoard = scoreboardManager.getNewScoreboard();
		Objective objective = questBoard.registerNewObjective("row", "dummy", "displayName");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("-*-*-*-" + ChatColor.DARK_RED + ChatColor.BOLD + ChatColor.UNDERLINE + "QUESTS" + ChatColor.RESET + "-*-*-*-");
		List<String> rowStrings = new ArrayList<>();
		
		String slotm = PlayerConfigurator.getCharacterRunningMainQuest(player);
		String cmpn1 = PlayerConfigurator.getCharacterRunningCampaignQuest1(player);
		String cmpn2 = PlayerConfigurator.getCharacterRunningCampaignQuest2(player);
		String slot1 = PlayerConfigurator.getCharacterRunningDailyQuest1(player);
		String slot2 = PlayerConfigurator.getCharacterRunningDailyQuest2(player);
		String slot3 = PlayerConfigurator.getCharacterRunningDailyQuest3(player);
		
		if(!PlayerConfigurator.getHideSpecialQuestsForCharacter(player)) {
			if(!slotm.equals("none")) {
				int phase = PlayerQuestConfigurator.getQuestPhase(player, slotm);
				rowStrings.addAll(generateQuestObjectiveList(player, "", slotm, phase, ChatColor.DARK_PURPLE));
			}
			
			if(!cmpn1.equals("none")) {
				int phase = PlayerQuestConfigurator.getQuestPhase(player, cmpn1);
				rowStrings.addAll(generateQuestObjectiveList(player, "    ", cmpn1, phase, ChatColor.DARK_AQUA));
			}
			
			if(!cmpn2.equals("none")) {
				int phase = PlayerQuestConfigurator.getQuestPhase(player, cmpn2);
				rowStrings.addAll(generateQuestObjectiveList(player, "     ", cmpn2, phase, ChatColor.DARK_AQUA));
			}
		}
		
		if(!slot1.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot1);
			rowStrings.addAll(generateQuestObjectiveList(player, " ", slot1, phase, ChatColor.GOLD));
		}
		
		if(!slot2.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot2);
			rowStrings.addAll(generateQuestObjectiveList(player, "  ", slot2, phase, ChatColor.GOLD));
		}
		
		if(!slot3.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot3);
			rowStrings.addAll(generateQuestObjectiveList(player, "   ", slot3, phase, ChatColor.GOLD));
		}
		
		for(int index = 0; index != rowStrings.size(); index++) {
			Score score = objective.getScore(rowStrings.get(index));
			score.setScore(rowStrings.size() - index);
		}
		
		if(rowStrings.isEmpty()) {
			Score score = objective.getScore(ChatColor.GRAY + "(None)");
			score.setScore(1);
		}
		
		addScoreboardTeams(player, questBoard);
		player.setScoreboard(questBoard);
	}
	
	
	/**
	 * Generates a list of a quest and its objectives, to show in the sidebar.
	 * 
	 * @param player The player who is doing the quest.
	 * @param questMargin The empty space above the title.
	 * @param questName The name of the quest.
	 * @param questPhase The phase of the quest.
	 * @param questColor The color of the quest type.
	 * 
	 * @return The list of quest objectives.
	 */
	private static List<String> generateQuestObjectiveList(Player player, String questMargin, String questName, int questPhase, ChatColor questColor) {
		WauzQuest quest = WauzQuest.getQuest(questName);
		return new QuestRequirementChecker(player, quest, questPhase).getObjectiveLores(questMargin, questColor);
	}
	
	/**
	 * Adds prefixes to the online players listed on the scoreboard, by creating new teams.
	 * Following prefixes are possible, ordered by priority: GROUP, GUILD, ADMIN.
	 * There will be also a suffix added to each team, to show the player's health.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * @param scoreboard The scoreboard that should receive the player teams.
	 */
	private static void addScoreboardTeams(Player player, Scoreboard scoreboard) {
		WauzPlayerData ownData = WauzPlayerDataPool.getPlayer(player);
		if(ownData == null) {
			return;
		}
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			try {
				Team team = scoreboard.getTeam(online.getName());
				if(team == null) {
					team = scoreboard.registerNewTeam(online.getName());
				}
				
				if(online.hasPermission(WauzPermission.SYSTEM.toString())) {
					team.setPrefix(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ADMIN ");
					team.setColor(org.bukkit.ChatColor.GOLD);
				}
				else {
					team.setColor(org.bukkit.ChatColor.GREEN);
				}
				
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(online);
				if(playerData == null) {
					team.setSuffix(ChatColor.RED + " " + ((int) online.getHealth()) + " / 20 " + UnicodeUtils.ICON_HEART);
				}
				else {
					if(WauzMode.isSurvival(player)) {
						team.setSuffix(ChatColor.RED + " " + ((int) online.getHealth()) + " / 20 " + UnicodeUtils.ICON_HEART);
					}
					else {
						team.setSuffix(ChatColor.RED + " " + playerData.getHealth() + " / " + playerData.getMaxHealth() + " " + UnicodeUtils.ICON_HEART);
					}
					
					if(!Objects.equals(ownData, playerData)) {
						if(ownData.isInGroup() && playerData.isInGroup() && StringUtils.equals(ownData.getGroupUuidString(), playerData.getGroupUuidString())) {
							team.setPrefix(ChatColor.BLUE + "GROUP ");
						}
						else {
							WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(online);
							WauzPlayerGuild ownGuild = PlayerConfigurator.getGuild(player);
							if(ownGuild != null && playerGuild != null && StringUtils.equals(ownGuild.getGuildUuidString(), playerGuild.getGuildUuidString())) {
								team.setPrefix(ChatColor.GREEN + "GUILD ");
							}
						}
					}
				}
				team.addEntry(online.getName());
			}
			catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

}
