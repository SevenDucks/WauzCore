package eu.wauz.wauzcore.items.util;

import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.ArmorCategory;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.EquipmentType;
import net.md_5.bungee.api.ChatColor;

/**
 * An util class for reading and writing equipment properties.
 * 
 * @author Wauzmons
 * 
 * @see ItemUtils
 */
public class EquipmentUtils {
	
	/**
	 * Checks if an item stack possesses an empty rune slot, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item has a rune socket.
	 */
	public static boolean hasRuneSocket(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, ChatColor.GREEN + "Empty");
	}
	
	/**
	 * Checks if an item stack possesses an empty skillgem slot, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return If the item has a skillgem socket.
	 */
	public static boolean hasSkillgemSocket(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, ChatColor.DARK_RED + "Empty");
	}
	
	/**
	 * Gets the name of an item stack's socketed skillgem, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The name of the socketed skillgem.
	 */
	public static String getSocketedSkill(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getStringBetweenFromLore(itemStack, "Skillgem (" + ChatColor.LIGHT_PURPLE, ChatColor.WHITE + ")") : null;
	}
	
	/**
	 * Gets the type of an equipment item stack, based on material.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The equipment type of the item.
	 */
	public static EquipmentType getEquipmentType(ItemStack itemStack) {
		if(itemStack == null) {
			return EquipmentType.UNKNOWN;
		}
		Material material = itemStack.getType();
		String materialString = material.toString();
		
		if(StringUtils.containsAny(materialString, "_SWORD", "_AXE", "_HOE")
				|| material.equals(Material.BOW)
				|| material.equals(Material.TRIDENT)
				|| material.equals(Material.SHIELD)
				|| material.equals(Material.FISHING_ROD)) {
			return EquipmentType.WEAPON;
		}
		else if(materialString.contains("CHESTPLATE")) {
			return EquipmentType.ARMOR;
		}
		else {
			return EquipmentType.UNKNOWN;
		}
	}
	
	/**
	 * Gets the category of an armor item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The armor category of the item.
	 */
	public static ArmorCategory getArmorCategory(ItemStack itemStack) {
		String categoryString = ItemUtils.hasLore(itemStack) ? ItemUtils.getStringFromLore(itemStack, "Category", 1) : null;
		
		if(categoryString == null) {
			return ArmorCategory.UNKNOWN;
		}
		else if(categoryString.contains(ArmorCategory.HEAVY.toString())) {
			return ArmorCategory.HEAVY;
		}
		else if(categoryString.contains(ArmorCategory.MEDIUM.toString())) {
			return ArmorCategory.MEDIUM;
		}
		else if(categoryString.contains(ArmorCategory.LIGHT.toString())) {
			return ArmorCategory.LIGHT;
		}
		else {
			return ArmorCategory.UNKNOWN;
		}
	}
	
	/**
	 * Gets the base attack of a weapon item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The base attack of the item.
	 * 
	 * @see EquipmentUtils#setBaseAtk(ItemStack, int)
	 */
	public static int getBaseAtk(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Attack:" + ChatColor.RED, 1) : 1;
	}
	
	/**
	 * Sets the base attack of a weapon item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to edit.
	 * @param newAttack The new base attack value.
	 * 
	 * @return If the action was successful.
	 * 
	 * @see EquipmentUtils#getBaseAtk(ItemStack)
	 */
	public static boolean setBaseAtk(ItemStack itemStack, int newAttack) {
		String oldAttackLore = "Attack:" + ChatColor.RED + " ";
		String newAttackLore = "Attack:" + ChatColor.RED + " " + newAttack;
		return ItemUtils.replaceStringFromLore(itemStack, oldAttackLore, newAttackLore);
	}
	
	/**
	 * Gets the base defense of an armor item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The base defense of the item.
	 * 
	 * @see EquipmentUtils#setBaseDef(ItemStack, int)
	 */
	public static int getBaseDef(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Defense:" + ChatColor.BLUE, 1) : 0;
	}
	
	/**
	 * Sets the base defense of an armor item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to edit.
	 * @param newDefense The new base defense value.
	 * 
	 * @return If the action was successful.
	 * 
	 * @see EquipmentUtils#getBaseDef(ItemStack)
	 */
	public static boolean setBaseDef(ItemStack itemStack, int newDefense) {
		String oldDefenseLore = "Defense:" + ChatColor.BLUE + " ";
		String newDefenseLore = "Defense:" + ChatColor.BLUE + " " + newDefense;
		return ItemUtils.replaceStringFromLore(itemStack, oldDefenseLore, newDefenseLore);
	}
	
	/**
	 * Gets the might of a rune item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The rune might of the item.
	 */
	public static int getRuneMight(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Might:" + ChatColor.YELLOW, 1) : 0;
	}
	
	/**
	 * Gets the current durability of an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The current durability of the item.
	 * 
	 * @see EquipmentUtils#setCurrentDurability(ItemStack, int)
	 */
	public static int getCurrentDurability(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 1) : 0;
	}
	
	/**
	 * Gets the maximum durability of an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The maximum durability of the item.
	 * 
	 * @see EquipmentUtils#setMaximumDurability(ItemStack, int)
	 */
	public static int getMaximumDurability(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 3) : 0;
	}
	
	/**
	 * Sets the current durability of an equipment item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to edit.
	 * @param durability The new current durability value.
	 * 
	 * @return If the action was successful.
	 * 
	 * @see EquipmentUtils#getCurrentDurability(ItemStack)
	 */
	public static boolean setCurrentDurability(ItemStack itemStack, int durability) {
		return ItemUtils.replaceStringFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 1, durability + "");
	}
	
	/**
	 * Sets the maximum durability of an equipment item stack, based on lore + repairs it.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to edit.
	 * @param newDurability The new maximum durability value.
	 * 
	 * @return If the action was successful.
	 * 
	 * @see EquipmentUtils#getMaximumDurability(ItemStack)
	 */
	public static boolean setMaximumDurability(ItemStack itemStack, int newDurability) {
		String oldDurabilityLore = "Durability:" + ChatColor.DARK_GREEN + " ";
		String newDurabilityLore = "Durability:" + ChatColor.DARK_GREEN + " " + newDurability;
		newDurabilityLore += " " + ChatColor.DARK_GRAY + "/ " + newDurability;
		DurabilityCalculator.setDamage(itemStack, 0);
		return ItemUtils.replaceStringFromLore(itemStack, oldDurabilityLore, newDurabilityLore);
	}
	
	/**
	 * Gets the attack boost from runes of an equipment item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The item's attack boost from runes.
	 */
	public static int getRuneAtkBoost(ItemStack itemStack) {
		return ItemUtils.getIntegerSumBetweenFromLore(itemStack, ChatColor.YELLOW + "Power Rune (" + ChatColor.RED + "+", " Atk");
	}
	
	/**
	 * Gets the defense boost from runes of an equipment item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The item's defense boost from runes.
	 */
	public static int getRuneDefBoost(ItemStack itemStack) {
		return ItemUtils.getIntegerSumBetweenFromLore(itemStack, ChatColor.YELLOW + "Power Rune (" + ChatColor.BLUE + "+", " Def");
	}
	
	/**
	 * Gets the durability boost from runes of an equipment item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The item's durability boost from runes.
	 */
	public static int getRuneDurBoost(ItemStack itemStack) {
		return ItemUtils.getIntegerSumBetweenFromLore(itemStack, ChatColor.YELLOW + "Hardening Rune (" + ChatColor.DARK_GREEN + "+", " Dur");
	}
	
	/**
	 * Gets the level requirement of an equipment item stack, based on lore.
	 * Does NOT include null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The level requirement of the item.
	 */
	public static int getLevelRequirement(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerSumBetweenFromLore(itemStack, "lvl " + ChatColor.AQUA, ChatColor.DARK_GRAY + ")") : 1;
	}
	
	/**
	 * Gets the percentage of bonus experience from an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The experience bonus percentage of the item.
	 */
	public static double getExperienceBonus(ItemStack itemStack) {
		double experienceBonus = 0;
		if(ItemUtils.hasLore(itemStack)) {
			experienceBonus += ItemUtils.getIntegerSumBetweenFromLore(itemStack, "+", "% Exp");
		}
		return experienceBonus;
	}
	
	/**
	 * Gets the reflection damage from an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The reflection damage of the item.
	 */
	public static double getReflectionDamage(ItemStack itemStack) {
		double reflectionDamage = 0;
		if(ItemUtils.hasLore(itemStack)) {
			reflectionDamage += ItemUtils.getIntegerSumBetweenFromLore(itemStack, "+", " Rfl");
		}
		return reflectionDamage;
	}
	
	/**
	 * Gets the health points on kill from an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The health points on kill of the item.
	 */
	public static int getEnhancementOnKillHP(ItemStack itemStack) {
		int onKillHP = 0;
		if(ItemUtils.hasLore(itemStack)) {
			onKillHP += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "HP on Kill");
		}
		return onKillHP;
	}
	
	/**
	 * Gets the mana points on kill from an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The mana points on kill of the item.
	 */
	public static int getEnhancementOnKillMP(ItemStack itemStack) {
		int onKillMP = 0;
		if(ItemUtils.hasLore(itemStack)) {
			onKillMP += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "MP on Kill");
		}
		return onKillMP;
	}
	
	/**
	 * Gets the critical damage multiplier from an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The critical damage multiplier of the item.
	 */
	public static float getEnhancementCriticalDamageMultiplier(ItemStack itemStack) {
		float critMultiplier = 0;
		if(ItemUtils.hasLore(itemStack)) {
			critMultiplier += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "% Crit Multiplier");
		}
		return critMultiplier / 100;
	}
	
	/**
	 * Gets the skill damage multiplier from an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The skill damage multiplier of the item.
	 */
	public static float getEnhancementSkillDamageMultiplier(ItemStack itemStack) {
		float critMultiplier = 0;
		if(ItemUtils.hasLore(itemStack)) {
			critMultiplier += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "% Skill Damage");
		}
		return critMultiplier / 100;
	}
	
	/**
	 * Gets the rune effectiveness multiplier from an equipment item stack, based on lore.
	 * Includes null check.
	 * 
	 * @param itemStack The item stack to check.
	 * 
	 * @return The rune effectiveness multiplier of the item.
	 */
	public static float getEnhancementRuneEffectivenessMultiplier(ItemStack itemStack) {
		float critMultiplier = 0;
		if(ItemUtils.hasLore(itemStack)) {
			critMultiplier += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "% Rune Effectiveness");
		}
		return critMultiplier / 100;
	}

}
