package eu.wauz.wauzcore.players.ui.scoreboard;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.system.quests.QuestRequirementChecker;
import eu.wauz.wauzcore.system.quests.WauzQuest;

/**
 * A scoreboard to show a quest list with all running quests and their objectives to a player.
 * 
 * @author Wauzmons
 */
public class QuestScoreboard extends BaseScoreboard {
	
	/**
	 * Initializes the scoreboard and fills it with data.
	 * 
	 * @param The player who should receive the scoreboard.
	 */
	public QuestScoreboard(Player player) {
		super(player);
	}

	/**
	 * @return The text to show as scoreboard title.
	 */
	@Override
	public String getTitleText() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "Quests" + ChatColor.RESET;
	}

	/**
	 * Fills the scoreboard with entries for the given player
	 * 
	 * @param The player who should receive the scoreboard.
	 */
	@Override
	public void fillScoreboard(Player player) {
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
		
		if(rowStrings.isEmpty()) {
			rowStrings.add(ChatColor.GRAY + "(None)");
		}
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
		return QuestRequirementChecker.create(player, quest, questPhase).getObjectiveLores(questMargin, questColor);
	}

}
