package eu.wauz.wauzcore.menu.collection;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.quests.QuestMenuItems;
import eu.wauz.wauzcore.system.quests.QuestRequirementChecker;
import eu.wauz.wauzcore.system.quests.WauzQuest;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that builds a quest display out of quest configs.
 * 
 * @author Wauzmons
 *
 * @see WauzQuest
 */
public class QuestMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "quests";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		QuestMenu.open(player);
	}
	
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
			menu.setItem(0, QuestMenuItems.generateQuest(player, slotm, phase, Material.MAGENTA_CONCRETE));
		}
		else {
			menu.setItem(0, QuestMenuItems.generateEmptyQust("Main"));
		}
		
		if(!cmpn1.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, cmpn1);
			menu.setItem(1, QuestMenuItems.generateQuest(player, cmpn1, phase, Material.LIGHT_BLUE_CONCRETE));
		}
		else {
			menu.setItem(1, QuestMenuItems.generateEmptyQust("Campaign"));
		}
		
		if(!cmpn2.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, cmpn2);
			menu.setItem(2, QuestMenuItems.generateQuest(player, cmpn2, phase, Material.LIGHT_BLUE_CONCRETE));
		}
		else {
			menu.setItem(2, QuestMenuItems.generateEmptyQust("Campaign"));
		}
		
		
		if(!slot1.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot1);
			menu.setItem(3, QuestMenuItems.generateQuest(player, slot1, phase, Material.YELLOW_CONCRETE));
		}
		else {
			menu.setItem(3, QuestMenuItems.generateEmptyQust("Daily"));	
		}
		
		if(!slot2.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot2);
			menu.setItem(4, QuestMenuItems.generateQuest(player, slot2, phase, Material.YELLOW_CONCRETE));
		}
		else {
			menu.setItem(4, QuestMenuItems.generateEmptyQust("Daily"));
		}
		
		if(!slot3.equals("none")) {
			int phase = PlayerQuestConfigurator.getQuestPhase(player, slot3);
			menu.setItem(5, QuestMenuItems.generateQuest(player, slot3, phase, Material.YELLOW_CONCRETE));
		}
		else {
			menu.setItem(5, QuestMenuItems.generateEmptyQust("Daily"));
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
				menu.setItem(currentSlot, QuestMenuItems.generateUnacceptedQuest(player, quest, 1, true));
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
	 * @see WauzPlayerScoreboard#scheduleScoreboardRefresh(Player)
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
			WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
			QuestMenu.open(player);
		}
		
		else if(displayName.contains("Hide Completed Quests")) {
			PlayerConfigurator.setHideCompletedQuestsForCharacter(player, !PlayerConfigurator.getHideCompletedQuestsForCharacter(player));
			WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
			QuestMenu.open(player);
		}
		
		else if(material.equals(Material.YELLOW_CONCRETE) ||
				material.equals(Material.LIGHT_BLUE_CONCRETE) ||
				material.equals(Material.MAGENTA_CONCRETE) ||
				material.equals(Material.WRITABLE_BOOK)) {
			
			String questName = ItemUtils.getStringFromLore(clicked, "Quest-ID", 1);
			WauzQuest quest = WauzQuest.getQuest(questName);
			int phase = PlayerQuestConfigurator.getQuestPhase(player, questName);
			
			if(ItemUtils.doesLoreContain(clicked, "Right Click to Cancel") && event.getClick().toString().contains("RIGHT")) {
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.setWauzPlayerEventName("Cancel Quest");
				playerData.setWauzPlayerEvent(new WauzPlayerEventQuestCancel(questName));
				clicked.setType(Material.WRITABLE_BOOK);
				WauzDialog.open(player, QuestMenuItems.generateUnacceptedQuest(player, quest, phase, false));
			}
			else {
				QuestRequirementChecker.create(player, quest, phase).trackQuestObjective();
			}
		}
	}

}
