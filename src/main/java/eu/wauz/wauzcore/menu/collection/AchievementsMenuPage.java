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

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import net.kyori.adventure.text.Component;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the achievements menu, showing a page of non generic achievements to the player.
 * 
 * @author Wauzmons
 * 
 * @see WauzAchievement
 */
public class AchievementsMenuPage implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "achievementpage";
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows completed achievements of a type, displayed as one item stack per achievement type.
	 * 
	 * @param player The player that should view the inventory.
	 * @param type The non generic achievement type to display.
	 * 
	 * @see WauzAchievement#getAchievementsOfType(WauzAchievementType)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, WauzAchievementType type) {
		List<WauzAchievement> achievements = WauzAchievement.getAchievementsOfType(type);
		List<String> awarded = PlayerCollectionConfigurator.getCharacterAchievementList(player, type);
		WauzInventoryHolder holder = new WauzInventoryHolder(new AchievementsMenuPage());
		int size = MenuUtils.roundInventorySize(achievements.size() + 1);
		Inventory menu = Bukkit.createInventory(holder, size, Component.text(ChatColor.BLACK + "" + ChatColor.BOLD + "Achievements"));
		
		ItemStack overviewItemStack = MenuIconHeads.getAchievementsItem();
		MenuUtils.setItemDisplayName(overviewItemStack, ChatColor.GOLD + "Back to Overview");
		menu.setItem(0, overviewItemStack);
		
		int slot = 1;
		for(WauzAchievement achievement : achievements) {
			boolean completed = awarded.contains(achievement.getKey());
			ItemStack achievementItemStack = completed ? GenericIconHeads.getConfirmItem() : GenericIconHeads.getDeclineItem();
			ItemMeta achievementItemMeta = achievementItemStack.getItemMeta();
			achievementItemMeta.displayName(Component.text(ChatColor.YELLOW + achievement.getName()));
			List<String> achievementLores = new ArrayList<>();
			String status = completed ? (ChatColor.GOLD + "COMPLETED") : (ChatColor.RED + "UNCOMPLETED");
			String goal = " \"" + achievement.getGoalString() + "\"";
			achievementLores.add(ChatColor.GREEN + "Status: " + status);
			achievementLores.add("");
			achievementLores.add(ChatColor.YELLOW + "Goal: " + achievement.getType().getMessage() + goal);
			if(!completed) {
				achievementLores.add(ChatColor.YELLOW + "Reward: " + achievement.getReward() + " Soulstones");
			}
			achievementItemMeta.setLore(achievementLores);
			achievementItemStack.setItemMeta(achievementItemMeta);
			menu.setItem(slot, achievementItemStack);
			slot++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and lets the player go back to the overview.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(HeadUtils.isHeadMenuItem(clicked, "Back to Overview")) {
			AchievementsMenu.open(player);
		}
	}

}
