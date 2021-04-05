package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventTitleBuy;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.WauzTitle;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.kyori.adventure.text.Component;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for selecting chat titles.
 * 
 * @author Wauzmons
 *
 * @see WauzTitle
 */
@PublicMenu
public class TitleMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "titles";
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
		TitleMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * A list of all titles to choose from will be shown.
	 * The menu lets the player choose from all unlocked titles.
	 * Locked titles can be bought here aswell, if requirements are met.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public static void open(Player player) {
		List<WauzTitle> titles = WauzTitle.getAllTitles();
		int size = MenuUtils.roundInventorySize(titles.size() + 18);
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Title Collection";
		Inventory menu = Components.inventory(new TitleMenu(), menuTitle, size);
		
		List<String> unlockedTitles = PlayerConfigurator.getCharacterTitleList(player);
		String currentTitle = PlayerConfigurator.getCharacterTitle(player);
		
		MenuUtils.setCurrencyDisplay(menu, player, 2);
		boolean defaultSelected = StringUtils.equals("default", currentTitle);
		menu.setItem(4, getTitleItemStack("default", WauzRank.getRank(player).getRankPrefix(), 1, 0, true, defaultSelected));
		boolean classSelected = StringUtils.equals("class", currentTitle);
		menu.setItem(6, getTitleItemStack("class", PlayerConfigurator.getCharacterClass(player), 1, 0, true, classSelected));
		
		int titleNumber = 9;
		for(WauzTitle title : titles) {
			if(titleNumber + 9 >= size) {
				break;
			}
			
			String titleName = title.getTitleName();
			String displayName = title.getTitleDisplayName();
			int level = title.getTitleLevel();
			int cost = title.getTitleCost();
			boolean unlocked = unlockedTitles.contains(titleName);
			boolean selected = StringUtils.equals(titleName, currentTitle);
			menu.setItem(titleNumber, getTitleItemStack(titleName, displayName, level, cost, unlocked, selected));
			titleNumber++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Creates an item stack, that represents a chat title.
	 * Used for showing slectable titles in the title menu.
	 * 
	 * @param titleName The key of the title.
	 * @param displayName The chat display name of the title.
	 * @param level The required level of the title.
	 * @param cost The soulstone cost of the title.
	 * @param unlocked If the title has been unlocked.
	 * @param selected If the title is currently selected.
	 * 
	 * @return The title item stack.
	 */
	private static ItemStack getTitleItemStack(String titleName, String displayName, int level, int cost, boolean unlocked, boolean selected) {
		ItemStack titleItemStack = unlocked ? MenuIconHeads.getTitlesItem() : GenericIconHeads.getDeclineItem();
		ItemMeta titleItemMeta = titleItemStack.getItemMeta();
		titleItemMeta.displayName(Component.text(unlocked ? ChatColor.GREEN + "Unlocked" : ChatColor.RED + "Locked"));
		List<String> titleLores = new ArrayList<>();
		titleLores.add(ChatColor.YELLOW + "Title: " + displayName);
		titleLores.add(ChatColor.GRAY + "Title-ID: " + titleName);
		titleLores.add(ChatColor.GRAY + "Required Level: " + level);
		titleLores.add("");
		if(unlocked) {
			if(selected) {
				titleLores.add(ChatColor.GRAY + "Currently Selected");
				titleItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
				titleItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			else {
				titleLores.add(ChatColor.GRAY + "Click to Select");
			}
		}
		else {
			titleLores.add(ChatColor.GRAY + "Click to Buy for " + cost + " Soulstones");
		}
		titleItemMeta.setLore(titleLores);
		titleItemStack.setItemMeta(titleItemMeta);
		return titleItemStack;
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If an already owned title is clicked, it will be selected.
	 * If another title is clicked and requirements are met, it will be bought.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PlayerConfigurator#setCharacterTitle(Player, String)
	 * @see WauzPlayerEventTitleBuy
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !ItemUtils.hasLore(clicked)) {
			return;
		}
		
		int level = PlayerCollectionConfigurator.getCharacterLevel(player);
		int requiredLevel = ItemUtils.getIntegerFromLore(clicked, "Required Level", 2);
		String titleName = ItemUtils.getStringFromLore(clicked, "Title-ID", 1);
		if(level < requiredLevel) {
			player.sendMessage(ChatColor.RED + "You don't meet the level requirement for this title yet!");
			player.closeInventory();
			return;
		}
		
		if(HeadUtils.isHeadMenuItem(clicked, "Unlocked")) {
			PlayerConfigurator.setCharacterTitle(player, titleName);
			String newTitle = WauzTitle.getTitle(player);
			if(StringUtils.isBlank(newTitle)) {
				newTitle = "Default / Rank";
			}
			player.sendMessage(ChatColor.GREEN + "Your chat title was changed to \"" + newTitle + "\"!");
			TitleMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Locked")) {
			WauzTitle title = WauzTitle.getTitle(titleName);
			long souls = PlayerCollectionConfigurator.getCharacterSoulstones(player);
			if(souls < title.getTitleCost()) {
				player.sendMessage(ChatColor.RED + "You don't have enough soulstones to unlock this title!");
				player.closeInventory();
				return;
			}
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			playerData.getSelections().setWauzPlayerEventName("Buy Title");
			playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventTitleBuy(title));
			ItemStack titleItemStack = MenuIconHeads.getTitlesItem();
			MenuUtils.setItemDisplayName(titleItemStack, ChatColor.YELLOW + "Title: " + title.getTitleDisplayName());
			MenuUtils.addItemLore(titleItemStack, ChatColor.GRAY + "Cost: " + title.getTitleCost() + " Soulstones", true);
			WauzDialog.open(player, titleItemStack);
		}
	}

}
