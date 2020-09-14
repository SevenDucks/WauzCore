package eu.wauz.wauzcore.system.quests;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerBestiaryConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.mobs.bestiary.WauzBestiaryEntry;
import eu.wauz.wauzcore.mobs.bestiary.WauzBestiarySpecies;

/**
 * A helper class for checking the completion of "kill" quest phase requirements.
 * 
 * @author Wauzmons
 */
public class QuestRequirementCheckerKills extends QuestRequirementChecker {
	
	/**
	 * Initializes a new quest requirement checker for kills.
	 * 
	 * @param player The player that is doing the quest.
	 * @param quest The quest to check requirements for.
	 * @param phase The quest phase to check requirements for.
	 */
	public QuestRequirementCheckerKills(Player player, WauzQuest quest, int phase) {
		super(player, quest, phase);
	}
	
	/**
	 * Initializes configuration values for the requirements, if any are needed.
	 */
	public void initRequirements() {
		for(int requirement = 1; requirement <= quest.getRequirementAmount(phase); requirement++) {
			String itemName = quest.getRequirementNeededItemName(phase, requirement);
			int kills = PlayerBestiaryConfigurator.getBestiaryKills(player, itemName);
			PlayerQuestConfigurator.setQuestPhaseRequirementValue(player, quest.getQuestName(), phase, requirement, kills);
		}
	}

	/**
	 * Checks the requirements and hands in the quest items, if all requirements were met.
	 * 
	 * @return If the quest phase is completed.
	 */
	public boolean tryToHandInQuest() {
		return execute(false);
	}
	
	/**
	 * Checks the requirements of the quest and creates corresponding tracker locations and lore.
	 * 
	 * @param onlyObjectives If only objectives and no progress should appear in the lore.
	 * 
	 * @return If the quest phase is completed.
	 */
	protected boolean execute(boolean onlyObjectives) {
		int requirementAmount = quest.getRequirementAmount(phase);
		int fulfilledAmount = 0;
		
		itemStackLores = new ArrayList<>();
		objectiveLores = new ArrayList<>();
		trackerLocationString = null;
		trackerName = null;
		
		for(int requirement = 1; requirement <= requirementAmount; requirement++) {		
			String itemName = quest.getRequirementNeededItemName(phase, requirement);
			String itemCoordinates = quest.getRequirementNeededItemCoordinates(phase, requirement);
			WauzBestiaryEntry entry = WauzBestiarySpecies.getEntry(itemName);
			String itemDisplayName = ChatColor.stripColor(entry.getEntryMobDisplayName());
			itemStackLores.add(ChatColor.GRAY + "Kill " + itemDisplayName + " around " + itemCoordinates);
			
			if(onlyObjectives) {
				continue;
			}
				
			long initialValue = PlayerQuestConfigurator.getQuestPhaseRequirementValue(player, quest.getQuestName(), phase, requirement);
			long currentValue = PlayerBestiaryConfigurator.getBestiaryKills(player, entry.getEntryFullName());
			int requiredAmount = quest.getRequirementNeededItemAmount(phase, requirement);
			long killedAmount = currentValue - initialValue;
			ChatColor finished = ChatColor.RED;
			
			if(killedAmount >= requiredAmount) {
				fulfilledAmount++;
				finished = ChatColor.GREEN;
				killedAmount = requiredAmount;
			}
			else if(trackerLocationString == null) {
				trackerLocationString = itemCoordinates;
				trackerName = itemDisplayName;
			}
			
			itemStackLores.add(finished + "Amount: " + killedAmount + " / " + requiredAmount);
			objectiveLores.add(finished + "  > " + ChatColor.WHITE + killedAmount + " / " + requiredAmount + " " + itemDisplayName);
			if(requirement > 1) {
				itemStackLores.add("");
			}
		}
		
		boolean success = fulfilledAmount == requirementAmount;
		if(success) {
			trackerLocationString = quest.getCoordinates();
			trackerName = quest.getDisplayName();
		}
		
		return success;
	}

}
