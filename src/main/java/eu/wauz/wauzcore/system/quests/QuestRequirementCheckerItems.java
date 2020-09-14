package eu.wauz.wauzcore.system.quests;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.InventoryItemRemover;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.MaterialPouch;

/**
 * A helper class for checking the completion of "item" quest phase requirements.
 * 
 * @author Wauzmons
 */
public class QuestRequirementCheckerItems extends QuestRequirementChecker {
	
	/**
	 * Initializes a new quest requirement checker for items.
	 * 
	 * @param player The player that is doing the quest.
	 * @param quest The quest to check requirements for.
	 * @param phase The quest phase to check requirements for.
	 */
	public QuestRequirementCheckerItems(Player player, WauzQuest quest, int phase) {
		super(player, quest, phase);
	}
	
	/**
	 * Initializes configuration values for the requirements, if any are needed.
	 */
	public void initRequirements() {
		// Nothing to do here...
	}

	/**
	 * Checks the requirements and hands in the quest items, if all requirements were met.
	 * 
	 * @return If the quest phase is completed.
	 */
	public boolean tryToHandInQuest() {
		boolean success = execute(false);
		if(success) {
			itemRemover.execute();
		}
		return success;
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
		
		Inventory inventory = MaterialPouch.getInventory(player, "questitems");
		itemRemover = new InventoryItemRemover(inventory);
		
		for(int requirement = 1; requirement <= requirementAmount; requirement++) {		
			String itemName = quest.getRequirementNeededItemName(phase, requirement);
			String itemCoordinates = quest.getRequirementNeededItemCoordinates(phase, requirement);
			itemStackLores.add(ChatColor.GRAY + "Collect " + itemName + " around " + itemCoordinates);
			
			if(onlyObjectives) {
				continue;
			}
				
			int requiredAmount = quest.getRequirementNeededItemAmount(phase, requirement);
			int collectedAmount = 0;
			ChatColor finished = ChatColor.RED;
			
			itemRemover.addItemNameToRemove(itemName, requiredAmount);
			for(ItemStack itemStack : inventory.getContents()) {
				if(itemStack != null && ItemUtils.isSpecificItem(itemStack, itemName)) {
					collectedAmount += itemStack.getAmount();
				}
			}
			
			if(collectedAmount >= requiredAmount) {
				fulfilledAmount++;
				finished = ChatColor.GREEN;
				collectedAmount = requiredAmount;
			}
			else if(trackerLocationString == null) {
				trackerLocationString = itemCoordinates;
				trackerName = itemName;
			}
			
			itemStackLores.add(finished + "Amount: " + collectedAmount + " / " + requiredAmount);
			objectiveLores.add(finished + "  > " + ChatColor.WHITE + collectedAmount + " / " + requiredAmount + " " + itemName);
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
