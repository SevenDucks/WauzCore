package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.system.quests.WauzQuest;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Lets you choose a reward after completing a quest.
 * 
 * @author Wauzmons
 *
 * @see WauzQuest
 */
public class QuestRewardChooser implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * Shows the rewards, the player receives for the quest and a confirm button.
	 * If only one reward item can be chosen, the player must do this before proceeding.
	 * 
	 * @param player The player that should view the inventory.
	 * @param quest The quest that has been completed.
	 */
	public static void open(Player player, WauzQuest quest) {
		
	}
	
	/**
	 * Creates an item stack, containing information about a finished quest.
	 * Contains information about scaled quest rewards.
	 * 
	 * @param player The player that is viewing the quest.
	 * @param quest The unaccepted quest.
	 * 
	 * @return The quest item stack.
	 */
	public ItemStack generateCompletedQuest(Player player, WauzQuest quest) {
		ItemStack completedQuestItemStack = new ItemStack(Material.WRITABLE_BOOK);
		ItemMeta completedQuestItemMeta = completedQuestItemStack.getItemMeta();
		completedQuestItemMeta.setDisplayName(ChatColor.GOLD + quest.getDisplayName() + " (Completed)");
		
		List<String> copletedQuestLores = new ArrayList<String>();
		
		completedQuestItemMeta.setLore(copletedQuestLores);
		completedQuestItemStack.setItemMeta(completedQuestItemMeta);
		return completedQuestItemStack;
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * The quest can be completed, when a reward was selected.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		
	}

}
