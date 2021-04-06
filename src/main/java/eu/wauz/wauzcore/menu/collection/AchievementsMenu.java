package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that shows the player's progress towards achievements.
 * 
 * @author Wauzmons
 * 
 * @see WauzAchievement
 */
@PublicMenu
public class AchievementsMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "achievements";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG);
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		AchievementsMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows progress towards achievements, displayed as one item stack per achievement type.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzAchievementType
	 * @see AchievementTracker#generateProgressLores(Player, WauzAchievementType)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Achievements";
		Inventory menu = Components.inventory(new AchievementsMenu(), menuTitle, 18);
		
		ItemStack killsItemStack = GenericIconHeads.getAchievementKillsItem();
		ItemMeta killsItemMeta = killsItemStack.getItemMeta();
		Components.displayName(killsItemMeta, ChatColor.YELLOW + "Kill Enemies");
		Components.lore(killsItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.KILL_ENEMIES));
		killsItemStack.setItemMeta(killsItemMeta);
		menu.setItem(0, killsItemStack);
				
		ItemStack identifiesItemStack = GenericIconHeads.getAchievementIdentifiesItem();
		ItemMeta identifiesItemMeta = identifiesItemStack.getItemMeta();
		Components.displayName(identifiesItemMeta, ChatColor.YELLOW + "Identify Items");
		Components.lore(identifiesItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.IDENTIFY_ITEMS));
		identifiesItemStack.setItemMeta(identifiesItemMeta);
		menu.setItem(1, identifiesItemStack);
		
		ItemStack manaItemStack = GenericIconHeads.getAchievementManaItem();
		ItemMeta manaItemMeta = manaItemStack.getItemMeta();
		Components.displayName(manaItemMeta, ChatColor.YELLOW + "Use Mana");
		Components.lore(manaItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.USE_MANA));
		manaItemStack.setItemMeta(manaItemMeta);
		menu.setItem(2, manaItemStack);
		
		ItemStack questsItemStack = GenericIconHeads.getAchievementQuestsItem();
		ItemMeta questsItemMeta = questsItemStack.getItemMeta();
		Components.displayName(questsItemMeta, ChatColor.YELLOW + "Complete Quests");
		Components.lore(questsItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.COMPLETE_QUESTS));
		questsItemStack.setItemMeta(questsItemMeta);
		menu.setItem(3, questsItemStack);
		
		ItemStack craftingItemStack = GenericIconHeads.getAchievementCraftingItem();
		ItemMeta craftingItemMeta = craftingItemStack.getItemMeta();
		Components.displayName(craftingItemMeta, ChatColor.YELLOW + "Craft Items");
		Components.lore(craftingItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.CRAFT_ITEMS));
		craftingItemStack.setItemMeta(craftingItemMeta);
		menu.setItem(4, craftingItemStack);
		
		ItemStack petsItemStack = GenericIconHeads.getAchievementPetsItem();
		ItemMeta petsItemMeta = petsItemStack.getItemMeta();
		Components.displayName(petsItemMeta, ChatColor.YELLOW + "Collect Pets");
		Components.lore(petsItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.COLLECT_PETS));
		petsItemStack.setItemMeta(petsItemMeta);
		menu.setItem(5, petsItemStack);
		
		ItemStack coinsItemStack = GenericIconHeads.getAchievementCoinsItem();
		ItemMeta coinsItemMeta = coinsItemStack.getItemMeta();
		Components.displayName(coinsItemMeta, ChatColor.YELLOW + "Earn Coins");
		Components.lore(coinsItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.EARN_COINS));
		coinsItemStack.setItemMeta(coinsItemMeta);
		menu.setItem(6, coinsItemStack);
		
		ItemStack playtimeItemStack = GenericIconHeads.getAchievementPlaytimeItem();
		ItemMeta playtimeItemMeta = playtimeItemStack.getItemMeta();
		Components.displayName(playtimeItemMeta, ChatColor.YELLOW + "Play Hours");
		Components.lore(playtimeItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.PLAY_HOURS));
		playtimeItemStack.setItemMeta(playtimeItemMeta);
		menu.setItem(7, playtimeItemStack);
		
		ItemStack levelsItemStack = GenericIconHeads.getAchievementLevelsItem();
		ItemMeta levelsItemMeta = levelsItemStack.getItemMeta();
		Components.displayName(levelsItemMeta, ChatColor.YELLOW + "Gain Levels");
		Components.lore(levelsItemMeta, AchievementTracker.generateProgressLores(player, WauzAchievementType.GAIN_LEVELS));
		levelsItemStack.setItemMeta(levelsItemMeta);
		menu.setItem(8, levelsItemStack);
		
		List<String> subMenuLores = new ArrayList<>();
		subMenuLores.add(ChatColor.GREEN + "Click to View");
		
		ItemStack artifactsItemStack = GenericIconHeads.getAchievementArtifactsItem();
		ItemMeta artifactsItemMeta = artifactsItemStack.getItemMeta();
		Components.displayName(artifactsItemMeta, ChatColor.GOLD + "Collected Artifacts");
		Components.lore(artifactsItemMeta, subMenuLores);
		artifactsItemStack.setItemMeta(artifactsItemMeta);
		menu.setItem(10, artifactsItemStack);
		
		ItemStack campaignsItemStack = GenericIconHeads.getAchievementCampaignsItem();
		ItemMeta campaignsItemMeta = campaignsItemStack.getItemMeta();
		Components.displayName(campaignsItemMeta, ChatColor.GOLD + "Completed Campaigns");
		Components.lore(campaignsItemMeta, subMenuLores);
		campaignsItemStack.setItemMeta(campaignsItemMeta);
		menu.setItem(12, campaignsItemStack);
		
		ItemStack bossesItemStack = GenericIconHeads.getAchievementBossesItem();
		ItemMeta bossesItemMeta = bossesItemStack.getItemMeta();
		Components.displayName(bossesItemMeta, ChatColor.GOLD + "Defeated Bosses");
		Components.lore(bossesItemMeta, subMenuLores);
		bossesItemStack.setItemMeta(bossesItemMeta);
		menu.setItem(14, bossesItemStack);
		
		ItemStack regionsItemStack = GenericIconHeads.getAchievementRegionsItem();
		ItemMeta regionsItemMeta = regionsItemStack.getItemMeta();
		Components.displayName(regionsItemMeta, ChatColor.GOLD + "Explored Regions");
		Components.lore(regionsItemMeta, subMenuLores);
		regionsItemStack.setItemMeta(regionsItemMeta);
		menu.setItem(16, regionsItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and lets the player navigate through achievement categories.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		final Player player = (Player) event.getWhoClicked();
		int slot = event.getRawSlot();
		
		if(slot == 10) {
			AchievementsMenuPage.open(player, WauzAchievementType.COLLECT_ARTIFACTS);
		}
		else if(slot == 12) {
			AchievementsMenuPage.open(player, WauzAchievementType.COMPLETE_CAMPAIGNS);
		}
		else if(slot == 14) {
			AchievementsMenuPage.open(player, WauzAchievementType.DEFEAT_BOSSES);
		}
		else if(slot == 16) {
			AchievementsMenuPage.open(player, WauzAchievementType.EXPLORE_REGIONS);
		}
	}

}
