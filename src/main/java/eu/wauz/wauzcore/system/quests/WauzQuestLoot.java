package eu.wauz.wauzcore.system.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.QuestConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.items.ItemManager;
import io.lumine.xikage.mythicmobs.items.MythicItem;

/**
 * The loot reward of a quest.
 * 
 * @author Wauzmons
 *
 * @see WauzQuest
 */
public class WauzQuestLoot {
	
	/**
	 * Access to the MythicMobs API item manager.
	 */
	private static ItemManager mythicMobs = MythicMobs.inst().getItemManager();
	
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
			
			Optional<MythicItem> mythicItemOptional = mythicMobs.getItem(canonicalName);
			if(mythicItemOptional.isPresent()) {
				ItemStack rewardItemStack = mythicMobs.getItemStack(canonicalName);
				if(StringUtils.isNotBlank(displayNameSuffix) && ItemUtils.hasDisplayName(rewardItemStack)) {
					ItemMeta rewardItemMeta = rewardItemStack.getItemMeta();
					rewardItemMeta.setDisplayName(rewardItemMeta.getDisplayName() + displayNameSuffix);
					rewardItemStack.setItemMeta(rewardItemMeta);
				}
				itemStacks.add(rewardItemStack);
			}
			else {
				WauzDebugger.log("Invalid MythicMobs Item in Quest \"" + questName + "\": " + rewardItemName);
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
