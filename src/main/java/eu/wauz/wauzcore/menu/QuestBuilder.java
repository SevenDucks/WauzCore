package eu.wauz.wauzcore.menu;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
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
import eu.wauz.wauzcore.events.WauzPlayerEventQuestAccept;
import eu.wauz.wauzcore.events.WauzPlayerEventQuestCancel;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.QuestRequirementChecker;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzQuest;
import net.md_5.bungee.api.ChatColor;

public class QuestBuilder implements WauzInventory {
	
// Load Quests from Questlog
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new QuestBuilder());
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
	
	public static void find(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new QuestBuilder());
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
		
		if(!isMainQuest)
			questLores.add(ChatColor.GRAY + "Right Click to Cancel");
		
		questItemMeta.setLore(questLores);
		questItemStack.setItemMeta(questItemMeta);
		return questItemStack;
	}
	
// Generate a free Quest-Slot
	
	public static ItemStack generateEmptyQust(String type) {
		ItemStack emptyQuestItemStack = new ItemStack(Material.WHITE_CONCRETE);
		ItemMeta emptyQuestItemMeta = emptyQuestItemStack.getItemMeta();
		emptyQuestItemMeta.setDisplayName(ChatColor.DARK_GRAY + "No " + type + "-Quest in progress...");
		emptyQuestItemStack.setItemMeta(emptyQuestItemMeta);
		return emptyQuestItemStack;
	}
	
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
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName()) {
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
			QuestBuilder.open(player);
		}
		
		else if(displayName.contains("Hide Completed Quests")) {
			PlayerConfigurator.setHideCompletedQuestsForCharacter(player, !PlayerConfigurator.getHideCompletedQuestsForCharacter(player));
			WauzPlayerScoreboard.scheduleScoreboard(player);
			QuestBuilder.open(player);
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
	
// When Player talks to Questgiver
	
	public static void accept(Player player, String questName) {
		accept(player, questName, null);
	}
	
	public static void accept(Player player, String questName, Location questLocation) {
		WauzQuest quest = WauzQuest.getQuest(questName);
		
		String questGiver = "[(" + ChatColor.YELLOW + "Q" + ChatColor.RESET + ") " + questName + "] " + ChatColor.GRAY;
		String type = quest.getType();
		
		WauzDebugger.log(player, "Quest: " + questName + " " + type);
		
		String slotm = PlayerConfigurator.getCharacterRunningMainQuest(player);
		String cmpn1 = PlayerConfigurator.getCharacterRunningCampaignQuest1(player);
		String cmpn2 = PlayerConfigurator.getCharacterRunningCampaignQuest2(player);
		String slot1 = PlayerConfigurator.getCharacterRunningDailyQuest1(player);
		String slot2 = PlayerConfigurator.getCharacterRunningDailyQuest2(player);
		String slot3 = PlayerConfigurator.getCharacterRunningDailyQuest3(player);
		
// Check if Player has free Quest-Slot
		
		String questSlot = null;
		boolean valid = false;
		if(type.equals("main")) {
			if(slotm.equals(questName) || slotm.equals("none")) {
				questSlot = "quest.running.main";
				valid = true;
			}
		}
		else if(type.equals("campaign")) {
			if(cmpn1.equals(questName) || cmpn1.equals("none")) {
				questSlot = "quest.running.campaign1";
				valid = true;
			}
			else if(cmpn2.equals(questName) || cmpn2.equals("none")) {
				questSlot = "quest.running.campaign2";
				valid = true;
			}
		}
		else if(type.equals("daily")) {
			if(slot1.equals(questName) || slot1.equals("none")) {
				questSlot = "quest.running.daily1";
				valid = true;
			}
			else if(slot2.equals(questName) || slot2.equals("none")) {
				questSlot = "quest.running.daily2";
				valid = true;
			}
			else if(slot3.equals(questName) || slot3.equals("none")) {
				questSlot = "quest.running.daily3";
				valid = true;
			}
		}
		
		WauzDebugger.log(player, "Valid: " + valid + " " + questSlot);
		
		if(!valid) {
			player.sendMessage(ChatColor.RED + "Your Quest-Slots are full!");
			return;
		}
		
// Check Cooldown of Daily-Quests
		
		int phase = PlayerQuestConfigurator.getQuestPhase(player, questName);
		long cooldown = PlayerQuestConfigurator.getQuestCooldown(player, questName);
		long millis = System.currentTimeMillis() - cooldown;
		
		if(phase == 0 && millis < 14400000) {
			millis = 14400000 - millis;
			long hours = TimeUnit.MILLISECONDS.toHours(millis);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
			
			if(hours > 0) {
				player.sendMessage(ChatColor.RED + "You have to wait " + (hours + 1) + " hour/s before you can do this quest again!");
			}
			else {
				player.sendMessage(ChatColor.RED + "You have to wait " + (minutes + 1) + " minute/s before you can do this quest again!");
			}
			return;
		}
		
		if(!type.equals("daily") && PlayerQuestConfigurator.isQuestCompleted(player, questName)) {
			List<String> lores = quest.getCompletedDialog();
			for(String lore : lores) {
				player.sendMessage((questGiver + lore).replaceAll("player", player.getName()));
			}
		}
		
		String questDisplayName = quest.getDisplayName();
		int phaseAmount = quest.getPhaseAmount();
		
		WauzDebugger.log(player, "Phase: " + phase + " / " + phaseAmount);
		
// Accept the Quest
		
		if(phase == 0) {
			WauzPlayerEventQuestAccept event = new WauzPlayerEventQuestAccept(quest, questSlot, questGiver);
			
			if(type.equals("main")) {
				event.execute(player);
			}
			else {
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.setWauzPlayerEventName("Accept Quest");
				playerData.setWauzPlayerEvent(event);
				WauzDialog.open(player, generateUnacceptedQuest(player, quest, 1, false));
			}
			return;
		}
		
// Check if Objectives are fulfilled
		
		if(!new QuestRequirementChecker(player, quest, phase).tryToHandInQuest()) {
			player.sendMessage(questGiver + quest.getUncompletedMessage(phase).replaceAll("player", player.getName()));
			return;
		}
		
// Complete or Continue Quest-Chain
		
		if(phase < phaseAmount) {
			try {
				phase++;
				PlayerQuestConfigurator.setQuestPhase(player, questName, phase);
				List<String> lores = quest.getPhaseDialog(phase);
				for(String lore : lores) {
					player.sendMessage((questGiver + lore).replaceAll("player", player.getName()));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
		else {
			try {
				PlayerQuestConfigurator.setQuestPhase(player, questName, 0);
				PlayerConfigurator.setCharacterQuestSlot(player, questSlot, "none");
				if(type.equals("daily")) {
					PlayerQuestConfigurator.setQuestCooldown(player, questName);
				}
				PlayerQuestConfigurator.addQuestCompletions(player, questName);
				player.getWorld().playEffect(player.getLocation(), Effect.DRAGON_BREATH, 0);
				player.sendMessage(ChatColor.GREEN + "You completed [" + questDisplayName + "]");
				WauzRewards.level(player, quest.getLevel(), 2.5 * phaseAmount, questLocation);
				WauzRewards.mmorpgToken(player);
				List<String> lores = quest.getCompletedDialog();
				for(String lore : lores) {
					player.sendMessage((questGiver + lore).replaceAll("player", player.getName()));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}	
	}

}
