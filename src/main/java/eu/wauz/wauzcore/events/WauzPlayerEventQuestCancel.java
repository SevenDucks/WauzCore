package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzQuest;
import eu.wauz.wauzcore.system.quests.QuestSlot;
import eu.wauz.wauzcore.system.quests.QuestType;

/**
 * An event that lets a player cancel a running quest.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventQuestCancel implements WauzPlayerEvent {
	
	/**
	 * The name of the quest to cancel.
	 */
	private String questName;
	
	/**
	 * Creates an event to cancel the given quest.
	 * 
	 * @param questName The name of the quest to cancel.
	 */
	public WauzPlayerEventQuestCancel(String questName) {
		this.questName = questName;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see PlayerQuestConfigurator#setQuestPhase(Player, String, int)
	 * @see PlayerConfigurator#setCharacterQuestSlot(Player, String, String)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			WauzQuest quest = WauzQuest.getQuest(questName);
			
			String questSlot = null;
			String type = quest.getType();
			
			String slotm = PlayerConfigurator.getCharacterRunningMainQuest(player);
			String cmpn1 = PlayerConfigurator.getCharacterRunningCampaignQuest1(player);
			String cmpn2 = PlayerConfigurator.getCharacterRunningCampaignQuest2(player);
			String slot1 = PlayerConfigurator.getCharacterRunningDailyQuest1(player);
			String slot2 = PlayerConfigurator.getCharacterRunningDailyQuest2(player);
			String slot3 = PlayerConfigurator.getCharacterRunningDailyQuest3(player);
			
			if(type.equals(QuestType.MAIN)) {
				if(slotm.equals(questName)) {
					questSlot = QuestSlot.MAIN.getConfigKey();
				}
			}
			else if(type.equals(QuestType.CAMPAIGN)) {
				if(cmpn1.equals(questName)) {
					questSlot = QuestSlot.CAMPAIGN1.getConfigKey();
				}
				else if(cmpn2.equals(questName)) {
					questSlot = QuestSlot.CAMPAIGN2.getConfigKey();
				}
			}
			else if(type.equals(QuestType.DAILY)) {
				if(slot1.equals(questName)) {
					questSlot = QuestSlot.DAILY1.getConfigKey();
				}
				else if(slot2.equals(questName)) {
					questSlot = QuestSlot.DAILY2.getConfigKey();
				}
				else if(slot3.equals(questName)) {
					questSlot = QuestSlot.DAILY3.getConfigKey();
				}
			}
			
			PlayerQuestConfigurator.setQuestPhase(player, questName, 0);
			PlayerConfigurator.setCharacterQuestSlot(player, questSlot, "none");
			
			WauzPlayerScoreboard.scheduleScoreboard(player);
			player.sendMessage(ChatColor.DARK_PURPLE + "[" + quest.getDisplayName() + "] was canceled!");
			player.closeInventory();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while canceling your quest!");
			player.closeInventory();
			return false;
		}
	}

}
