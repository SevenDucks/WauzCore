package eu.wauz.wauzcore.system.quests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenTalk;
import eu.wauz.wauzcore.events.WauzPlayerEventQuestAccept;
import eu.wauz.wauzcore.events.WauzPlayerEventQuestComplete;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.collection.QuestRewardChooser;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * A class to manage quest progression of players.
 * 
 * @author Wauzmons
 */
public class QuestProcessor {
	
	/**
	 * Tries to accept or continue the quest.
	 * 
	 * @param player The player who is doing the quest.
	 * @param questName The name of the quest.
	 * 
	 * @see QuestProcessor#run() The internal logic.
	 */
	public static void processQuest(Player player, String questName) {
		new QuestProcessor(player, questName).run();
	}
	
	/**
	 * The player who is doing the quest.
	 */
	private final Player player;
	
	/**
	 * The quest of the player;
	 */
	private final WauzQuest quest;
	
	/**
	 * The name of the quest.
	 */
	private final String questName;
	
	/**
	 * The type of the quest;
	 */
	private final String questType;
	
	/**
	 * The current phase of the quest.
	 */
	private int questPhase;
	
	/**
	 * The total amount of quest phases.
	 */
	private final int questPhaseAmount;
	
	/**
	 * The slot of the quest.
	 */
	private String questSlot = null;
	
	/**
	 * Create a quest processor to manage the progression of a player's quest.
	 * 
	 * @param player The player who is doing the quest.
	 * @param questName The name of the quest.
	 */
	private QuestProcessor(Player player, String questName) {
		this.player = player;
		this.questName = questName;
		
		quest = WauzQuest.getQuest(questName);
		questType = quest.getType();
		questPhase = PlayerQuestConfigurator.getQuestPhase(player, questName);
		questPhaseAmount = quest.getPhaseAmount();
	}
	
	/**
	 * Tries to accept or continue the quest.
	 * 
	 * @see QuestProcessor#checkQuestSlots()
	 * @see QuestProcessor#checkQuestCompletion()
	 * @see QuestProcessor#acceptQuest()
	 * @see QuestProcessor#checkQuestCompletion()
	 * @see QuestProcessor#completeQuestStep()
	 */
	private void run() {
		if(!checkQuestSlots()) {
			player.sendMessage(ChatColor.RED + "Your Quest-Slots are full!");
			return;
		}
		WauzDebugger.log(player, "Quest Slot: " + questSlot);
		
		if(checkQuestCompletion()) {
			return;
		}
		WauzDebugger.log(player, "Phase: " + questPhase + " / " + questPhaseAmount);
		
		if(questPhase == 0) {
			acceptQuest();
		}
		else if(checkQuestObjectives()) {
			completeQuestStep();
		}
	}
	
	/**
	 * Determines the quest slot to use.
	 * If the quest isn't already running and no quest slot of the fitting type is available,
	 * false will be retuned, so the interaction can be cancelled.
	 * 
	 * @return If a usable quest slot was found.
	 * 
	 * @see PlayerConfigurator#getCharacterRunningMainQuest(Player)
	 */
	private boolean checkQuestSlots() {
		List<Pair<String, String>> slotsToCheck = new ArrayList<>();
		
		if(questType.equals(QuestType.MAIN)) {
			String slotm = PlayerConfigurator.getCharacterRunningMainQuest(player);
			slotsToCheck.add(Pair.of(slotm, QuestSlot.MAIN.getConfigKey()));
		}
		else if(questType.equals(QuestType.CAMPAIGN)) {
			String cmpn1 = PlayerConfigurator.getCharacterRunningCampaignQuest1(player);
			String cmpn2 = PlayerConfigurator.getCharacterRunningCampaignQuest2(player);
			slotsToCheck.add(Pair.of(cmpn1, QuestSlot.CAMPAIGN1.getConfigKey()));
			slotsToCheck.add(Pair.of(cmpn2, QuestSlot.CAMPAIGN2.getConfigKey()));
		}
		else if(questType.equals(QuestType.DAILY)) {
			String slot1 = PlayerConfigurator.getCharacterRunningDailyQuest1(player);
			String slot2 = PlayerConfigurator.getCharacterRunningDailyQuest2(player);
			String slot3 = PlayerConfigurator.getCharacterRunningDailyQuest3(player);
			slotsToCheck.add(Pair.of(slot1, QuestSlot.DAILY1.getConfigKey()));
			slotsToCheck.add(Pair.of(slot2, QuestSlot.DAILY2.getConfigKey()));
			slotsToCheck.add(Pair.of(slot3, QuestSlot.DAILY3.getConfigKey()));
		}
		
		for(Pair<String, String> slot : slotsToCheck) {
			if(questName.equals(slot.getKey())) {
				questSlot =  slot.getValue();
				return true;
			}
		}
		for(Pair<String, String> slot : slotsToCheck) {
			if("none".equals(slot.getKey())) {
				questSlot =  slot.getValue();
				return true;
			}
		}
		return StringUtils.isNotBlank(questSlot);
	}
	
	/**
	 * Determines if the quest cannot be repeated right now.
	 * If the quest is daily and has a cooldown, the remaining time, till the quest is redoable is shown.
	 * If the quest is not daily and already complete, a completion message is shown.
	 * 
	 * @return If the quest cannot be repeated right now.
	 * 
	 * @see PlayerQuestConfigurator#getQuestCooldown(Player, String)
	 * @see PlayerQuestConfigurator#isQuestCompleted(Player, String)
	 * @see WauzQuest#getCompletedDialog()
	 */
	private boolean checkQuestCompletion() {
		long cooldown = PlayerQuestConfigurator.getQuestCooldown(player, questName);
		long millis = System.currentTimeMillis() - cooldown;
		
		if(questPhase == 0 && millis < 14400000) {
			String time = WauzDateUtils.formatHoursMins(14400000 - millis + 60000);
			player.sendMessage(ChatColor.RED + "You have to wait " + time + " before you can do this quest again!");
			return true;
		}
		
		if(!questType.equals(QuestType.DAILY) && PlayerQuestConfigurator.isQuestCompleted(player, questName)) {
			new WauzPlayerEventCitizenTalk(quest.getQuestGiver(), quest.getCompletedDialog()).execute(player);
			return true;
		}
		return false;
	}
	
	/**
	 * Determines if all objectives of the quest phase are fulfilled.
	 * If the quest is running and phase requirements are not met, an "uncomplete" message is shown.
	 * 
	 * @return If all quest objectives are fulfilled.
	 * 
	 * @see QuestRequirementChecker#checkRequirements()
	 * @see WauzQuest#getUncompletedMessage(int)
	 */
	private boolean checkQuestObjectives() {
		if(!QuestRequirementChecker.create(player, quest, questPhase).checkRequirements()) {
			List<String> message = Collections.singletonList(quest.getUncompletedMessage(questPhase));
			new WauzPlayerEventCitizenTalk(quest.getQuestGiver(), message).execute(player);
			return false;
		}
		return true;
	}
	
	/**
	 * Lets the player accept the quest.
	 * If the quest is unstarted, an accept dialog is shown, or accepted directly, if it is a main quest.
	 * 
	 * @see WauzPlayerEventQuestAccept
	 */
	private void acceptQuest() {
		WauzPlayerEventQuestAccept event = new WauzPlayerEventQuestAccept(quest, questSlot);
		
		if(questType.equals(QuestType.MAIN)) {
			event.execute(player);
		}
		else {
			String requiredClass = quest.getRequiredClass();
			if(StringUtils.isNotBlank(requiredClass) && !PlayerConfigurator.getCharacterClass(player).contains(requiredClass)) {
				player.sendMessage(ChatColor.RED + "Only " + requiredClass + "s can accept this quest!");
				return;
			}
			String requiredPrequest = quest.getRequiredPrequest();
			if(StringUtils.isNotBlank(requiredPrequest) && PlayerQuestConfigurator.getQuestCompletions(player, requiredPrequest) < 1) {
				String requiredPrequestName = WauzQuest.getQuest(requiredPrequest).getDisplayName();
				player.sendMessage(ChatColor.RED + "You must complete \"" + requiredPrequestName + "\" before this quest!");
				return;
			}
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			playerData.getSelections().setWauzPlayerEventName("Accept Quest");
			playerData.getSelections().setWauzPlayerEvent(event);
			WauzDialog.open(player, QuestMenuItems.generateUnacceptedQuest(player, quest, 1, false));
		}
	}
	
	/**
	 * Starts the next quest phase or completes the quest.
	 * If the phase is completed, the next phase is initiated and an phase description message is shown.
	 * If all phases are completed, the reward chooser dialog is opened.
	 * 
	 * @see WauzQuest#getPhaseDialog(int)
	 * @see PlayerQuestConfigurator#setQuestPhase(Player, String, int)
	 * @see QuestRewardChooser#open(Player, WauzQuest, WauzPlayerEventQuestComplete)
	 */
	private void completeQuestStep() {
		if(questPhase < questPhaseAmount) {
			QuestRequirementChecker.create(player, quest, questPhase).handInItems();
			questPhase++;
			QuestRequirementChecker.create(player, quest, questPhase).initRequirements();
			PlayerQuestConfigurator.setQuestPhase(player, questName, questPhase);
			new WauzPlayerEventCitizenTalk(quest.getQuestGiver(), quest.getPhaseDialog(questPhase)).execute(player);
		}
		else {
			WauzPlayerEventQuestComplete completeEvent = new WauzPlayerEventQuestComplete(quest, questSlot);
			QuestRewardChooser.open(player, quest, completeEvent);
		}
	}

}
