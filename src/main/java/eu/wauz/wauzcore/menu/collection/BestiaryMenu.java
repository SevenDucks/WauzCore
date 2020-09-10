package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerBestiaryConfigurator;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.mobs.bestiary.ObservationRank;
import eu.wauz.wauzcore.mobs.bestiary.ObservationTracker;
import eu.wauz.wauzcore.mobs.bestiary.WauzBestiaryCategory;
import eu.wauz.wauzcore.mobs.bestiary.WauzBestiaryEntry;
import eu.wauz.wauzcore.mobs.bestiary.WauzBestiarySpecies;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that displays info about defeated mobs.
 * 
 * @author Wauzmons
 *
 * @see WauzBestiaryEntry
 */
public class BestiaryMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "bestiary";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		BestiaryMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows a selection of all categories in the bestiary.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzBestiaryCategory
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new BestiaryMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Bestiary - Categories");
		
		int slot = 1;
		for(WauzBestiaryCategory category : WauzBestiaryCategory.values()) {
			ItemStack categoryItemStack = new ItemStack(Material.BOOKSHELF);
			MenuUtils.setItemDisplayName(categoryItemStack, ChatColor.YELLOW + "Category: " + category.toString());
			menu.setItem(slot, categoryItemStack);
			slot++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows a selection of all species in the bestiary category.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzBestiarySpecies
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public void showCategorySpecies(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(this);
		List<WauzBestiarySpecies> categorySpecies = WauzBestiarySpecies.getSpecies(category);
		int size = MenuUtils.roundInventorySize(categorySpecies.size() + 1);
		Inventory menu = Bukkit.createInventory(holder, size, ChatColor.BLACK + "" + ChatColor.BOLD + "Bestiary - " + category.toString());
		page = "species";
		
		ItemStack categoriesItemStack = new ItemStack(Material.BOOKSHELF);
		MenuUtils.setItemDisplayName(categoriesItemStack, ChatColor.GOLD + "Back to Category Selection");
		menu.setItem(0, categoriesItemStack);
		
		int slot = 1;
		for(WauzBestiarySpecies species : categorySpecies) {
			ItemStack speciesItemStack = new ItemStack(Material.BOOK);
			MenuUtils.setItemDisplayName(speciesItemStack, ChatColor.YELLOW + "Species: " + species.getSpeciesName());
			List<String> speciesLores = new ArrayList<>();
			for(String textPart : UnicodeUtils.wrapText(species.getSpeciesDescription())) {
				speciesLores.add(ChatColor.WHITE + textPart);
			}
			speciesItemStack.setLore(speciesLores);
			menu.setItem(slot, speciesItemStack);
			slot++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows a selection of all entries in the bestiary species.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzBestiaryEntry
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public void showSpeciesEntries(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(this);
		List<WauzBestiaryEntry> speciesEntries = species.getEntries();
		int size = MenuUtils.roundInventorySize(speciesEntries.size() + 2);
		Inventory menu = Bukkit.createInventory(holder, size, ChatColor.BLACK + "" + ChatColor.BOLD + "Bestiary - " + species.getSpeciesName());
		page = "entries";
		
		ItemStack categoriesItemStack = new ItemStack(Material.BOOKSHELF);
		MenuUtils.setItemDisplayName(categoriesItemStack, ChatColor.GOLD + "Back to Category Selection");
		menu.setItem(0, categoriesItemStack);
		
		ItemStack speciesItemStack = new ItemStack(Material.BOOK);
		MenuUtils.setItemDisplayName(speciesItemStack, ChatColor.GOLD + "Back to Species Selection");
		menu.setItem(1, speciesItemStack);
		
		int slot = 2;
		for(WauzBestiaryEntry entry : species.getEntries()) {
			menu.setItem(slot, getEntryItemStack(player, entry));
			slot++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Generates an item stack for displaying information about a bestiary entry.
	 * 
	 * @param player The player looking at the information.
	 * @param entry The entry that will be shown.
	 * 
	 * @return The generated item stack.
	 */
	public ItemStack getEntryItemStack(Player player, WauzBestiaryEntry entry) {
		int mobKills = PlayerBestiaryConfigurator.getBestiaryKills(player, entry.getEntryFullName());
		if(mobKills < 1) {
			ItemStack entryItemStack = GenericIconHeads.getUnknownItem();
			MenuUtils.setItemDisplayName(entryItemStack, ChatColor.GRAY + "???");
			return entryItemStack;
		}
		ObservationRank currentRank = ObservationRank.getObservationRank(mobKills, entry.isBoss());
		ItemStack entryItemStack = currentRank.getIconItemStack();
		ItemMeta entryItemMeta = entryItemStack.getItemMeta();
		entryItemMeta.setDisplayName(entry.getEntryMobDisplayName());
		List<String> entryLores = new ArrayList<>();
		
		for(int index = 1; index < ObservationRank.values().length; index++) {
			ObservationRank rank = ObservationRank.values()[index];
			if(currentRank.getRankTier() < rank.getRankTier()) {
				break;
			}
			entryLores.addAll(entry.getRankLores(rank.getRankTier()));
		}
		if(!entryLores.isEmpty()) {
			entryLores.add("");
		}
		entryLores.addAll(ObservationTracker.generateProgressLore(player, entry));
		entryItemMeta.setLore(entryLores);
		entryItemStack.setItemMeta(entryItemMeta);
		return entryItemStack;
	}
	
	/**
	 * The name of the current bestiary page view.
	 */
	private String page = "categories";
	
	/**
	 * The currently selected category.
	 */
	private WauzBestiaryCategory category;
	
	/**
	 * The currently selected species.
	 */
	private WauzBestiarySpecies species;
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and lets the player navigate through the bestiary.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		int slot = event.getRawSlot();
		
		if(slot < 0) {
			return;
		}
		else if(page.equals("categories") && slot > 0 && slot < 8) {
			String clickedName = clicked.getItemMeta().getDisplayName();
			String categoryName = StringUtils.substringAfter(clickedName, "Category: ");
			category = WauzBestiaryCategory.valueOf(categoryName.toUpperCase());
			showCategorySpecies(player);
		}
		else if(page.equals("species") && slot < WauzBestiarySpecies.getSpecies(category).size() + 1) {
			if(slot == 0) {
				open(player);
				return;
			}
			List<WauzBestiarySpecies> categorySpecies = WauzBestiarySpecies.getSpecies(category);
			species = categorySpecies.get(slot - 1);
			showSpeciesEntries(player);
		}
		else if(page.equals("entries")) {
			if(slot == 0) {
				open(player);
			}
			else if(slot == 1) {
				showCategorySpecies(player);
			}
		}
	}

}
