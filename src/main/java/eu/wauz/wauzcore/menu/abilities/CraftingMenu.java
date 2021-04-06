package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.CraftingConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.items.InventoryItemRemover;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the abilities menu, that is used for crafting items out of materials.
 * 
 * @author Wauzmons
 * 
 * @see CraftingConfigurator
 */
@PublicMenu
public class CraftingMenu implements WauzInventory {
	
	/**
	 * The first page index of the craftable item list.
	 */
	private static final int MIN_PAGE = 1;
	
	/**
	 * The last page index of the craftable item list.
	 */
	private static final int MAX_PAGE = 2;
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "crafting";
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
		CraftingMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows a list of all pages of craftable items, to choose from.
	 * Based on the min and max page constants.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public static void open(Player player) {
		Integer playerCraftingLevel = PlayerSkillConfigurator.getCraftingSkill(player);
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Crafting Menu" +
				ChatColor.DARK_RED + ChatColor.BOLD + " Level " + playerCraftingLevel;
		Inventory menu = Components.inventory(new CraftingMenu(), menuTitle, 9);
		
		for(int index = 1; index <= MAX_PAGE; index++) {
			ItemStack pageItemStack = new ItemStack(Material.FLINT_AND_STEEL);
			ItemMeta pageItemMeta = pageItemStack.getItemMeta();
			Components.displayName(pageItemMeta, ChatColor.GOLD + "Page " + index);
			pageItemStack.setItemMeta(pageItemMeta);
			menu.setItem(index - 1, pageItemStack);
		}
		for(int index = MAX_PAGE + 1; index <= 9; index++) {
			MenuUtils.setComingSoon(menu, null, index - 1);
		}
		
		player.openInventory(menu);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows the list of craftable items, with the given page index.
	 * Lets the player craft an item, by clicking on it or switch the page, by clicking on prev/next buttons.
	 * 
	 * @param player The player that should view the inventory.
	 * @param page The page index of the craftable item list.
	 * 
	 * @see CraftingMenu#generateRecipe(Inventory, Player, Integer, int, int)
	 */
	public static void listRecipes(Player player, int page) {
		Integer playerCraftingLevel = PlayerSkillConfigurator.getCraftingSkill(player);
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Crafting Page "
				+ page + " " + ChatColor.DARK_RED + ChatColor.BOLD + " Level " + playerCraftingLevel;
		Inventory menu = Components.inventory(new CraftingMenu(), menuTitle, 9);
		
		for(int index = 0; index < 6; index++) {
			generateRecipe(menu, player, playerCraftingLevel, index + ((page - 1) * 6), index);
		}
		
		ItemStack prevItemStack = GenericIconHeads.getPrevItem();
		ItemMeta prevItemMeta = prevItemStack.getItemMeta();
		Components.displayName(prevItemMeta, ChatColor.YELLOW + "Page PREV");
		prevItemStack.setItemMeta(prevItemMeta);
		menu.setItem(6, prevItemStack);
	
		ItemStack nextItemStack = GenericIconHeads.getNextItem();
		ItemMeta nextItemMeta = nextItemStack.getItemMeta();
		Components.displayName(nextItemMeta, ChatColor.YELLOW + "Page NEXT");
		nextItemStack.setItemMeta(nextItemMeta);
		menu.setItem(8, nextItemStack);
	
		ItemStack backItemStack = new ItemStack(Material.FLINT_AND_STEEL);
		ItemMeta backItemMeta = backItemStack.getItemMeta();
		Components.displayName(backItemMeta, ChatColor.BLUE + "Back to Page-Selection");
		backItemStack.setItemMeta(backItemMeta);
		menu.setItem(7, backItemStack);
		
		player.openInventory(menu);
	}
	
	/**
	 * Creates a recipe to show in the given menu.
	 * If the player has not reached the needed crafting skill level, it will be displayed as locked.
	 * Shows the needed materials to craft the item, and if a craft can increase the skill level.
	 * Players in crafting debug mode will be able to see and craft everything.
	 * 
	 * @param menu The menu that should display the recipe.
	 * @param player The player that should view the inventory.
	 * @param playerCraftingLevel The crafting skill level of the player.
	 * @param itemIndex The item index in the crafting config.
	 * @param baseIndex The item index as shown in the menu.
	 * 
	 * @see CraftingConfigurator
	 * @see CraftingMenu#getLocked(Inventory, int, int, int)
	 * @see WauzDebugger#toggleCraftingDebugMode(Player)
	 */
	public static void generateRecipe(Inventory menu, Player player, Integer playerCraftingLevel, int itemIndex, int baseIndex) {
		String material = CraftingConfigurator.getItemMaterial(itemIndex);
		if(StringUtils.isBlank(material)) {
			MenuUtils.setComingSoon(menu, ChatColor.DARK_GRAY + "Index: " + itemIndex, baseIndex);
			return;
		}
		
		String name = CraftingConfigurator.getItemName(itemIndex);
		List<String> lores = CraftingConfigurator.getItemLores(itemIndex);
		int amount = CraftingConfigurator.getItemAmount(itemIndex);
		int level = CraftingConfigurator.getItemLevel(itemIndex);
		int requirements = CraftingConfigurator.getItemCraftingCostsAmount(itemIndex);
		
		if(level > playerCraftingLevel && !player.hasPermission(WauzPermission.DEBUG_CRAFTING.toString())) {
			getLocked(menu, level, itemIndex, baseIndex);
			return;
		}
		
		lores.add("");
		for(int requirementIndex = 1; requirementIndex <= requirements; requirementIndex++) {
			String itemName = CraftingConfigurator.getCraftingCostItemString(itemIndex, requirementIndex);		
			int requiredAmount = CraftingConfigurator.getCraftingCostItemAmount(itemIndex, requirementIndex);
			int itemAmount = 0;
			ChatColor finished = ChatColor.RED;
			
			for(ItemStack itemStack : player.getInventory().getContents()) {
				if((itemStack != null) && ItemUtils.isMaterialItem(itemStack) && ItemUtils.isSpecificItem(itemStack, itemName))
					itemAmount = itemAmount + itemStack.getAmount();
			}
			
			if(itemAmount >= requiredAmount || player.hasPermission(WauzPermission.DEBUG_CRAFTING.toString())) {
				finished = ChatColor.GREEN;
			}
			
			lores.add(finished + "- " + itemAmount + " / " + requiredAmount + " " + itemName);
		}
		lores.add("");
		lores.add(ChatColor.DARK_GRAY + "Index: " + itemIndex);
		
		if((level + 10) >= playerCraftingLevel && playerCraftingLevel < WauzCore.MAX_CRAFTING_SKILL) {
			lores.add(ChatColor.YELLOW + "Increases Crafting Level");
		}
		
		ItemStack recipeItemStack = new ItemStack(Material.getMaterial(material), amount);
		ItemMeta recipeItemMeta = recipeItemStack.getItemMeta();
		Components.displayName(recipeItemMeta, name);
		Components.lore(recipeItemMeta, lores);
		if(recipeItemMeta instanceof PotionMeta) {
			PotionEffectType potionType  = CraftingConfigurator.getPotionType(itemIndex);
			int potionLevel = CraftingConfigurator.getPotionLevel(itemIndex);
			int potionDuration = CraftingConfigurator.getPotionDuration(itemIndex);
			((PotionMeta) recipeItemMeta).addCustomEffect(new PotionEffect(potionType, potionDuration, potionLevel), true);
		}
		recipeItemStack.setItemMeta(recipeItemMeta);
		menu.setItem(baseIndex, recipeItemStack);
	}
	
	/**
	 * Creates a locked recipe to show in the given menu.
	 * This item can not be crafted from the menu.
	 * 
	 * @param menu The menu that should display the locked recipe.
	 * @param level The level needed to unlock the recipe.
	 * @param itemIndex The item index in the crafting config.
	 * @param baseIndex The item index as shown in the menu.
	 */
	public static void getLocked(Inventory menu, int level, int itemIndex, int baseIndex) {
		ItemStack lockedItemStack = new ItemStack(Material.BARRIER);
		ItemMeta lockedItemMeta = lockedItemStack.getItemMeta();
		Components.displayName(lockedItemMeta, ChatColor.RED + "Recipe Locked");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.YELLOW + "Required Crafting Level: " + level);
		lores.add("");
		lores.add(ChatColor.DARK_GRAY + "Index: " + itemIndex);
		Components.lore(lockedItemMeta, lores);
		lockedItemStack.setItemMeta(lockedItemMeta);
		menu.setItem(baseIndex, lockedItemStack);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and initiates the corresponding craft or displays the selected page.
	 * Flint and steel is used to select a page directly, while prev/next buttons can turn one page.
	 * Items cannot be crafted if the recipe is locked or is missing materials.
	 * Once the crafting has been started, the materials are removed (only if not in crafting debug mode)
	 * and the crafting skill level might be increased, depending on the recipe level shown in lore.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see CraftingConfigurator
	 * @see CraftingMenu#open(Player)
	 * @see CraftingMenu#listRecipes(Player, int)
	 * @see CraftingMenu#tryToCraftItem(ItemStack, Player)
	 * @see PlayerSkillConfigurator#increaseCraftingSkill(Player)
	 * @see WauzDebugger#toggleCraftingDebugMode(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(!ItemUtils.hasDisplayName(clicked)) {
			return;
		}
		if(clicked.getType().equals(Material.FLINT_AND_STEEL)) {
			String clickedName = clicked.getItemMeta().getDisplayName();
			if(clickedName.contains("Page ")) {
				int page = Integer.parseInt(StringUtils.substringAfter(clickedName, "Page "));
				listRecipes(player, page);
			}
			else {
				open(player);
			}
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Page PREV")) {
			int index = getIndex(player.getOpenInventory());
			index = index > MIN_PAGE ? index - 1 : MAX_PAGE;
			listRecipes(player, index);
			return;
		}		
		else if(HeadUtils.isHeadMenuItem(clicked, "Page NEXT")) {
			int index = getIndex(player.getOpenInventory());
			index = index < MAX_PAGE ? index + 1 : MIN_PAGE;
			listRecipes(player, index);
			return;
		}
		else if(clicked.getType().equals(Material.OAK_SIGN) || !clicked.getItemMeta().hasLore()) {
			return;
		}
		tryToCraftItem(clicked, player);
	}

	/**
	 * Tries to craft the given item.
	 * 
	 * @param clicked The item that should be crafted.
	 * @param player The player crafting the item.
	 * 
	 * @see CraftingMenu#selectMenuPoint(InventoryClickEvent)
	 */
	private void tryToCraftItem(ItemStack clicked, final Player player) {
		int itemIndex = 0;
		boolean valid = false;
		boolean increasesLevel = false;
		boolean missingMaterial = false;
		
		List<String> lores = clicked.getItemMeta().getLore();
		for(String lore : lores) {
			if(lore.contains("Index")) {
				String[] val = lore.split(" ");
				itemIndex = Integer.parseInt(val[1]);
				valid = true;
			}
			else if(lore.contains("Increases Crafting Level")) {
				increasesLevel = true;
			}
			else if(lore.contains(ChatColor.RED + "- ")) {
				missingMaterial = true;
			}
		}
		
		if(!valid) {
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(ChatColor.RED + "Recipe Locked")) {
			player.sendMessage(ChatColor.RED + "You haven't unlocked this recipe yet!");
			player.closeInventory();
			return;
		}
		if(missingMaterial) {
			player.sendMessage(ChatColor.RED + "You don't have the materials to craft this!");
			player.closeInventory();
			return;
		}
		
		ItemStack itemStack = new ItemStack(clicked.getType(), clicked.getAmount());
		ItemMeta itemMeta = clicked.getItemMeta();
		Components.displayName(itemMeta, clicked.getItemMeta().getDisplayName());
		Components.lore(itemMeta, CraftingConfigurator.getItemLores(itemIndex));
		itemStack.setItemMeta(itemMeta);
		
		if(!ItemUtils.fitsInInventory(player.getInventory(), itemStack)) {
			player.sendMessage(ChatColor.RED + "Your inventory is full!");
			player.closeInventory();
			return;
		}
		if(!player.hasPermission(WauzPermission.DEBUG_CRAFTING.toString())) {
			InventoryItemRemover itemRemover = new InventoryItemRemover(player.getInventory());
			int requirements = CraftingConfigurator.getItemCraftingCostsAmount(itemIndex);
			for(int requirementIndex = 1; requirementIndex <= requirements; requirementIndex++) {
				String itemName = CraftingConfigurator.getCraftingCostItemString(itemIndex, requirementIndex);			
				int requiredAmount = CraftingConfigurator.getCraftingCostItemAmount(itemIndex, requirementIndex);
				itemRemover.addItemNameToRemove(itemName, requiredAmount);
			}
			itemRemover.execute();
		}
		if(increasesLevel) {
			PlayerSkillConfigurator.increaseCraftingSkill(player);
		}
		player.getInventory().addItem(WauzNmsClient.nmsSerialize(itemStack));
		AchievementTracker.addProgress(player, WauzAchievementType.CRAFT_ITEMS, 1);
		listRecipes(player, getIndex(player.getOpenInventory()));
	}
	
	/**
	 * Gets the page index from the title of the given crafting inventory.
	 * 
	 * @param inventory The inventory to fetch the page index from.
	 * 
	 * @return The page index.
	 */
	private static int getIndex(InventoryView inventory) {
		return Integer.parseInt(StringUtils.substringBetween(inventory.getTitle(), "Page ", " "));
	}
	
}
