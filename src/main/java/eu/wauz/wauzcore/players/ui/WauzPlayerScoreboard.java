package eu.wauz.wauzcore.players.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.instances.InstanceMobArena;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.instances.WauzInstanceType;
import eu.wauz.wauzcore.system.quests.QuestRequirementChecker;
import eu.wauz.wauzcore.system.quests.WauzQuest;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An UI class to show a player quests and the like in the sidebar, based on their current world.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerTablist
 */
public class WauzPlayerScoreboard {
	
	/**
	 * Schedules a task to update the sidebar of the player, based on the world they are in.
	 * The scoreboard will be refeshed in 0.5 seconds from then.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @see WauzPlayerScoreboard#getHubBoard(Player)
	 * @see WauzPlayerScoreboard#getDungeonBoard(Player)
	 * @see WauzPlayerScoreboard#getQuestBoard(Player)
	 * @see WauzPlayerScoreboard#getSurvivalBoard(Player)
	 * @see WauzPlayerTablist#createAndShow()
	 */
	public static void scheduleScoreboardRefresh(final Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
            	String worldName = player.getWorld().getName();
            	Scoreboard scoreboard = null;
            	
            	if(WauzMode.isMMORPG(player)) {
            		if(worldName.startsWith("Hub")) {
            			scoreboard = getHubBoard(player);
            		}
            		else if(worldName.startsWith("WzInstance")) {
            			scoreboard = getDungeonBoard(player);
            		}
            		else {
            			scoreboard = getQuestBoard(player);
            		}
            	}
            	else if(WauzMode.isSurvival(player)) {
            		scoreboard = getSurvivalBoard(player);
            	}
            	else {
            		scoreboard = getHubBoard(player);
            	}
            	new WauzPlayerTablist(player, scoreboard).createAndShow();
            }
		}, 10);
	}
	
	/**
	 * Creates a hub sidebar with the ip, aswell as their rank and tokens to a player.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @return The created scoreboard.
	 */
	private static Scoreboard getHubBoard(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard hubBoard = scoreboardManager.getNewScoreboard();
		Objective objective = hubBoard.registerNewObjective("row", "dummy", "displayName");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("-*-*-*-" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "DELSEYRIA" + ChatColor.RESET + "-*-*-*-");
		List<String> rowStrings = new ArrayList<>();
		
		rowStrings.add("");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "The Minecraft MMORPG");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "IP: play.wauz.eu");
		rowStrings.add(" ");
		WauzRank rank = WauzRank.getRank(player);
		String rankTitle = rank.getRankPrefix();
		rankTitle = StringUtils.isNotBlank(rankTitle) ? rankTitle : (rank.getRankColor() + rank.getRankName());
		rowStrings.add("Rank: " + rankTitle);
		rowStrings.add("Tokens: " + ChatColor.GOLD + Formatters.INT.format(PlayerConfigurator.getTokens(player)));
		
		for(int index =  0; index != rowStrings.size(); index++) {
			Score score = objective.getScore(rowStrings.get(index));
			score.setScore(rowStrings.size() - index);
		}
		return hubBoard;
	}
	
	/**
	 * Creates a survival sidebar with the ip, season, commands, aswell as their score and tokens to a player.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @return The created scoreboard.
	 */
	private static Scoreboard getSurvivalBoard(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard survivalBoard = scoreboardManager.getNewScoreboard();
		Objective objective = survivalBoard.registerNewObjective("row", "dummy", "displayName");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("-*-*-*-" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "DELSEYRIA" + ChatColor.RESET + "-*-*-*-");
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
		return survivalBoard;
	}
	
	/**
	 * Creates a dungeon sidebar with the name and status of the instance to a player.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @return The created scoreboard.
	 */
	private static Scoreboard getDungeonBoard(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard dungeonBoard = scoreboardManager.getNewScoreboard();
		Objective objective = dungeonBoard.registerNewObjective("row", "dummy", "displayName");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("-*-*-*-" + ChatColor.DARK_PURPLE + ChatColor.BOLD + ChatColor.UNDERLINE + "DUNGEON" + ChatColor.RESET + "-*-*-*-");
		List<String> rowStrings = new ArrayList<>();
		
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(player);
		rowStrings.add("");
		rowStrings.add(ChatColor.WHITE + instance.getInstanceName());
		
		WauzInstanceType instanceType = instance.getType();
		if(instanceType.equals(WauzInstanceType.KEYS)) {
			rowStrings.add(" ");
			rowStrings.add(ChatColor.DARK_AQUA + UnicodeUtils.ICON_BULLET + " Dungeon Keys");
			for(String keyId : instance.getKeyIds()) {
				rowStrings.add(ChatColor.WHITE + "  > " + keyId + ": " + instance.getKeyStatus(keyId).toString());
			}
		}
		else if(instanceType.equals(WauzInstanceType.ARENA)) {
			rowStrings.add(" ");
			rowStrings.add(ChatColor.DARK_AQUA + UnicodeUtils.ICON_BULLET + " Arena Progress");
			InstanceMobArena arena = instance.getMobArena();
			String currentWave = ChatColor.GOLD + "" + arena.getCurrentWave();
			String maximumWave = ChatColor.GOLD + "" + arena.getMaximumWave();
			rowStrings.add(ChatColor.WHITE + "  > Wave: " + currentWave + ChatColor.WHITE + " / " + maximumWave);
			rowStrings.add(ChatColor.WHITE + "  > Remaining Enemies: " + ChatColor.RED + arena.getMobsLeft());
			long medals = PlayerConfigurator.getCharacterMedals(player);
			rowStrings.add(ChatColor.WHITE + "  > Earned Medals: " + ChatColor.AQUA + medals);
		}
		
		for(int index =  0; index != rowStrings.size(); index++) {
			Score score = objective.getScore(rowStrings.get(index));
			score.setScore(rowStrings.size() - index);
		}
		return dungeonBoard;
	}
	
	/**
	 * Creates a quest sidebar with all running quests and their objectives to a player.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @return The created scoreboard.
	 * 
	 * @see WauzPlayerScoreboard#generateQuestObjectiveList(Player, String, String, int, ChatColor)
	 */
	private static Scoreboard getQuestBoard(Player player) {
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
		return questBoard;
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
	
}
