package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPetsConfigurator;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the main menu, that is used to display collection mechanics.
 * 
 * @author Wauzmons
 *
 * @see WauzMenu
 */
public class CollectionMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "collection";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		CollectionMenu.open(player);
	}

	/**
	 * Opens the menu for the given player.
	 * All menus for collection mechanics plus a short information are shown.
	 * Here is a quick summary:</br>
	 * Slot 1: The currency menu + coin amount display.</br>
	 * Slot 3: The quest menu + completed quest count display.</br>
	 * Slot 4: Return to main menu...</br>
	 * Slot 5: The achievement menu + achievement count display.</br>
	 * Slot 7: The pet menu + used pet slots display.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerConfigurator#getCharacterCoins(Player)
	 * @see PlayerConfigurator#getCharacterAchievementProgress(Player, WauzAchievementType)
	 * @see PlayerConfigurator#getCharacterCompletedAchievements(Player)
	 * @see PlayerConfigurator#getCharacterUsedPetSlots(Player)
	 * @see MenuUtils#setMainMenuOpener(Inventory, int)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new CollectionMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Collection Menu");
		
		ItemStack currencyItemStack = HeadUtils.getMoneyItem();
		ItemMeta currencyItemMeta = currencyItemStack.getItemMeta();
		currencyItemMeta.setDisplayName(ChatColor.GOLD + "Currencies");
		List<String> currencyLores = new ArrayList<String>();
		currencyLores.add(ChatColor.DARK_PURPLE + "Total Coins: " + ChatColor.YELLOW
			+ Formatters.INT.format(PlayerConfigurator.getCharacterCoins(player)));
		currencyLores.add("");
		currencyLores.add(ChatColor.GRAY + "View all of your collected currencies");
		currencyLores.add(ChatColor.GRAY + "and faction reputation (favor points).");
		currencyItemMeta.setLore(currencyLores);
		currencyItemStack.setItemMeta(currencyItemMeta);
		menu.setItem(1, currencyItemStack);
		
		MenuUtils.setComingSoon(menu, "Tabards", 2);
		
		ItemStack questlogItemStack = HeadUtils.getQuestItem();
		ItemMeta questlogItemMeta = questlogItemStack.getItemMeta();
		questlogItemMeta.setDisplayName(ChatColor.GOLD + "Questlog");
		List<String> questlogLores = new ArrayList<String>();
		questlogLores.add(ChatColor.DARK_PURPLE + "Completed Quests: " + ChatColor.YELLOW
			+ Formatters.INT.format(PlayerConfigurator.getCharacterAchievementProgress(player, WauzAchievementType.COMPLETE_QUESTS)));
		questlogLores.add("");
		questlogLores.add(ChatColor.GRAY + "View or Cancel your running Quests.");
		questlogLores.add(ChatColor.GRAY + "Use the Questfinder to locate Questgivers.");
		questlogItemMeta.setLore(questlogLores);
		questlogItemStack.setItemMeta(questlogItemMeta);
		menu.setItem(3, questlogItemStack);
		
		ItemStack achievementsItemStack = HeadUtils.getAchievementsItem();
		ItemMeta achievementsItemMeta = achievementsItemStack.getItemMeta();
		achievementsItemMeta.setDisplayName(ChatColor.GOLD + "Achievements");
		List<String> achievementsLores = new ArrayList<String>();
		achievementsLores.add(ChatColor.DARK_PURPLE + "Collected Achievements: " + ChatColor.YELLOW
				+ Formatters.INT.format(PlayerConfigurator.getCharacterCompletedAchievements(player)));
		achievementsLores.add("");
		achievementsLores.add(ChatColor.GRAY + "Collect Achievements in many Categories,");
		achievementsLores.add(ChatColor.GRAY + "to earn a lot of precious Tokens.");
		achievementsItemMeta.setLore(achievementsLores);
		achievementsItemStack.setItemMeta(achievementsItemMeta);
		menu.setItem(5, achievementsItemStack);
		
		MenuUtils.setComingSoon(menu, "Bestiary", 6);
		
		ItemStack petsItemStack = HeadUtils.getTamesItem();
		ItemMeta petsItemMeta = petsItemStack.getItemMeta();
		petsItemMeta.setDisplayName(ChatColor.GOLD + "Pets");
		List<String> petsLores = new ArrayList<String>();
		petsLores.add(ChatColor.DARK_PURPLE + "Used Pet Slots: " + ChatColor.YELLOW
				+ PlayerPetsConfigurator.getCharacterUsedPetSlots(player) + " / 5");
			petsLores.add("");
		petsLores.add(ChatColor.GRAY + "View and Summon your tamed Pets.");
		petsLores.add(ChatColor.GRAY + "Breed them to get stronger Offsprings.");
		petsItemMeta.setLore(petsLores);
		petsItemStack.setItemMeta(petsItemMeta);
		menu.setItem(7, petsItemStack);
		
		MenuUtils.setMainMenuOpener(menu, 4);
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a sub menu was clicked, it will be opened.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see CurrencyMenu#open(Player)
	 * @see QuestMenu#open(Player)
	 * @see AchievementsMenu#open(Player)
	 * @see PetOverviewMenu#open(Player, int)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Currencies")) {
			CurrencyMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Questlog")) {
			QuestMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Achievements")) {
			AchievementsMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Pets")) {
			PetOverviewMenu.open(player, -1);
		}
	}

}
