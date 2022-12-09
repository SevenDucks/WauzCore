package eu.wauz.wauzcore.system.quests;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.QuestConfigurator;
import eu.wauz.wauzcore.items.InventorySerializer;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.MythicUtils;
import io.lumine.mythic.api.items.ItemManager;

/**
 * The loot reward of a quest.
 * 
 * @author Wauzmons
 *
 * @see WauzQuest
 */
public class WauzQuestLoot {
	
	/**
	 * The name of the quest.
	 */
	private final String questName;
	
	/**
	 * The fixed item reward as string list.
	 */
	private final List<String> rewardItemNames;
	
	/**
	 * The fixed item reward.
	 */
	private final List<ItemStack> rewardItems;
	
	/**
	 * The choosable item reward as string list.
	 */
	private final List<String> rewardChoiceItemNames;
	
	/**
	 * The choosable item reward.
	 */
	private final List<ItemStack> rewardChoiceItems;

	/**
	 * Constructs collections of reward items for the quest with the given name.
	 * 
	 * @param questName The name of the quest.
	 * 
	 * @see WauzQuestLoot#generateItemStackList(List)
	 */
	public WauzQuestLoot(String questName) {
		this.questName = questName;
		rewardItemNames = QuestConfigurator.getRewardItems(questName);
		rewardChoiceItemNames = QuestConfigurator.getRewardChoiceItems(questName);
		
		rewardItems = generateItemStackList(rewardItemNames);
		rewardChoiceItems = generateItemStackList(rewardChoiceItemNames);
	}
	
	/**
	 * Generates a list of item stacks from MythicMobs, based on their names.
	 * Items can have title suffixes, indicated by a ; after their name.
	 * Logs to the console, if an item is unknown.
	 * 
	 * @param itemNames The names of the items.
	 * 
	 * @return The generated list of item stacks.
	 * 
	 * @see ItemManager#getItem(String)
	 * @see ItemManager#getItemStack(String)
	 * @see WauzDebugger#log(String)
	 */
	private List<ItemStack> generateItemStackList(List<String> itemNames) {
		List<ItemStack> itemStacks = new ArrayList<>();
		for(String rewardItemName : itemNames) {
			String[] nameParts = rewardItemName.split(";");
			String canonicalName = nameParts[0];
			String displayNameSuffix = nameParts.length > 1 ? nameParts[1] : null;
			
			ItemStack rewardItemStack = MythicUtils.getItemStack(canonicalName, "Quest " + questName);
			if(rewardItemStack != null) {
				rewardItemStack = InventorySerializer.serialize(rewardItemStack);
				if(StringUtils.isNotBlank(displayNameSuffix) && ItemUtils.hasDisplayName(rewardItemStack)) {
					ItemMeta rewardItemMeta = rewardItemStack.getItemMeta();
					Components.displayName(rewardItemMeta, Components.displayName(rewardItemMeta) + displayNameSuffix);
					rewardItemStack.setItemMeta(rewardItemMeta);
				}
				itemStacks.add(rewardItemStack);
			}
		}
		return itemStacks;
	}

	/**
	 * @return The fixed item reward.
	 */
	public List<ItemStack> getRewardItems() {
		return cloneItemList(rewardItems);
	}

	/**
	 * @return The choosable item reward.
	 */
	public List<ItemStack> getRewardChoiceItems() {
		return cloneItemList(rewardChoiceItems);
	}
	
	/**
	 * Creates a save copy of a list of reward item stacks.
	 * 
	 * @param itemsToClone The list of items to clone.
	 * 
	 * @return The list of cloned items.
	 */
	private List<ItemStack> cloneItemList(List<ItemStack> itemsToClone) {
		List<ItemStack> clonedItems = new ArrayList<>();
		for(ItemStack itemToClone : itemsToClone) {
			clonedItems.add(itemToClone.clone());
		}
		return clonedItems;
	}
	
}
