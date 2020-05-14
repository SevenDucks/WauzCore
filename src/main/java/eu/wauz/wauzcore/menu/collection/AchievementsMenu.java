package eu.wauz.wauzcore.menu.collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that shows the player's progress towards achievements.
 * 
 * @author Wauzmons
 * 
 * @see WauzAchievement
 */
public class AchievementsMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "achievements";
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
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new AchievementsMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Achievements");
		
		ItemStack killsItemStack = HeadUtils.getAchievementKillsItem();
		ItemMeta killsItemMeta = killsItemStack.getItemMeta();
		killsItemMeta.setDisplayName(ChatColor.YELLOW + "Kill Enemies");
		killsItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.KILL_ENEMIES));
		killsItemStack.setItemMeta(killsItemMeta);
		menu.setItem(0, killsItemStack);
				
		ItemStack identifiesItemStack = HeadUtils.getAchievementIdentifiesItem();
		ItemMeta identifiesItemMeta = identifiesItemStack.getItemMeta();
		identifiesItemMeta.setDisplayName(ChatColor.YELLOW + "Identify Items");
		identifiesItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.IDENTIFY_ITEMS));
		identifiesItemStack.setItemMeta(identifiesItemMeta);
		menu.setItem(1, identifiesItemStack);
		
		ItemStack manaItemStack = HeadUtils.getAchievementManaItem();
		ItemMeta manaItemMeta = manaItemStack.getItemMeta();
		manaItemMeta.setDisplayName(ChatColor.YELLOW + "Use Mana");
		manaItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.USE_MANA));
		manaItemStack.setItemMeta(manaItemMeta);
		menu.setItem(2, manaItemStack);
		
		ItemStack questsItemStack = HeadUtils.getAchievementQuestsItem();
		ItemMeta questsItemMeta = questsItemStack.getItemMeta();
		questsItemMeta.setDisplayName(ChatColor.YELLOW + "Complete Quests");
		questsItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.COMPLETE_QUESTS));
		questsItemStack.setItemMeta(questsItemMeta);
		menu.setItem(3, questsItemStack);
		
		ItemStack craftingItemStack = HeadUtils.getAchievementCraftingItem();
		ItemMeta craftingItemMeta = craftingItemStack.getItemMeta();
		craftingItemMeta.setDisplayName(ChatColor.YELLOW + "Craft Items");
		craftingItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.CRAFT_ITEMS));
		craftingItemStack.setItemMeta(craftingItemMeta);
		menu.setItem(4, craftingItemStack);
		
		ItemStack petsItemStack = HeadUtils.getAchievementPetsItem();
		ItemMeta petsItemMeta = petsItemStack.getItemMeta();
		petsItemMeta.setDisplayName(ChatColor.YELLOW + "Collect Pets");
		petsItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.COLLECT_PETS));
		petsItemStack.setItemMeta(petsItemMeta);
		menu.setItem(5, petsItemStack);
		
		ItemStack coinsItemStack = HeadUtils.getAchievementCoinsItem();
		ItemMeta coinsItemMeta = coinsItemStack.getItemMeta();
		coinsItemMeta.setDisplayName(ChatColor.YELLOW + "Earn Coins");
		coinsItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.EARN_COINS));
		coinsItemStack.setItemMeta(coinsItemMeta);
		menu.setItem(6, coinsItemStack);
		
		ItemStack playtimeItemStack = HeadUtils.getAchievementPlaytimeItem();
		ItemMeta playtimeItemMeta = playtimeItemStack.getItemMeta();
		playtimeItemMeta.setDisplayName(ChatColor.YELLOW + "Play Hours");
		playtimeItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.PLAY_HOURS));
		playtimeItemStack.setItemMeta(playtimeItemMeta);
		menu.setItem(7, playtimeItemStack);
		
		ItemStack levelsItemStack = HeadUtils.getAchievementLevelsItem();
		ItemMeta levelsItemMeta = levelsItemStack.getItemMeta();
		levelsItemMeta.setDisplayName(ChatColor.YELLOW + "Gain Levels");
		levelsItemMeta.setLore(AchievementTracker.generateProgressLores(player, WauzAchievementType.GAIN_LEVELS));
		levelsItemStack.setItemMeta(levelsItemMeta);
		menu.setItem(8, levelsItemStack);
		
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * This menu has no events yet, so the click is canceled.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
	}

}
