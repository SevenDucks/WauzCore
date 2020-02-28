package eu.wauz.wauzcore.menu;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventQuestCancel;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzQuest;
import eu.wauz.wauzcore.system.quests.QuestRequirementChecker;
import net.md_5.bungee.api.ChatColor;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the main menu, that builds a quest display out of quest configs and manages quest progression.
 * 
 * @author Wauzmons
 *
 * @see WauzQuest
 */
public class QuestMenu implements WauzInventory {
	
// Load Quests from Questlog
	
	/**
	 * Opens the menu for the given player.
	 * Shows all quests that the player currently has, aswell as their progress.
	 * Also shows options to open the quest finder and toggle the visibility of certain quests.
	 * A quest can be tracked, by clicking on it.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerConfigurator#getCharacterRunningMainQuest(Player)
	 * @see QuestMenu#generateQuest(Player, String, int, Material)
	 * @see QuestMenu#generateEmptyQust(String)
	 * @see PlayerConfigurator#getHideSpecialQuestsForCharacter(Player)
	 * @see PlayerConfigurator#getHideCompletedQuestsForCharacter(Player)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new QuestMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Questlog");
		
		String slotm = PlayerConfigurator.getCharacterRunningMainQuest(player);
		String cmpn1 = PlayerConfigurator.getCharacterRunningCampaignQuest1(player);
		String cmpn2 = PlayerConfigurator.getCharacterRunningCampaignQuest2(player);
		String slot1 = PlayerConfigurator.getCharacterRunningDailyQuest1(player);
		String slot2 = PlayerConfigurator.getCharacterRunningDailyQuest2(player);
		String slot3 = PlayerConfigurator.getCharacterRunningDailyQuest3(player);
		
		if(!slotm.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slotm);
			menu.setItem(0, generateQuest(player, slotm, phase, Material.MAGENTA_CONCRETE));
		}
		else {
			menu.setItem(0, generateEmptyQust("Main"));
		}
		
		if(!cmpn1.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, cmpn1);
			menu.setItem(1, generateQuest(player, cmpn1, phase, Material.LIGHT_BLUE_CONCRETE));
		}
		else {
			menu.setItem(1, generateEmptyQust("Campaign"));
		}
		
		if(!cmpn2.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, cmpn2);
			menu.setItem(2, generateQuest(player, cmpn2, phase, Material.LIGHT_BLUE_CONCRETE));
		}
		else {
			menu.setItem(2, generateEmptyQust("Campaign"));
		}
		
		
		if(!slot1.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot1);
			menu.setItem(3, generateQuest(player, slot1, phase, Material.YELLOW_CONCRETE));
		}
		else {
			menu.setItem(3, generateEmptyQust("Daily"));	
		}
		
		if(!slot2.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot2);
			menu.setItem(4, generateQuest(player, slot2, phase, Material.YELLOW_CONCRETE));
		}
		else {
			menu.setItem(4, generateEmptyQust("Daily"));
		}
		
		if(!slot3.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot3);
			menu.setItem(5, generateQuest(player, slot3, phase, Material.YELLOW_CONCRETE));
		}
		else {
			menu.setItem(5, generateEmptyQust("Daily"));
		}
		
		ItemStack questFinderItemStack = new ItemStack(Material.BOOKSHELF);
		ItemMeta questFinderItemMeta = questFinderItemStack.getItemMeta();
		questFinderItemMeta.setDisplayName(ChatColor.BLUE + "Quest Finder");
		List<String> questFinderLores = new ArrayList<String>();
		questFinderLores.add(ChatColor.GRAY + "Find quests near your location.");
		questFinderItemMeta.setLore(questFinderLores);
		questFinderItemStack.setItemMeta(questFinderItemMeta);
		menu.setItem(6, questFinderItemStack);
		
		boolean hideSpecialQuests = PlayerConfigurator.getHideSpecialQuestsForCharacter(player);
		ItemStack hideSpecialQuestsItemStack = new ItemStack(hideSpecialQuests ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
		ItemMeta hideSpecialQuestsItemMeta = hideSpecialQuestsItemStack.getItemMeta();
		hideSpecialQuestsItemMeta.setDisplayName(ChatColor.GREEN + "Hide Special Quests");
		List<String> hideSpecialQuestLores = new ArrayList<String>();
		hideSpecialQuestLores.add(hideSpecialQuests ?
			ChatColor.GREEN + "ON " + ChatColor.GRAY + "Only Daily-Quests are shown in the sidebar!" :
			ChatColor.RED + "OFF " + ChatColor.GRAY + "All Quest-Types are shown in the sidebar!");
		hideSpecialQuestsItemMeta.setLore(hideSpecialQuestLores);
		hideSpecialQuestsItemStack.setItemMeta(hideSpecialQuestsItemMeta);
		menu.setItem(7, hideSpecialQuestsItemStack);
		
		boolean hideCompletedQuests = PlayerConfigurator.getHideCompletedQuestsForCharacter(player);
		ItemStack hideCompletedQuestsItemStack = new ItemStack(hideCompletedQuests ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
		ItemMeta hideComletedQuestsItemMeta = hideCompletedQuestsItemStack.getItemMeta();
		hideComletedQuestsItemMeta.setDisplayName(ChatColor.GREEN + "Hide Completed Quests");
		List<String> hideCompletedQuestsLores = new ArrayList<String>();
		hideCompletedQuestsLores.add(hideCompletedQuests ?
			ChatColor.GREEN + "ON " + ChatColor.GRAY + "Completed Quests are hidden in the sidebar!" :
			ChatColor.RED + "OFF " + ChatColor.GRAY + "Completed Quests are shown in the sidebar!");
		hideComletedQuestsItemMeta.setLore(hideCompletedQuestsLores);
		hideCompletedQuestsItemStack.setItemMeta(hideComletedQuestsItemMeta);
		menu.setItem(8, hideCompletedQuestsItemStack);
		
		player.openInventory(menu);		
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows all unaccepted quests that are near the player, trackable by clicking on the results.
	 * Ordered by level, then by distance from the player.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzQuest#getQuestsForLevel(int)
	 * @see QuestMenu#generateUnacceptedQuest(Player, WauzQuest, int, boolean)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void find(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new QuestMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Quests near "
				+ player.getName());
		
		int currentSlot = 0;
		
		Location playerLocation = player.getLocation();
		final Point2D playerPoint = new Point2D.Double(playerLocation.getX(), playerLocation.getZ());
		Comparator<WauzQuest> questLocationDistanceComparator = new Comparator<WauzQuest>()
        {
            @Override
            public int compare(WauzQuest quest0, WauzQuest quest1)
            {
				double distance0 = quest0.getQuestPoint().distanceSq(playerPoint);
				double distance1 = quest1.getQuestPoint().distanceSq(playerPoint);
				return Double.compare(distance0, distance1);
            }

        };
		
		for(int level = player.getLevel(); level <= WauzCore.MAX_PLAYER_LEVEL; level++) {
			List<WauzQuest> quests = WauzQuest.getQuestsForLevel(level);
			quests = quests.stream()
					.filter(quest -> PlayerQuestConfigurator.getQuestPhase(player, quest.getQuestName()) == 0)
					.filter(quest -> !quest.getType().toUpperCase().equals("MAIN"))
					.collect(Collectors.toList());
			
			Collections.sort(quests, questLocationDistanceComparator);
			
			for(WauzQuest quest : quests) {
				menu.setItem(currentSlot, generateUnacceptedQuest(player, quest, 1, true));
				currentSlot++;
				if(currentSlot > 8) {
					player.openInventory(menu);
					return;
				}
			}
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
// Generate a taken Quest-Slot
	
	/**
	 * Creates an item stack, containing information about a running quest.
	 * If it is not a main quest, an option to cancel the quest, will be added to the lore.
	 * 
	 * @param player The player that is doing the quest.
	 * @param questName The name of the quest.
	 * @param phase The current quest phase.
	 * @param colorMaterial The material of the item stack.
	 * 
	 * @return The quest item stack.
	 * 
	 * @see WauzQuest#getDisplayName()
	 * @see QuestRequirementChecker#getItemStackLores()
	 */
	public static ItemStack generateQuest(Player player, String questName, int phase, Material colorMaterial) {
		WauzQuest quest = WauzQuest.getQuest(questName);
		
		ItemStack questItemStack = new ItemStack(colorMaterial);
		ItemMeta questItemMeta = questItemStack.getItemMeta();
		questItemMeta.setDisplayName(ChatColor.GOLD + quest.getDisplayName());
		
		List<String> questLores = new ArrayList<String>();
		
		int level = quest.getLevel();
		questLores.add(ChatColor.GRAY + "Questgiver: " + questName.substring(0,1).toUpperCase() + questName.substring(1) + " at " + quest.getCoordinates());
		questLores.add(ChatColor.GRAY + "Level " + level + " [" + quest.getType().toUpperCase() + "] Quest");
		questLores.add("");
		
		for(String lore : quest.getPhaseDialog(phase)) {
			questLores.add(ChatColor.WHITE + lore.replaceAll("player", player.getName()));
		}
		questLores.add("");
		
		QuestRequirementChecker questRequirementChecker = new QuestRequirementChecker(player, quest, phase);
		questLores.addAll(questRequirementChecker.getItemStackLores());
		
		boolean isMainQuest = colorMaterial.equals(Material.MAGENTA_CONCRETE);
		
		if(quest.getRequirementAmount(phase) > 0) {
			questLores.add("");
		}
		questLores.add(ChatColor.GRAY + (isMainQuest ? "" : "Left ") + "Click to Track Objective");
		
		if(!isMainQuest) {
			questLores.add(ChatColor.GRAY + "Right Click to Cancel");
		}
		
		questItemMeta.setLore(questLores);
		questItemStack.setItemMeta(questItemMeta);
		return questItemStack;
	}
	
// Generate a free Quest-Slot
	
	/**
	 * Creates an item stack, that represents a free quest slot, that isn't bound to any quest.
	 * Used for showing free slots in the quest overview menu.
	 * 
	 * @param type The type of the quest, for showing in the display name.
	 * 
	 * @return The quest item stack.
	 */
	public static ItemStack generateEmptyQust(String type) {
		ItemStack emptyQuestItemStack = new ItemStack(Material.WHITE_CONCRETE);
		ItemMeta emptyQuestItemMeta = emptyQuestItemStack.getItemMeta();
		emptyQuestItemMeta.setDisplayName(ChatColor.DARK_GRAY + "No " + type + "-Quest in progress...");
		emptyQuestItemStack.setItemMeta(emptyQuestItemMeta);
		return emptyQuestItemStack;
	}
	
	/**
	 * Creates an item stack, containing information about an unstarted quest.
	 * Can be trackable for showing in the quest finder, or not, for showing in a dialog.
	 * 
	 * @param player The player that is viewing the quest.
	 * @param quest The name of the quest.
	 * @param phase The displayed quest phase.
	 * @param trackable If the quest can be tracked.
	 * 
	 * @return The quest item stack.
	 * 
	 * @see WauzQuest#getDisplayName()
	 * @see QuestRequirementChecker#getItemStackLoresUnaccepted()
	 */
	public static ItemStack generateUnacceptedQuest(Player player, WauzQuest quest, int phase, boolean trackable) {
		ItemStack unacceptedQuestItemStack = new ItemStack(Material.WRITABLE_BOOK);
		ItemMeta unacceptedQuestItemMeta = unacceptedQuestItemStack.getItemMeta();
		unacceptedQuestItemMeta.setDisplayName(ChatColor.GOLD + quest.getDisplayName());
		
		List<String> unacceptedQuestLores = new ArrayList<String>();
		
		int level = quest.getLevel();
		String questName = quest.getQuestName();
		
		unacceptedQuestLores.add(ChatColor.GRAY + "Questgiver: " + questName.substring(0,1).toUpperCase() + questName.substring(1) + " at " + quest.getCoordinates());
		unacceptedQuestLores.add(ChatColor.GRAY + "Level " + level + " [" + quest.getType().toUpperCase() + "] Quest");
		unacceptedQuestLores.add("");
		
		QuestRequirementChecker questRequirementChecker = new QuestRequirementChecker(player, quest, phase);
		unacceptedQuestLores.addAll(questRequirementChecker.getItemStackLoresUnaccepted());
		
		if(trackable) {
			if(quest.getRequirementAmount(phase) > 0) {
				unacceptedQuestLores.add("");
			}
			unacceptedQuestLores.add(ChatColor.GRAY + "Left Click to Track Objective");
		}
		
		unacceptedQuestItemMeta.setLore(unacceptedQuestLores);
		unacceptedQuestItemStack.setItemMeta(unacceptedQuestItemMeta);
		return unacceptedQuestItemStack;
	}
	
// Select Quest or Option
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and handles the interaction with a quest or display option.
	 * If a bookshelf was clicked and the player is in the main world, the quest finder will be opened.
	 * If an option to hide quests was clicked, it will be toggled and the menu will be reloaded.
	 * If a quest is clicked, it will be tracked.
	 * If a cancelable quest is right clicked, the cancel dialog will be shown.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see QuestMenu#find(Player)
	 * @see PlayerConfigurator#setHideSpecialQuestsForCharacter(Player, boolean)
	 * @see PlayerConfigurator#setHideCompletedQuestsForCharacter(Player, boolean)
	 * @see WauzPlayerScoreboard#scheduleScoreboard(Player)
	 * @see QuestRequirementChecker#trackQuestObjective()
	 * @see WauzPlayerEventQuestCancel
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(!ItemUtils.hasDisplayName(clicked)) {
			return;
		}

		String displayName = clicked.getItemMeta().getDisplayName();
		Material material = clicked.getType();
		
		if(material.equals(Material.BOOKSHELF)) {
			String worldString = PlayerConfigurator.getCharacterWorldString(player);
			if(player.getWorld().getName().equals(worldString)) {
				find(player);
			}
			else {
				player.sendMessage(ChatColor.RED + "The quest finder is only usable in " + worldString + "!");
				player.closeInventory();
				return;
			}
		}
		
		else if(displayName.contains("Hide Special Quests")) {
			PlayerConfigurator.setHideSpecialQuestsForCharacter(player, !PlayerConfigurator.getHideSpecialQuestsForCharacter(player));
			WauzPlayerScoreboard.scheduleScoreboard(player);
			QuestMenu.open(player);
		}
		
		else if(displayName.contains("Hide Completed Quests")) {
			PlayerConfigurator.setHideCompletedQuestsForCharacter(player, !PlayerConfigurator.getHideCompletedQuestsForCharacter(player));
			WauzPlayerScoreboard.scheduleScoreboard(player);
			QuestMenu.open(player);
		}
		
		else if(material.equals(Material.YELLOW_CONCRETE) ||
				material.equals(Material.LIGHT_BLUE_CONCRETE) ||
				material.equals(Material.MAGENTA_CONCRETE) ||
				material.equals(Material.WRITABLE_BOOK)) {
			
			String questName = ItemUtils.getStringBetweenFromLore(clicked, "Questgiver: ", " at ");
			WauzQuest quest = WauzQuest.getQuest(questName);
			int phase = PlayerQuestConfigurator.getQuestPhase(player, questName);
			
			if(ItemUtils.doesLoreContain(clicked, "Right Click to Cancel") && event.getClick().toString().contains("RIGHT")) {
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.setWauzPlayerEventName("Cancel Quest");
				playerData.setWauzPlayerEvent(new WauzPlayerEventQuestCancel(questName));
				clicked.setType(Material.WRITABLE_BOOK);
				WauzDialog.open(player, generateUnacceptedQuest(player, quest, phase, false));
			}
			else {
				new QuestRequirementChecker(player, quest, phase).trackQuestObjective();
			}
		}
	}

}
