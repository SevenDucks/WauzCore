package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.events.WauzPlayerEventQuestComplete;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.system.quests.QuestMenuItems;
import eu.wauz.wauzcore.system.quests.WauzQuest;
import eu.wauz.wauzcore.system.util.Components;

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
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "questrewards";
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows the rewards, the player receives for the quest and a confirm button.
	 * If a reward item can be chosen, the player must do this before proceeding.
	 * 
	 * @param player The player that should view the inventory.
	 * @param quest The quest that has been completed.
	 * @param completeEvent The complete event to execute.
	 */
	public static void open(Player player, WauzQuest quest, WauzPlayerEventQuestComplete completeEvent) {
		QuestRewardChooser rewardChooser = new QuestRewardChooser(quest, completeEvent);
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Complete Quest";
		Inventory menu = Components.inventory(rewardChooser, menuTitle, 9);
		rewardChooser.setChoiceDialog(menu);
		
		ItemStack completeItemStack = GenericIconHeads.getConfirmItem();
		ItemMeta completeItemMeta = completeItemStack.getItemMeta();
		Components.displayName(completeItemMeta, ChatColor.GREEN + "COMPLETE");
		completeItemStack.setItemMeta(completeItemMeta);
		menu.setItem(0, completeItemStack);
		
		menu.setItem(2, QuestMenuItems.generateCompletedQuest(player, quest));
		
		List<ItemStack> rewardChoices = quest.getRewardLoot().getRewardChoiceItems();
		for(int index = 0; index < 3; index++) {
			int offsetIndex = index + 4;
			if(rewardChoices.size() > index) {
				menu.setItem(offsetIndex, makeRewardChoice(rewardChoices.get(index)));
			}
			else {
				menu.setItem(offsetIndex, generateUnavailableChoiceItem(index + 1));
			}
		}
		if(!rewardChoices.isEmpty()) {
			rewardChooser.updateChoiceSelection();
		}
		
		ItemStack cancelItemStack = GenericIconHeads.getDeclineItem();
		ItemMeta cancelItemMeta = cancelItemStack.getItemMeta();
		Components.displayName(cancelItemMeta, ChatColor.RED + "CANCEL");
		cancelItemStack.setItemMeta(cancelItemMeta);
		menu.setItem(8, cancelItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Generates an item stack, displaying an unavailable choice.
	 * 
	 * @param itemNumber The number in the title.
	 * 
	 * @return The choice item stack.
	 */
	private static ItemStack generateUnavailableChoiceItem(int itemNumber) {
		ItemStack choiceItemStack = new ItemStack(Material.OAK_SIGN);
		ItemMeta choiceItemMeta = choiceItemStack.getItemMeta();
		Components.displayName(choiceItemMeta, ChatColor.RED + "No Reward Choice " + itemNumber);
		choiceItemStack.setItemMeta(choiceItemMeta);
		return choiceItemStack;
	}
	
	/**
	 * Transforms an item stack into a reward choice, by adding a prefix to it.
	 * 
	 * @param choiceItemStack The item check to transform.
	 */
	private static ItemStack makeRewardChoice(ItemStack choiceItemStack) {
		ItemMeta choiceItemMeta = choiceItemStack.getItemMeta();
		Components.displayName(choiceItemMeta, ChatColor.GREEN + "Reward Choice: " + choiceItemMeta.getDisplayName());
		choiceItemStack.setItemMeta(choiceItemMeta);
		return choiceItemStack;
	}
	
	/**
	 * Checks if an item stack is a reward choice, by looking for the prefix.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If it is a reward choice.
	 */
	private static boolean isRewardChoice(ItemStack itemStack) {
		if(!ItemUtils.hasDisplayName(itemStack)) {
			return false;
		}
		return itemStack.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Reward Choice: ");
	}
	
	/**
	 * The quest that has been completed.
	 */
	private WauzQuest quest;
	
	/**
	 * The complete event to execute.
	 */
	private WauzPlayerEventQuestComplete completeEvent;
	
	/**
	 * The inventory instance to hold the selectable choices.
	 */
	private Inventory choiceDialog;
	
	/**
	 * The index of the chosen reward item stack.
	 */
	private Integer chosenRewardIndex;
	
	/**
	 * Creates a new chooser with an event to execute after the choice was made.
	 * 
	 * @param quest The quest that has been completed.
	 * @param completeEvent The complete event to execute.
	 */
	public QuestRewardChooser(WauzQuest quest, WauzPlayerEventQuestComplete completeEvent) {
		this.quest = quest;
		this.completeEvent = completeEvent;
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * The quest can be completed, when a reward was selected.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerEventQuestComplete#execute(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "COMPLETE")) {
			player.closeInventory();
			List<ItemStack> questLoot = new ArrayList<>();
			questLoot.addAll(quest.getRewardLoot().getRewardItems());
			if(chosenRewardIndex != null) {
				List<ItemStack> rewardChoices = quest.getRewardLoot().getRewardChoiceItems();
				questLoot.add(rewardChoices.get(chosenRewardIndex - 4));
			}
			completeEvent.setQuestLoot(questLoot);
			completeEvent.execute(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "CANCEL")) {
			player.closeInventory();
		}
		else if(isRewardChoice(clicked)) {
			chosenRewardIndex = event.getSlot();
			updateChoiceSelection();
		}
	}
	
	/**
	 * Updates the selection of the reward.
	 * If no reward is selected, a notice will be added to the complete menu item, also disabling it that way.
	 * The selected reward will start to glow, by adding a hidden enchant to it.
	 */
	public void updateChoiceSelection() {
		ItemStack completeItemStack = choiceDialog.getItem(0);
		ItemMeta completeItemMeta = completeItemStack.getItemMeta();
		
		if(chosenRewardIndex == null) {
			Components.displayName(completeItemMeta, ChatColor.GREEN + "COMPLETE" + ChatColor.GRAY + " (Select a reward first...)");
		}
		else {
			Components.displayName(completeItemMeta, ChatColor.GREEN + "COMPLETE");
			for(int index = 0; index < 3; index++) {
				int offsetIndex = index + 4;
				ItemStack choiceItemStack = choiceDialog.getItem(offsetIndex);
				ItemMeta choiceItemMeta = choiceItemStack.getItemMeta();
				if(chosenRewardIndex == offsetIndex) {
					choiceItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
					choiceItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				else {
					choiceItemMeta.removeEnchant(Enchantment.ARROW_INFINITE);
					choiceItemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				choiceItemStack.setItemMeta(choiceItemMeta);
			}
		}
		completeItemStack.setItemMeta(completeItemMeta);
	}

	/**
	 * @param choiceDialog The new inventory instance to hold the selectable choices.
	 */
	public void setChoiceDialog(Inventory choiceDialog) {
		this.choiceDialog = choiceDialog;
	}

}
