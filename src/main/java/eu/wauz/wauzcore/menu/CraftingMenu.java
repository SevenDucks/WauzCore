package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.items.InventoryItemRemover;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import net.md_5.bungee.api.ChatColor;

public class CraftingMenu implements WauzInventory {
	
	private static final int MIN_PAGE = 1;
	
	private static final int MAX_PAGE = 2;
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new CraftingMenu());
		Integer playerCraftingLevel = PlayerConfigurator.getCharacterCraftingSkill(player);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Crafting Menu" +
				ChatColor.DARK_RED + ChatColor.BOLD + " Level " + playerCraftingLevel);
		
		for(int index = 1; index <= MAX_PAGE; index++) {
			ItemStack page = new ItemStack(Material.FLINT_AND_STEEL);
			ItemMeta im = page.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Page " + index);
			page.setItemMeta(im);
			menu.setItem(index - 1, page);
		}
		for(int index = MAX_PAGE + 1; index <= 9; index++) {
			MenuUtils.setComingSoon(menu, null, index - 1);
		}
		
		player.openInventory(menu);
	}
	
	public static void listRecipes(Player player, int page) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new CraftingMenu());
		Integer playerCraftingLevel = PlayerConfigurator.getCharacterCraftingSkill(player);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Crafting Page "
				+ page + " " + ChatColor.DARK_RED + ChatColor.BOLD + " Level " + playerCraftingLevel);
		
		for(int index = 0; index < 6; index++) {
			generateRecipe(menu, player, playerCraftingLevel, index + ((page - 1) * 6), index);
		}
		
		{
			ItemStack move = HeadUtils.getPrevItem();
			ItemMeta im = move.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "Page PREV");
			move.setItemMeta(im);
			menu.setItem(6, move);
		}
		
		{
			ItemStack move = HeadUtils.getNextItem();
			ItemMeta im = move.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "Page NEXT");
			move.setItemMeta(im);
			menu.setItem(8, move);
		}
		
		{
			ItemStack back = new ItemStack(Material.FLINT_AND_STEEL);
			ItemMeta im = back.getItemMeta();
			im.setDisplayName(ChatColor.BLUE + "Back to Page-Selection");
			back.setItemMeta(im);
			menu.setItem(7, back);
		}
		
		player.openInventory(menu);
	}
	
	public static void generateRecipe(Inventory menu, Player player, Integer playerCraftingLevel, int itemIndex, int baseIndex) {
		String material = CraftingConfigurator.getItemMaterial(itemIndex);
		if(StringUtils.isBlank(material)) {
			MenuUtils.setComingSoon(menu, ChatColor.DARK_GRAY + "Index: " + itemIndex, baseIndex);
			return;
		}
		
		String name = ChatColor.translateAlternateColorCodes('§', CraftingConfigurator.getItemName(itemIndex));
		List<String> lores = CraftingConfigurator.getItemLores(itemIndex);
		int amount = CraftingConfigurator.getItemAmount(itemIndex);
		int level = CraftingConfigurator.getItemLevel(itemIndex);
		int requirements = CraftingConfigurator.getItemCraftingCostsAmount(itemIndex);
		
		if(level > playerCraftingLevel && !player.hasPermission("wauz.debug.crafting")) {
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
				if((itemStack != null) && ItemUtils.isMaterial(itemStack) && ItemUtils.isSpecificItem(itemStack, itemName))
					itemAmount = itemAmount + itemStack.getAmount();
			}
			
			if(itemAmount >= requiredAmount || player.hasPermission("wauz.debug.crafting"))
				finished = ChatColor.GREEN;
			
			lores.add(finished + "- " + itemAmount + " / " + requiredAmount + " " + itemName);
		}
		lores.add("");
		lores.add(ChatColor.DARK_GRAY + "Index: " + itemIndex);
		
		if((level + 10) >= playerCraftingLevel && playerCraftingLevel < WauzCore.MAX_CRAFTING_SKILL) 
			lores.add(ChatColor.YELLOW + "Increases Crafting Level");
		
		ItemStack stack = new ItemStack(Material.getMaterial(material), amount);
		ItemMeta im = stack.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lores);
		if(im instanceof PotionMeta) {
			PotionEffectType potionType  = CraftingConfigurator.getPotionType(itemIndex);
			int potionLevel = CraftingConfigurator.getPotionLevel(itemIndex);
			int potionDuration = CraftingConfigurator.getPotionDuration(itemIndex);
			((PotionMeta) im).addCustomEffect(new PotionEffect(potionType, potionDuration, potionLevel), true);
		}
		stack.setItemMeta(im);
		menu.setItem(baseIndex, stack);
	}
	
	public static void getLocked(Inventory menu, int level, int itemIndex, int baseIndex) {
		ItemStack lock = new ItemStack(Material.BARRIER);
		ItemMeta im = lock.getItemMeta();
		im.setDisplayName(ChatColor.RED + "Recipe Locked");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.YELLOW + "Required Crafting Level: " + level);
		lores.add("");
		lores.add(ChatColor.DARK_GRAY + "Index: " + itemIndex);
		im.setLore(lores);
		lock.setItemMeta(im);
		menu.setItem(baseIndex, lock);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null ||
			!clicked.hasItemMeta() ||
			!clicked.getItemMeta().hasDisplayName())
			return;
		
		if(clicked.getType().equals(Material.FLINT_AND_STEEL)) {
			String clickedName = clicked.getItemMeta().getDisplayName();
			if(clickedName.contains("Page ")) {
				int page = Integer.parseInt(StringUtils.substringAfter(clickedName, "Page "));
				listRecipes(player, page);
			}
			else
				open(player);
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
		else if(clicked.getType().equals(Material.SIGN)) {
			return;
		}
		
		if(!clicked.getItemMeta().hasLore())
			return;
		
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
			else if(lore.contains("Increases Crafting Level"))
				increasesLevel = true;
			else if(lore.contains(ChatColor.RED + "- "))
				missingMaterial = true;
		}
		
		if(!valid)
			return;
		
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
		itemMeta.setDisplayName(clicked.getItemMeta().getDisplayName());
		itemMeta.setLore(CraftingConfigurator.getItemLores(itemIndex));
		itemStack.setItemMeta(itemMeta);
		
		if(!ItemUtils.fitsInInventory(player.getInventory(), itemStack)) {
			player.sendMessage(ChatColor.RED + "Your inventory is full!");
			player.closeInventory();
			return;
		}
		
		if(!player.hasPermission("wauz.debug.crafting")) {
			InventoryItemRemover itemRemover = new InventoryItemRemover(player.getInventory());
			int requirements = CraftingConfigurator.getItemCraftingCostsAmount(itemIndex);
			for(int requirementIndex = 1; requirementIndex <= requirements; requirementIndex++) {
				String itemName = CraftingConfigurator.getCraftingCostItemString(itemIndex, requirementIndex);			
				int requiredAmount = CraftingConfigurator.getCraftingCostItemAmount(itemIndex, requirementIndex);
				itemRemover.addItemNameToRemove(itemName, requiredAmount);
			}
			itemRemover.execute();
		}
		
		if(increasesLevel)
			PlayerConfigurator.increaseCharacterCraftingSkill(player);
		player.getInventory().addItem(itemStack);
		
		listRecipes(player, getIndex(player.getOpenInventory()));
	}
	
	private static int getIndex(InventoryView inventory) {
		return Integer.parseInt(StringUtils.substringBetween(inventory.getTitle(), "Page ", " "));
	}
	
}
