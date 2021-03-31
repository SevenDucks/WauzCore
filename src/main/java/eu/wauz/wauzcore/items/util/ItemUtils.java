package eu.wauz.wauzcore.items.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * An util class for reading and writing item stack properties.
 * 
 * @author Wauzmons
 * 
 * @see EquipmentUtils
 * @see FoodUtils
 * @see PetEggUtils
 */
public class ItemUtils {
	
	/**
	 * Checks if an item stack is neither null or air.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is not air.
	 */
	public static boolean isNotAir(ItemStack itemStack) {
		return itemStack != null && !itemStack.getType().equals(Material.AIR);
	}
	
	/**
	 * Checks if the item stack is not null and made out of a given material.
	 * 
	 * @param itemStack The item stack to check.
	 * @param material The material to check for.
	 * 
	 * @return If the item is made out of that material.
	 */
	public static boolean isMaterial(ItemStack itemStack, Material material) {
		return itemStack != null && itemStack.getType().equals(material);
	}
	
	/**
	 * Checks if enough space is left in an inventory, to fit in the given item stack.
	 * This is done by checking, if a free slot is available.
	 * If not, it is checked, if the items can be stacked with an already existing item stack.
	 * 
	 * @param inventory The inventory to iterate through.
	 * @param itemStack The item stack to check for.
	 * 
	 * @return If the item stack fits inside the inventory.
	 * 
	 * @see ItemUtils#countSpecificStackableItems(Inventory, ItemStack)
	 * @see ItemUtils#getAmountOfStacks(int, int)
	 */
	public static boolean fitsInInventory(Inventory inventory, ItemStack itemStack) {
		if(inventory.firstEmpty() >= 0) {
			return true;
		}
		if(!hasDisplayName(itemStack)) {
			return false;
		}
		
		int maxStackSize = itemStack.getMaxStackSize();
		int amountInInventory = countSpecificStackableItems(inventory, itemStack);
		
		int stacksBefore = getAmountOfStacks(maxStackSize, amountInInventory);
		int stacksAfter = getAmountOfStacks(maxStackSize, amountInInventory + itemStack.getAmount());
		return stacksBefore > 0 && stacksBefore == stacksAfter;
	}
	
	/**
	 * Counts how many stacks are needed to fit the given amount of items.
	 * 
	 * @param maxStackSize The maximum stack size of that item.
	 * @param itemAmount The amount of items.
	 * 
	 * @return How man stacks are needed to fit the items.
	 */
	private static int getAmountOfStacks(int maxStackSize, int itemAmount) {
		int stackAmount = 0;
		while(itemAmount > 0) {
			itemAmount -= maxStackSize;
			stackAmount++;
		}
		return stackAmount;
	}
	
	/**
	 * Counts the total amount of the given item stack inside an inventory.
	 * 
	 * @param inventory The inventory to iterate through.
	 * @param itemStack The item stack to check for.
	 * 
	 * @return The amount of times, the item was found inside the inventory.
	 * 
	 * @see ItemUtils#isSpecificItem(ItemStack, String)
	 */
	public static int countSpecificStackableItems(Inventory inventory, ItemStack itemStack) {
		int itemAmount = 0;
		for(ItemStack inventoryItemStack : inventory.getContents()) {			
			if(isSpecificItem(inventoryItemStack, itemStack.getItemMeta().getDisplayName()) && hasEqualLore(itemStack, inventoryItemStack)) {
				itemAmount += inventoryItemStack.getAmount();
			}
		}
		return itemAmount;
	}
	
	/**
	 * Checks if an item stack is a specific item, based on display name.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param itemName The item name to check for.
	 * 
	 * @return
	 */
	public static boolean isSpecificItem(ItemStack itemStack, String itemName) {
		return hasDisplayName(itemStack) && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).equals(ChatColor.stripColor(itemName));
	}
	
	/**
	 * Checks if an item stack possesses a banner meta.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is a banner.
	 */
	public static boolean isBanner(ItemStack itemStack) {
		return itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof BannerMeta;
	}
	
	/**
	 * Checks if an item stack is a material, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is a material.
	 */
	public static boolean isMaterialItem(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Material");
	}
	
	/**
	 * Checks if an item stack is a quest item, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is a quest item.
	 */
	public static boolean isQuestItem(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Quest Item");
	}
	
	/**
	 * Checks if an item stack is an ammo item, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is an ammo item.
	 * 
	 * @see ItemUtils#getArrowCount(ItemStack)
	 */
	public static boolean isAmmoItem(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Ammo Item");
	}
	
	/**
	 * Checks if an item stack is a bought item, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is a bought item.
	 * 
	 * @see ItemUtils#getSellValue(ItemStack)
	 */
	public static boolean isBoughtItem(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Bought");
	}
	
	/**
	 * Gets the amount of arrows from an item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The amount of arrows.
	 * 
	 * @see ItemUtils#isAmmoItem(ItemStack)
	 */
	public static int getArrowCount(ItemStack itemStack) {
		return hasLore(itemStack) ? getIntegerBetweenFromLore(itemStack, ChatColor.GRAY + "Amount Left: ", " Arrows") : 0;
	}
	
	/**
	 * Gets the sell value of an item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The sell value.
	 * 
	 * @see ItemUtils#isBoughtItem(ItemStack)
	 */
	public static int getSellValue(ItemStack itemStack) {
		if(!hasLore(itemStack)) {
			return 0;
		}
		boolean hasIcon = doesLoreContain(itemStack, UnicodeUtils.ICON_BULLSEYE);
		return getIntegerFromLore(itemStack, "Sell Value:" + ChatColor.DARK_GREEN, hasIcon ? 3 : 2) * itemStack.getAmount();
	}
	
	/**
	 * Checks if the item stack is an instance map, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item is an instance map.
	 * 
	 * @see ItemUtils#isInstanceMap(ItemStack)
	 */
	public static boolean isInstanceMap(ItemStack itemStack) {
		return hasLore(itemStack) && StringUtils.isNotBlank(getInstanceMapType(itemStack));
	}
	
	/**
	 * Gets the map type of an item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The instance map type.
	 * 
	 * @see ItemUtils#isInstanceMap(ItemStack)
	 */
	public static String getInstanceMapType(ItemStack itemStack) {
		return getStringBetweenFromLore(itemStack, "" + ChatColor.RED, " Map");
	}
	
	/**
	 * Checks if the given color is contained in an item stack's display name.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param chatColor The color to check for.
	 * 
	 * @return If the color is contained in the name.
	 */
	public static boolean hasColoredName(ItemStack itemStack, ChatColor chatColor) {
		return hasDisplayName(itemStack) && itemStack.getItemMeta().getDisplayName().contains(String.valueOf(chatColor));
	}
	
	/**
	 * Checks if the given item stack has a display name.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item has a name.
	 */
	public static boolean hasDisplayName(ItemStack itemStack) {
		return itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName();
	}
	
	/**
	 * Gets the display name of the given item stack.
	 * Includes partial null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The name of the item.
	 */
	public static String getDisplayName(ItemStack itemStack) {
		return hasDisplayName(itemStack) ? itemStack.getItemMeta().getDisplayName() : itemStack.getI18NDisplayName();
	}
	
	/**
	 * Checks if the given item has a lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item has a lore.
	 */
	public static boolean hasLore(ItemStack itemStack) {
		return itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore();
	}
	
	/**
	 * Checks if the given item stacks have the same lore.
	 * Includes null check.
	 * 
	 * @param itemStackA The first item stack to check.
	 * @param itemStackB The second item stack to check.
	 * 
	 * @return If the items have equal lore.
	 */
	public static boolean hasEqualLore(ItemStack itemStackA, ItemStack itemStackB) {
		return hasLore(itemStackA) && hasLore(itemStackB) && itemStackA.getItemMeta().getLore().equals(itemStackB.getItemMeta().getLore());
	}
	
	/**
	 * Checks if the lore of the item stack contains the given string.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param content The string to check for.
	 * 
	 * @return If the string was found.
	 */
	public static boolean doesLoreContain(ItemStack itemStack, String content) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore : lores) {
			if(lore.contains(content)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets a string from an item stack's lore, based on line content and word index.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param content The string to identify the line with.
	 * @param index The word index inside the found line.
	 * 
	 * @return The string on the found line, with the given word index.
	 */
	public static String getStringFromLore(ItemStack itemStack, String content, int index) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore : lores) {
			if(lore.contains(content)) {
				return lore.split(" ")[index];
			}
		}
		return null;
	}
	
	/**
	 * Finds a string between two other strings somewhere inside an item's lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param before The string before the wanted string.
	 * @param after The string after the wanted string.
	 * 
	 * @return The string between the other strings.
	 */
	public static String getStringBetweenFromLore(ItemStack itemStack, String before, String after) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore: lores) {
			if(lore.contains(before) && lore.contains(after)) {
				return StringUtils.substringBetween(lore, before, after);
			}
		}
		return null;
	}
	
	/**
	 * Gets an integer from an item stack's lore, based on line content and word index.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param content The string to identify the line with.
	 * @param index The word index inside the found line.
	 * 
	 * @return The int on the found line, with the given word index.
	 */
	public static int getIntegerFromLore(ItemStack itemStack, String content, int index) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore : lores) {
			if(lore.contains(content)) {
				return Integer.parseInt(lore.split(" ")[index]);
			}
		}
		return 0;
	}
	
	/**
	 * Finds an int between two strings somewhere inside an item's lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param before The string before the wanted int.
	 * @param after The string after the wanted int.
	 * 
	 * @return The int between the other strings.
	 */
	public static int getIntegerBetweenFromLore(ItemStack itemStack, String before, String after) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore: lores) {
			if(lore.contains(before) && lore.contains(after)) {
				return Integer.parseInt(StringUtils.substringBetween(lore, before, after));
			}
		}
		return 0;
	}
	
	/**
	 * Finds the sum of all integers between all occurances of two strings somewhere inside an item's lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param before The string before the wanted int.
	 * @param after The string after the wanted int.
	 * 
	 * @return The sum of all integers between occurances of the two strings.
	 */
	public static int getIntegerSumBetweenFromLore(ItemStack itemStack, String before, String after) {
		int integerSum = 0;
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore: lores) {
			if(lore.contains(before) && lore.contains(after)) {
				integerSum += Integer.parseInt(StringUtils.substringBetween(lore, before, after));
			}
		}
		return integerSum;
	}
	
	/**
	 * Gets a long from an item stack's lore, based on line content and word index.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * @param content The string to identify the line with.
	 * @param index The word index inside the found line.
	 * 
	 * @return The long on the found line, with the given word index.
	 */
	public static long getLongFromLore(ItemStack itemStack, String content, int index) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore : lores) {
			if(lore.contains(content)) {
				return Long.parseLong(lore.split(" ")[index]);
			}
		}
		return 0;
	}
	
	/**
	 * Replaces all occurances of a line inside an item stack's lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to edit.
	 * @param content The string to identify the line with.
	 * @param replacement The replacement of the content.
	 * 
	 * @return If at least one occurance was replaced.
	 */
	public static boolean replaceStringFromLore(ItemStack itemStack, String content, String replacement) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> lores = itemMeta.getLore();
		List<String> newLores = new ArrayList<>();
		boolean replaced = false;
		for(String lore : lores) {
			if(!replaced && lore.contains(content)) {
				lore = replacement;
				replaced = true;
			}
			newLores.add(lore);
		}
		itemMeta.setLore(newLores);
		itemStack.setItemMeta(itemMeta);
		return replaced;
	}
	
	/**
	 * Replaces a substring with given word index in all occurances of a string inside an item's lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to edit.
	 * @param content The string to identify the line with.
	 * @param index The word index of the substring to replace inside the found line.
	 * @param replacement The replacement of the content.
	 * 
	 * @return If at least one occurance was replaced.
	 */
	public static boolean replaceStringFromLore(ItemStack itemStack, String content, int index, String replacement) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> lores = itemMeta.getLore();
		List<String> newLores = new ArrayList<>();
		boolean replaced = false;
		for(String lore : lores) {
			if(!replaced && lore.contains(content)) {
				String[] splitString = lore.split(" ");
				splitString[index] = replacement;
				lore = StringUtils.join(splitString, " ");
				replaced = true;
			}
			newLores.add(lore);
		}
		itemMeta.setLore(newLores);
		itemStack.setItemMeta(itemMeta);
		return replaced;
	}
	
}
