package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemUtils {
	
	public static boolean isNotAir(ItemStack itemStack) {
		return itemStack != null && !itemStack.getType().equals(Material.AIR);
	}
	
	public static boolean isMaterial(ItemStack itemStack, Material material) {
		return itemStack != null & itemStack.getType().equals(material);
	}
	
	public static boolean fitsInInventory(Inventory inventory, ItemStack itemStack) {
		if(inventory.firstEmpty() >= 0)
			return true;
		if(!hasDisplayName(itemStack))
			return false;
		
		int maxStackSize = itemStack.getMaxStackSize();
		int amountInInventory = countSpecificStackableItems(inventory, itemStack);
		
		int stacksBefore = getAmountOfStacks(maxStackSize, amountInInventory);
		int stacksAfter = getAmountOfStacks(maxStackSize, amountInInventory + itemStack.getAmount());
		return stacksBefore > 0 && stacksBefore == stacksAfter;
	}
	
	private static int getAmountOfStacks(int maxStackSize, int itemAmount) {
		int stackAmount = 0;
		while(itemAmount > 0) {
			itemAmount -= maxStackSize;
			stackAmount++;
		}
		return stackAmount;
	}
	
	public static int countSpecificStackableItems(Inventory inventory, ItemStack itemStack) {
		int itemAmount = 0;
		for(ItemStack inventoryItemStack : inventory.getContents()) {			
			if(isSpecificItem(inventoryItemStack, itemStack.getItemMeta().getDisplayName()) && hasEqualLore(itemStack, inventoryItemStack))
				itemAmount += inventoryItemStack.getAmount();
		}
		return itemAmount;
	}
	
	public static boolean isSpecificItem(ItemStack itemStack, String itemName) {
		return hasDisplayName(itemStack) && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).equals(ChatColor.stripColor(itemName));
	}
	
	public static boolean isBanner(ItemStack itemStack) {
		return itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof BannerMeta;
	}
	
	public static boolean isMaterialItem(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Material");
	}
	
	public static boolean isQuestItem(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Quest Item");
	}
	
	public static boolean isFoodItem(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Food Item");
	}
	
	public static boolean isAmmoItem(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Ammo Item");
	}
	
	public static boolean hasSkillgemSocket(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, ChatColor.DARK_RED + "Empty");
	}
	
	public static String getSocketedSkill(ItemStack itemStack) {
		return hasLore(itemStack) ? getStringBetweenFromLore(itemStack, "Skillgem (" + ChatColor.LIGHT_PURPLE, ChatColor.WHITE + ")") : null;
	}
	
	public static int getBaseAtk(ItemStack itemStack) {
		return hasLore(itemStack) ? getIntegerFromLore(itemStack, "Attack:" + ChatColor.RED, 1) : 1;
	}
	
	public static int getBaseDef(ItemStack itemStack) {
		return hasLore(itemStack) ? getIntegerFromLore(itemStack, "Defense:" + ChatColor.BLUE, 1) : 0;
	}
	
	public static int getCurrentDurability(ItemStack itemStack) {
		return hasLore(itemStack) ? getIntegerFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 1) : 0;
	}
	
	public static int getMaximumDurability(ItemStack itemStack) {
		return hasLore(itemStack) ? getIntegerFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 3) : 0;
	}
	
	public static void setDurability(ItemStack itemStack, int durability) {
		replaceStringFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 1, durability + "");
	}
	
	public static int getRuneAtkBoost(ItemStack itemStack) {
		return getIntegerSumBetweenFromLore(itemStack, ChatColor.YELLOW + "Power Rune (" + ChatColor.RED + "+", " Atk");
	}
	
	public static int getRuneDefBoost(ItemStack itemStack) {
		return getIntegerSumBetweenFromLore(itemStack, ChatColor.YELLOW + "Power Rune (" + ChatColor.BLUE + "+", " Def");
	}
	
	public static int getLevelRequirement(ItemStack itemStack) {
		return hasLore(itemStack) ? getIntegerSumBetweenFromLore(itemStack, "lvl " + ChatColor.AQUA, ChatColor.DARK_GRAY + ")") : 1;
	}
	
	public static int getArrowCount(ItemStack itemStack) {
		return hasLore(itemStack) ? getIntegerSumBetweenFromLore(itemStack, ChatColor.GRAY + "Amount Left: ", " Arrows") : 0;
	}
	
	public static boolean containsSaturationModifier(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Saturation");
	}
	
	public static boolean containsHealingModifier(ItemStack itemStack) {
		return hasLore(itemStack) && doesLoreContain(itemStack, "Healing");
	}
	
	public static boolean containsTemperatureModifier(ItemStack itemStack) {
		return hasLore(itemStack) && (doesLoreContain(itemStack, "Heat Resistance") || doesLoreContain(itemStack, "Cold Resistance"));
	}
	
	public static boolean containsPvPProtectionModifier(ItemStack itemStack) {
		return hasLore(itemStack) && (doesLoreContain(itemStack, "PvP Protection"));
	}
	
	public static short getSaturation(ItemStack itemStack) {
		String saturationString = getStringFromLore(itemStack, "Saturation", 1);
		return saturationString == null ? 0 : Short.parseShort(saturationString);
	}
	
	public static short getHealing(ItemStack itemStack) {
		String healingString = getStringFromLore(itemStack, "Healing", 1);
		return healingString == null ? 0 : Short.parseShort(healingString);
	}
	
	public static short getHeatResistance(ItemStack itemStack) {
		String resistanceString = getStringFromLore(itemStack, "Heat Resistance", 2);
		return resistanceString == null ? 0 : Short.parseShort(resistanceString);
	}
	
	public static short getColdResistance(ItemStack itemStack) {
		String resistanceString = getStringFromLore(itemStack, "Cold Resistance", 2);
		return resistanceString == null ? 0 : Short.parseShort(resistanceString);
	}
	
	public static short getPvPProtection(ItemStack itemStack) {
		String resistanceString = getStringFromLore(itemStack, "PvP Protection", 2);
		return resistanceString == null ? 0 : Short.parseShort(resistanceString);
	}
	
	public static double getExperienceBonus(ItemStack itemStack) {
		double experienceBonus = 0;
		if(hasLore(itemStack))
			experienceBonus += getIntegerSumBetweenFromLore(itemStack, "+", "% Exp");
		return experienceBonus;
	}
	
	public static double getReflectionDamage(ItemStack itemStack) {
		double reflectionDamage = 0;
		if(hasLore(itemStack))
			reflectionDamage += getIntegerSumBetweenFromLore(itemStack, "+", " Rfl");
		return reflectionDamage;
	}
	
	public static int getEnhancementOnKillHP(ItemStack itemStack) {
		int onKillHP = 0;
		if(hasLore(itemStack))
			onKillHP += getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "HP on Kill");
		return onKillHP;
	}
	
	public static int getEnhancementOnKillMP(ItemStack itemStack) {
		int onKillMP = 0;
		if(hasLore(itemStack))
			onKillMP += getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "MP on Kill");
		return onKillMP;
	}
	
	public static float getEnhancementCritMultiplier(ItemStack itemStack) {
		float critMultiplier = 0;
		if(hasLore(itemStack))
			critMultiplier += getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "% Crit Multiplier");
		return critMultiplier / 100;
	}
	
	public static boolean isInstanceMap(ItemStack itemStack) {
		return hasLore(itemStack) && StringUtils.isNotBlank(getInstanceMapType(itemStack));
	}
	
	public static String getInstanceMapType(ItemStack itemStack) {
		return getStringBetweenFromLore(itemStack, "" + ChatColor.RED, " Map");
	}
	
	public static boolean hasColoredName(ItemStack itemStack, ChatColor chatColor) {
		return hasDisplayName(itemStack) && itemStack.getItemMeta().getDisplayName().contains("" + chatColor);
	}
	
	public static boolean hasDisplayName(ItemStack itemStack) {
		return itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName();
	}
	
	public static boolean hasLore(ItemStack itemStack) {
		return itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore();
	}
	
	public static boolean hasEqualLore(ItemStack itemStackA, ItemStack itemStackB) {
		return hasLore(itemStackA) && hasLore(itemStackB) && itemStackA.getItemMeta().getLore().equals(itemStackB.getItemMeta().getLore());
	}
	
	public static boolean doesLoreContain(ItemStack itemStack, String content) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore : lores)
			if(lore.contains(content))
				return true;
		return false;
	}
	
	public static String getStringFromLore(ItemStack itemStack, String content, int index) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore : lores)
			if(lore.contains(content))
				return lore.split(" ")[index];
		return null;
	}
	
	public static void replaceStringFromLore(ItemStack itemStack, String content, int index, String replacement) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		List<String> lores = itemMeta.getLore();
		List<String> newLores = new ArrayList<>();
		for(String lore : lores) {
			if(lore.contains(content)) {
				String[] splitString = lore.split(" ");
				splitString[index] = replacement;
				lore = StringUtils.join(splitString, " ");
			}
			newLores.add(lore);
		}
		itemMeta.setLore(newLores);
		itemStack.setItemMeta(itemMeta);
	}
	
	public static String getStringBetweenFromLore(ItemStack itemStack, String before, String after) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore: lores)
			if(lore.contains(before) && lore.contains(after))
				return StringUtils.substringBetween(lore, before, after);
		return null;
	}
	
	public static int getIntegerFromLore(ItemStack itemStack, String content, int index) {
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore : lores)
			if(lore.contains(content))
				return Integer.parseInt(lore.split(" ")[index]);
		return 0;
	}
	
	public static int getIntegerSumBetweenFromLore(ItemStack itemStack, String before, String after) {
		int integerSum = 0;
		List<String> lores = itemStack.getItemMeta().getLore();
		for(String lore: lores)
			if(lore.contains(before) && lore.contains(after))
				integerSum += Integer.parseInt(StringUtils.substringBetween(lore, before, after));
		return integerSum;
	}
	
}
