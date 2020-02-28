package eu.wauz.wauzcore.system.quests;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.CitizenConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenTalk;
import eu.wauz.wauzcore.events.WauzPlayerEventQuestAccept;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.menu.QuestMenu;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzQuest;
import net.md_5.bungee.api.ChatColor;

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
	 * @param questCitizen The name of the citizen who gives out the quest.
	 * 
	 * @see QuestProcessor#processQuest(Player, String, String, Location) Calls this with null as last param.
	 */
	public static void processQuest(Player player, String questName, String questCitizen) {
		processQuest(player, questName, questCitizen, null);
	}
	
	/**
	 * Tries to accept or continue the quest.
	 * 
	 * @param player The player who is doing the quest.
	 * @param questName The name of the quest.
	 * @param questCitizen The name of the citizen who gives out the quest.
	 * @param questLocation The location to show exp rewards.
	 * 
	 * @see QuestProcessor#run(Player, String, String, Location) The internal logic.
	 */
	public static void processQuest(Player player, String questName, String questCitizen, Location questLocation) {
		new QuestProcessor(player, questName, questCitizen, questLocation).run();
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
	 * The display name of the citizen who gave out the quest.
	 */
	private final String questGiver;
	
	/**
	 * The location to show exp rewards.
	 */
	private final Location questLocation;
	
	/**
	 * The slot of the quest.
	 */
	private String questSlot = null;
	
	/**
	 * Create a quest processor to manage the progression of a player's quest.
	 * 
	 * @param player The player who is doing the quest.
	 * @param questName The name of the quest.
	 * @param questCitizen The name of the citizen who gives out the quest.
	 * @param questLocation The location to show exp rewards.
	 */
	private QuestProcessor(Player player, String questName, String questCitizen, Location questLocation) {
		this.player = player;
		this.questName = questName;
		this.questLocation = questLocation;
		
		quest = WauzQuest.getQuest(questName);
		questGiver = CitizenConfigurator.getDisplayName(questCitizen);
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
		Map<String, String> slotValuesToCheck = new HashMap<>();
		
		if(questType.equals("main")) {
			String slotm = PlayerConfigurator.getCharacterRunningMainQuest(player);
			slotValuesToCheck.put(slotm, "quest.running.main");
		}
		else if(questType.equals("campaign")) {
			String cmpn1 = PlayerConfigurator.getCharacterRunningCampaignQuest1(player);
			String cmpn2 = PlayerConfigurator.getCharacterRunningCampaignQuest2(player);
			slotValuesToCheck.put(cmpn1, "quest.running.campaign1");
			slotValuesToCheck.put(cmpn2, "quest.running.campaign2");
		}
		else if(questType.equals("daily")) {
			String slot1 = PlayerConfigurator.getCharacterRunningDailyQuest1(player);
			String slot2 = PlayerConfigurator.getCharacterRunningDailyQuest2(player);
			String slot3 = PlayerConfigurator.getCharacterRunningDailyQuest3(player);
			slotValuesToCheck.put(slot1, "quest.running.daily1");
			slotValuesToCheck.put(slot2, "quest.running.daily2");
			slotValuesToCheck.put(slot3, "quest.running.daily3");
		}
		
		for(String slot : slotValuesToCheck.keySet()) {
			if(questName.equals(slot)) {
				questSlot =  slotValuesToCheck.get(slot);
				return true;
			}
		}
		for(String slot : slotValuesToCheck.keySet()) {
			if("none".equals(slot)) {
				questSlot =  slotValuesToCheck.get(slot);
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
			millis = 14400000 - millis;
			long hours = TimeUnit.MILLISECONDS.toHours(millis);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
			
			if(hours > 0) {
				player.sendMessage(ChatColor.RED + "You have to wait " + (hours + 1) + " hour/s before you can do this quest again!");
			}
			else {
				player.sendMessage(ChatColor.RED + "You have to wait " + (minutes + 1) + " minute/s before you can do this quest again!");
			}
			return true;
		}
		
		if(!questType.equals("daily") && PlayerQuestConfigurator.isQuestCompleted(player, questName)) {
			new WauzPlayerEventCitizenTalk(questGiver, quest.getCompletedDialog()).execute(player);
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
	 * @see QuestRequirementChecker#tryToHandInQuest()
	 * @see WauzQuest#getUncompletedMessage(int)
	 */
	private boolean checkQuestObjectives() {
		if(!new QuestRequirementChecker(player, quest, questPhase).tryToHandInQuest()) {
			List<String> message = Collections.singletonList(quest.getUncompletedMessage(questPhase));
			new WauzPlayerEventCitizenTalk(questGiver, message).execute(player);
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
		WauzPlayerEventQuestAccept event = new WauzPlayerEventQuestAccept(quest, questSlot, questGiver);
		
		if(questType.equals("main")) {
			event.execute(player);
		}
		else {
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			playerData.setWauzPlayerEventName("Accept Quest");
			playerData.setWauzPlayerEvent(event);
			WauzDialog.open(player, QuestMenu.generateUnacceptedQuest(player, quest, 1, false));
		}
	}
	
	/**
	 * Starts the next quest phase or completes the quest.
	 * If the phase is completed, the next phase is initiated and an phase description message is shown.
	 * If all phases are completed, the quest slot and the phase are cleared,
	 * the cooldown for daily quests gets resetted, the quest completions increase,
	 * an effect and the completion message is shown and the reward is handed out.
	 * 
	 * @see WauzQuest#getPhaseDialog(int)
	 * @see PlayerQuestConfigurator#setQuestPhase(Player, String, int)
	 * @see PlayerConfigurator#setCharacterQuestSlot(Player, String, String)
	 * @see PlayerQuestConfigurator#setQuestCooldown(Player, String)
	 * @see PlayerQuestConfigurator#addQuestCompletions(Player, String)
	 * @see WauzRewards#level(Player, int, double, Location)
	 * @see WauzRewards#mmorpgToken(Player)
	 */
	private void completeQuestStep() {
		if(questPhase < questPhaseAmount) {
			questPhase++;
			PlayerQuestConfigurator.setQuestPhase(player, questName, questPhase);
			new WauzPlayerEventCitizenTalk(questGiver, quest.getPhaseDialog(questPhase)).execute(player);
		}
		else {
			PlayerQuestConfigurator.setQuestPhase(player, questName, 0);
			PlayerConfigurator.setCharacterQuestSlot(player, questSlot, "none");
			if(questType.equals("daily")) {
				PlayerQuestConfigurator.setQuestCooldown(player, questName);
			}
			PlayerQuestConfigurator.addQuestCompletions(player, questName);
			player.getWorld().playEffect(player.getLocation(), Effect.DRAGON_BREATH, 0);
			player.sendMessage(ChatColor.GREEN + "You completed [" + quest.getDisplayName() + "]");
			WauzRewards.level(player, quest.getLevel(), 2.5 * questPhaseAmount, questLocation);
			WauzRewards.mmorpgToken(player);
			new WauzPlayerEventCitizenTalk(questGiver, quest.getCompletedDialog()).execute(player);
		}
	}

}
