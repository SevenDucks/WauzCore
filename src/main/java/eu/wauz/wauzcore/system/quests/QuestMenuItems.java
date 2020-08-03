package eu.wauz.wauzcore.system.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.data.players.PlayerRelationConfigurator;
import eu.wauz.wauzcore.mobs.citizens.RelationLevel;
import eu.wauz.wauzcore.players.calc.ExperienceCalculator;

/**
 * Used to create quest related menu items for different menus.
 * 
 * @author Wauzmons
 */
public class QuestMenuItems {
	
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
		
		questLores.addAll(getInformationSection(quest));
		questLores.add("");
		
		for(String lore : quest.getPhaseDialog(phase)) {
			questLores.add(ChatColor.WHITE + lore.replaceAll("player", player.getName()));
		}
		questLores.add("");
		
		questLores.addAll(getRewardSection(player, quest));
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
	 * @param quest The unaccepted quest.
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
		
		unacceptedQuestLores.addAll(getInformationSection(quest));
		unacceptedQuestLores.add("");
		
		unacceptedQuestLores.addAll(getRewardSection(player, quest));
		if(quest.getRequirementAmount(phase) > 0) {
			unacceptedQuestLores.add("");
		}
		
		QuestRequirementChecker questRequirementChecker = new QuestRequirementChecker(player, quest, phase);
		unacceptedQuestLores.addAll(questRequirementChecker.getItemStackLoresUnaccepted());
		
		if(trackable) {
			unacceptedQuestLores.add("");
			unacceptedQuestLores.add(ChatColor.GRAY + "Left Click to Track Objective");
		}
		
		unacceptedQuestItemMeta.setLore(unacceptedQuestLores);
		unacceptedQuestItemStack.setItemMeta(unacceptedQuestItemMeta);
		return unacceptedQuestItemStack;
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
	public static ItemStack generateCompletedQuest(Player player, WauzQuest quest) {
		ItemStack completedQuestItemStack = new ItemStack(Material.WRITABLE_BOOK);
		ItemMeta completedQuestItemMeta = completedQuestItemStack.getItemMeta();
		completedQuestItemMeta.setDisplayName(ChatColor.GOLD + quest.getDisplayName() + " (Completed)");
		
		List<String> completedQuestLores = new ArrayList<String>();
		
		completedQuestLores.addAll(getInformationSection(quest));
		completedQuestLores.add("");
		
		for(String lore : quest.getCompletedDialog()) {
			completedQuestLores.add(ChatColor.WHITE + lore.replaceAll("player", player.getName()));
		}
		completedQuestLores.add("");
		
		completedQuestLores.addAll(getRewardSection(player, quest));
		
		completedQuestItemMeta.setLore(completedQuestLores);
		completedQuestItemStack.setItemMeta(completedQuestItemMeta);
		return completedQuestItemStack;
	}
	
	/**
	 * Creates a information section, to display in quest item lores.
	 * 
	 * @param quest The quest to show.
	 * 
	 * @return The generated information section.
	 */
	private static List<String> getInformationSection(WauzQuest quest) {
		List<String> lores = new ArrayList<>();
		lores.add(ChatColor.GRAY + "Quest-ID: " + quest.getQuestName());
		lores.add(ChatColor.GRAY + "Questgiver: " + quest.getQuestGiver() + " at " + quest.getCoordinates());
		lores.add(ChatColor.GRAY + "Level " + quest.getLevel() + " [" + quest.getType().toUpperCase() + "] Quest");
		return lores;
	}
	
	/**
	 * Creates a reward section, including bonus multipliers, to display in quest item lores.
	 * 
	 * @param player The player that is viewing the quest.
	 * @param quest The quest to show.
	 * 
	 * @return The generated reward section.
	 */
	private static List<String> getRewardSection(Player player, WauzQuest quest) {
		List<String> lores = new ArrayList<>();
		int relationProgress = PlayerRelationConfigurator.getRelationProgress(player, quest.getQuestGiver());
		double rewardMultiplier = RelationLevel.getRelationLevel(relationProgress).getRewardMultiplier();
		int rewardCoins = (int) (quest.getRewardCoins() * PlayerSkillConfigurator.getTradingFloat(player) * rewardMultiplier);
		int rewardExp = (int) (ExperienceCalculator.applyExperienceBonus(player, quest.getRewardExp() * rewardMultiplier) * 100);
		
		String hyphen = ChatColor.GRAY + "- ";
		lores.add(ChatColor.GRAY + "Completion Rewards:");
		lores.add(hyphen + ChatColor.GOLD + rewardCoins + " Coins");
		lores.add(hyphen + ChatColor.AQUA + rewardExp + " Exp");
		lores.add(hyphen + ChatColor.LIGHT_PURPLE + quest.getRewardRelationExp() + " Relation Exp");
		return lores;
	}

}
