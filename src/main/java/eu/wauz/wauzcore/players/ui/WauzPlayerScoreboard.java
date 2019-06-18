package eu.wauz.wauzcore.players.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.menu.util.QuestRequirementChecker;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.WauzQuest;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerScoreboard {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###");
	
	public static void scheduleScoreboard(final Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
            	String worldName = player.getWorld().getName();
            	
            	if(WauzMode.isMMORPG(player)) {
            		if(worldName.startsWith("Hub"))
            			scoreboardHub(player);
            		else if(worldName.startsWith("WzInstance"))
            			scoreboardDungeon(player);
            		else
            			scoreboardQuests(player);
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
		rowStrings.add("Tokens: " + ChatColor.GOLD + formatter.format(PlayerConfigurator.getTokens(player)));
		
		for(int index =  0; index != rowStrings.size(); index++) {
			Score score = objective.getScore(rowStrings.get(index));
			score.setScore(rowStrings.size() - index);
		}
		
		addScoreboardTeams(player, hubBoard);
		player.setScoreboard(hubBoard);
	}
	
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
		rowStrings.add("Survival Score: " + ChatColor.AQUA + formatter.format(PlayerConfigurator.getSurvivalScore(player)));
		rowStrings.add("One point and a free Token");
		rowStrings.add("for each Level beyond 30");
		rowStrings.add("  ");
		rowStrings.add("Tokens: " + ChatColor.GOLD + formatter.format(PlayerConfigurator.getTokens(player)));
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
			rowStrings.add(ChatColor.DARK_AQUA + "• Dungeon Keys");
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
				int phase = PlayerConfigurator.getCharacterQuestPhase(player, slotm);
				rowStrings.addAll(generateQuestObjectiveList(player, "", slotm, phase, ChatColor.DARK_PURPLE));
			}
			
			if(!cmpn1.equals("none")) {
				int phase = PlayerConfigurator.getCharacterQuestPhase(player, cmpn1);
				rowStrings.addAll(generateQuestObjectiveList(player, "    ", cmpn1, phase, ChatColor.DARK_AQUA));
			}
			
			if(!cmpn2.equals("none")) {
				int phase = PlayerConfigurator.getCharacterQuestPhase(player, cmpn2);
				rowStrings.addAll(generateQuestObjectiveList(player, "     ", cmpn2, phase, ChatColor.DARK_AQUA));
			}
		}
		
		if(!slot1.equals("none")) {
			int phase = PlayerConfigurator.getCharacterQuestPhase(player, slot1);
			rowStrings.addAll(generateQuestObjectiveList(player, " ", slot1, phase, ChatColor.GOLD));
		}
		
		if(!slot2.equals("none")) {
			int phase = PlayerConfigurator.getCharacterQuestPhase(player, slot2);
			rowStrings.addAll(generateQuestObjectiveList(player, "  ", slot2, phase, ChatColor.GOLD));
		}
		
		if(!slot3.equals("none")) {
			int phase = PlayerConfigurator.getCharacterQuestPhase(player, slot3);
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
	
	
	private static List<String> generateQuestObjectiveList(Player player, String questMargin, String questName, int questPhase, ChatColor questColor) {
		WauzQuest quest = WauzQuest.getQuest(questName);
		return new QuestRequirementChecker(player, quest, questPhase).getObjectiveLores(questMargin, questColor);
	}
	
	private static void addScoreboardTeams(Player player, Scoreboard scoreboard) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			try {
				Team team = scoreboard.getTeam(online.getName());
				if(team == null)
					team = scoreboard.registerNewTeam(online.getName());
				
				if(online.hasPermission("wauz.system")) {
					team.setPrefix(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ADMIN ");
					team.setColor(org.bukkit.ChatColor.GOLD);
				}
				else {
					team.setColor(org.bukkit.ChatColor.GREEN);
				}
				
				WauzPlayerData pd = WauzPlayerDataPool.getPlayer(online);
				if(pd == null) {
					team.setSuffix(ChatColor.RED + " " + online.getHealth() + " / 20 " + ChatFormatter.ICON_HEART);
				}
				else {
					team.setSuffix(ChatColor.RED + " " + pd.getHealth() + " / " + pd.getMaxHealth() + " " + ChatFormatter.ICON_HEART);
				}
				
				WauzPlayerData ownPd = WauzPlayerDataPool.getPlayer(player);
				if(ownPd.isInGroup() && pd.isInGroup() && StringUtils.equals(ownPd.getGroupUuidString(), pd.getGroupUuidString())) {
					team.setPrefix(ChatColor.BLUE + "GROUP ");
				}
				else {
					WauzPlayerGuild ownPg = PlayerConfigurator.getGuild(player);
					WauzPlayerGuild pg = PlayerConfigurator.getGuild(online);
					if(ownPg != null && pg != null && StringUtils.equals(ownPg.getGuildUuidString(), pg.getGuildUuidString())) {
						team.setPrefix(ChatColor.GREEN + "GUILD ");
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
