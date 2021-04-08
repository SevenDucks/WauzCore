package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.social.TitleMenu;
import eu.wauz.wauzcore.mobs.pets.WauzPetBreedingLevel;
import eu.wauz.wauzcore.system.WauzTitle;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * An util to add collection sub menus to the main menu.
 * 
 * @author Wauzmons
 *
 * @see WauzMenu
 */
public class CollectionMenuParts {
	
	/**
	 * Fills the given main menu with sub menu icons.<br/>
	 * Row 1, Slot 1: The quest menu + completed quest count display.<br/>
	 * Row 1, Slot 2: The achievement menu + achievement count display.<br/>
	 * Row 1, Slot 3: The pet menu + used pet slots display.<br/>
	 * Row 2, Slot 1: The currency menu + coin amount display.<br/>
	 * Row 2, Slot 2: The titles menu + unlocked title count display.<br/>
	 * Row 2, Slot 3: The bestiary menu + killed enemy count display.
	 * 
	 * @param player The player that should view the inventory.
	 * @param menu The main menu inventory.
	 * @param startIndex The first slot of the inventory to fill.
	 */
	public static void addMenuParts(Player player, Inventory menu, int startIndex) {
		ItemStack questlogItemStack = MenuIconHeads.getQuestItem();
		ItemMeta questlogItemMeta = questlogItemStack.getItemMeta();
		Components.displayName(questlogItemMeta, ChatColor.GOLD + "Questlog");
		List<String> questlogLores = new ArrayList<String>();
		questlogLores.add(ChatColor.DARK_PURPLE + "Completed Quests: " + ChatColor.YELLOW
			+ Formatters.INT.format(PlayerCollectionConfigurator.getCharacterAchievementProgress(player, WauzAchievementType.COMPLETE_QUESTS)));
		questlogLores.add("");
		questlogLores.add(ChatColor.GRAY + "View or Cancel your running Quests.");
		questlogLores.add(ChatColor.GRAY + "Use the Questfinder to locate Questgivers.");
		Components.lore(questlogItemMeta, questlogLores);
		questlogItemStack.setItemMeta(questlogItemMeta);
		menu.setItem(startIndex, questlogItemStack);
		
		ItemStack achievementsItemStack = MenuIconHeads.getAchievementsItem();
		ItemMeta achievementsItemMeta = achievementsItemStack.getItemMeta();
		Components.displayName(achievementsItemMeta, ChatColor.GOLD + "Achievements");
		List<String> achievementsLores = new ArrayList<>();
		achievementsLores.add(ChatColor.DARK_PURPLE + "Collected Achievements: " + ChatColor.YELLOW
				+ Formatters.INT.format(PlayerCollectionConfigurator.getCharacterCompletedAchievements(player)));
		achievementsLores.add("");
		achievementsLores.add(ChatColor.GRAY + "Collect Achievements in many Categories,");
		achievementsLores.add(ChatColor.GRAY + "to earn a lot of precious Tokens.");
		Components.lore(achievementsItemMeta, achievementsLores);
		achievementsItemStack.setItemMeta(achievementsItemMeta);
		menu.setItem(startIndex + 1, achievementsItemStack);
		
		ItemStack petsItemStack = MenuIconHeads.getTamesItem();
		ItemMeta petsItemMeta = petsItemStack.getItemMeta();
		Components.displayName(petsItemMeta, ChatColor.GOLD + "Breeding");
		List<String> petsLores = new ArrayList<>();
		int breedingExp = PlayerSkillConfigurator.getTamingSkill(player);
		petsLores.add(ChatColor.DARK_PURPLE + "Breeding Level: " + ChatColor.YELLOW
				+ WauzPetBreedingLevel.getBreedingLevel(breedingExp).getLevel() + " / " + WauzCore.MAX_BREEDING_SKILL);
		petsLores.add("");
		petsLores.add(ChatColor.GRAY + "Breed your collected Pets");
		petsLores.add(ChatColor.GRAY + "and obtain stronger Offsprings.");
		Components.lore(petsItemMeta, petsLores);
		petsItemStack.setItemMeta(petsItemMeta);
		menu.setItem(startIndex + 2, petsItemStack);
		
		ItemStack currencyItemStack = MenuIconHeads.getMoneyItem();
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		Components.displayName(currencyItemMeta, ChatColor.GOLD + "Currencies");
		List<String> currencyLores = new ArrayList<>();
		currencyLores.add(ChatColor.DARK_PURPLE + "Total Coins: " + ChatColor.YELLOW
			+ Formatters.formatCoins(PlayerCollectionConfigurator.getCharacterCoins(player)));
		currencyLores.add("");
		currencyLores.add(ChatColor.GRAY + "View all of your collected Currencies");
		currencyLores.add(ChatColor.GRAY + "and Faction Reputation (Favor Points).");
		Components.lore(currencyItemMeta, currencyLores);
		currencyItemStack.setItemMeta(currencyItemMeta);
		menu.setItem(startIndex + 9, currencyItemStack);
		
		ItemStack titlesItemStack = MenuIconHeads.getTitlesItem();
		ItemMeta titlesItemMeta = titlesItemStack.getItemMeta();
		Components.displayName(titlesItemMeta, ChatColor.GOLD + "Titles");
		List<String> titlesLores = new ArrayList<>();
		titlesLores.add(ChatColor.DARK_PURPLE + "Unlocked Titles: " + ChatColor.YELLOW
				+ PlayerConfigurator.getCharacterTitleList(player).size() + " / " + WauzTitle.getTitleCount());
		titlesLores.add("");
		titlesLores.add(ChatColor.GRAY + "View and select your Chat Titles");
		titlesLores.add(ChatColor.GRAY + "or buy new ones with your Soulstones.");
		Components.lore(titlesItemMeta, titlesLores);
		titlesItemStack.setItemMeta(titlesItemMeta);
		menu.setItem(startIndex + 10, titlesItemStack);
		
		ItemStack bestiaryItemStack = MenuIconHeads.getBeastsItem();
		ItemMeta bestiaryItemMeta = bestiaryItemStack.getItemMeta();
		Components.displayName(bestiaryItemMeta, ChatColor.GOLD + "Bestiary");
		List<String> bestiaryLores = new ArrayList<>();
		bestiaryLores.add(ChatColor.DARK_PURPLE + "Killed Enemies: " + ChatColor.YELLOW
			+ Formatters.INT.format(PlayerCollectionConfigurator.getCharacterAchievementProgress(player, WauzAchievementType.KILL_ENEMIES)));
		bestiaryLores.add("");
		bestiaryLores.add(ChatColor.GRAY + "View your Collection of Defeated Enemies.");
		bestiaryLores.add(ChatColor.GRAY + "Unlock Information based on Kill Count.");
		Components.lore(bestiaryItemMeta, bestiaryLores);
		bestiaryItemStack.setItemMeta(bestiaryItemMeta);
		menu.setItem(startIndex + 11, bestiaryItemStack);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * 
	 * @param player The placer who clicked a menu item.
	 * @param clicked The item that was clicled.
	 * 
	 * @return If an event was triggered.
	 * 
	 * @see QuestMenu#open(Player)
	 * @see AchievementsMenu#open(Player)
	 * @see PetOverviewMenu#open(Player, int)
	 * @see CurrencyMenu#open(Player)
	 * @see TitleMenu#open(Player)
	 * @see BestiaryMenu#open(Player)
	 */
	public static boolean check(Player player, ItemStack clicked) {
		if(HeadUtils.isHeadMenuItem(clicked, "Questlog")) {
			QuestMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Achievements")) {
			AchievementsMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Breeding")) {
			BreedingMenu.open(player, PlayerSkillConfigurator.getTamingSkill(player));
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Currencies")) {
			CurrencyMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Titles")) {
			TitleMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Bestiary")) {
			BestiaryMenu.open(player);
			return true;
		}
		return false;
	}

}
