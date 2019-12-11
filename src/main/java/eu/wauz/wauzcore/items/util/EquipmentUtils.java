package eu.wauz.wauzcore.items.util;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.ArmorCategory;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.EquipmentType;
import net.md_5.bungee.api.ChatColor;

public class EquipmentUtils {
	
	public static boolean hasRuneSocket(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, ChatColor.GREEN + "Empty");
	}
	
	public static boolean hasSkillgemSocket(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) && ItemUtils.doesLoreContain(itemStack, ChatColor.DARK_RED + "Empty");
	}
	
	public static String getSocketedSkill(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getStringBetweenFromLore(itemStack, "Skillgem (" + ChatColor.LIGHT_PURPLE, ChatColor.WHITE + ")") : null;
	}
	
	public static EquipmentType getEquipmentType(ItemStack itemStack) {
		String itemMaterial = itemStack.getType().toString();
		
		if(StringUtils.containsAny(itemMaterial, "_SWORD", "_AXE", "_HOE", "BOW")) {
			return EquipmentType.WEAPON;
		}
		else if(itemMaterial.contains("CHESTPLATE")) {
			return EquipmentType.ARMOR;
		}
		else {
			return EquipmentType.UNKNOWN;
		}
	}
	
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
	
	public static int getBaseAtk(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Attack:" + ChatColor.RED, 1) : 1;
	}
	
	public static boolean setBaseAtk(ItemStack itemStack, int newAttack) {
		String oldAttackLore = "Attack:" + ChatColor.RED + " ";
		String newAttackLore = "Attack:" + ChatColor.RED + " " + newAttack;
		return ItemUtils.replaceStringFromLore(itemStack, oldAttackLore, newAttackLore);
	}
	
	public static int getBaseDef(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Defense:" + ChatColor.BLUE, 1) : 0;
	}
	
	public static boolean setBaseDef(ItemStack itemStack, int newDefense) {
		String oldDefenseLore = "Defense:" + ChatColor.BLUE + " ";
		String newDefenseLore = "Defense:" + ChatColor.BLUE + " " + newDefense;
		return ItemUtils.replaceStringFromLore(itemStack, oldDefenseLore, newDefenseLore);
	}
	
	public static int getRuneMight(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Might:" + ChatColor.YELLOW, 1) : 0;
	}
	
	public static int getCurrentDurability(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 1) : 0;
	}
	
	public static int getMaximumDurability(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 3) : 0;
	}
	
	public static void setDurability(ItemStack itemStack, int durability) {
		ItemUtils.replaceStringFromLore(itemStack, "Durability:" + ChatColor.DARK_GREEN, 1, durability + "");
	}
	
	public static boolean setMaximumDurability(ItemStack itemStack, int newDurability) {
		String oldDurabilityLore = "Durability:" + ChatColor.DARK_GREEN + " ";
		String newDurabilityLore = "Durability:" + ChatColor.DARK_GREEN + " " + newDurability;
		newDurabilityLore += " " + ChatColor.DARK_GRAY + "/ " + newDurability;
		DurabilityCalculator.setDamage(itemStack, 0);
		return ItemUtils.replaceStringFromLore(itemStack, oldDurabilityLore, newDurabilityLore);
	}
	
	public static int getRuneAtkBoost(ItemStack itemStack) {
		return ItemUtils.getIntegerSumBetweenFromLore(itemStack, ChatColor.YELLOW + "Power Rune (" + ChatColor.RED + "+", " Atk");
	}
	
	public static int getRuneDefBoost(ItemStack itemStack) {
		return ItemUtils.getIntegerSumBetweenFromLore(itemStack, ChatColor.YELLOW + "Power Rune (" + ChatColor.BLUE + "+", " Def");
	}
	
	public static int getRuneDurBoost(ItemStack itemStack) {
		return ItemUtils.getIntegerSumBetweenFromLore(itemStack, ChatColor.YELLOW + "Hardening Rune (" + ChatColor.DARK_GREEN + "+", " Dur");
	}
	
	public static int getLevelRequirement(ItemStack itemStack) {
		return ItemUtils.hasLore(itemStack) ? ItemUtils.getIntegerSumBetweenFromLore(itemStack, "lvl " + ChatColor.AQUA, ChatColor.DARK_GRAY + ")") : 1;
	}
	
	public static double getExperienceBonus(ItemStack itemStack) {
		double experienceBonus = 0;
		if(ItemUtils.hasLore(itemStack)) {
			experienceBonus += ItemUtils.getIntegerSumBetweenFromLore(itemStack, "+", "% Exp");
		}
		return experienceBonus;
	}
	
	public static double getReflectionDamage(ItemStack itemStack) {
		double reflectionDamage = 0;
		if(ItemUtils.hasLore(itemStack)) {
			reflectionDamage += ItemUtils.getIntegerSumBetweenFromLore(itemStack, "+", " Rfl");
		}
		return reflectionDamage;
	}
	
	public static int getEnhancementOnKillHP(ItemStack itemStack) {
		int onKillHP = 0;
		if(ItemUtils.hasLore(itemStack)) {
			onKillHP += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "HP on Kill");
		}
		return onKillHP;
	}
	
	public static int getEnhancementOnKillMP(ItemStack itemStack) {
		int onKillMP = 0;
		if(ItemUtils.hasLore(itemStack)) {
			onKillMP += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "MP on Kill");
		}
		return onKillMP;
	}
	
	public static float getEnhancementCriticalDamageMultiplier(ItemStack itemStack) {
		float critMultiplier = 0;
		if(ItemUtils.hasLore(itemStack)) {
			critMultiplier += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "% Crit Multiplier");
		}
		return critMultiplier / 100;
	}
	
	public static float getEnhancementSkillDamageMultiplier(ItemStack itemStack) {
		float critMultiplier = 0;
		if(ItemUtils.hasLore(itemStack)) {
			critMultiplier += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "% Skill Damage");
		}
		return critMultiplier / 100;
	}
	
	public static float getEnhancementRuneEffectivenessMultiplier(ItemStack itemStack) {
		float critMultiplier = 0;
		if(ItemUtils.hasLore(itemStack)) {
			critMultiplier += ItemUtils.getIntegerSumBetweenFromLore(itemStack, " ", " " + ChatColor.GRAY + "% Rune Effectiveness");
		}
		return critMultiplier / 100;
	}

}
