package eu.wauz.wauzcore.items.identifiers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.enums.Rarity;
import eu.wauz.wauzcore.items.enums.Tier;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;

/**
 * A class to get special equipment items from.
 * 
 * @author Wauzmons
 */
public class WauzEquipmentHelper {
	
	/**
	 * Gives a weapon with socketed skillgem, if valid skillId provided.
	 * 
	 * @param player The player that should receive the skillgem weapon.
	 * @param skillId The id of the skill.
	 * 
	 * @return If the player received their weapon.
	 */
	public static boolean getSkillgemWeapon(Player player, String skillId) {
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillId);
		if(skill == null) {
			return false;
		}
		
		player.getInventory().addItem(getSkillgemWeapon(skill, Material.DIAMOND_HOE, true));
		return true;
	}
	
	/**
	 * Creates a weapon with socketed skillgem.
	 * 
	 * @param skill The skill inside the weapon.
	 * @param material The material of the weapon item stack.
	 * @param debug If the weapon should have debug stats. Otherwise a starter weapon is created.
	 * 
	 * @return A weapon with socketed skillgem.
	 */
	public static ItemStack getSkillgemWeapon(WauzPlayerSkill skill, Material material, boolean debug) {
		WauzEquipmentBuilder builder = new WauzEquipmentBuilder(material);
		builder.addSkillgem(skill);
		builder.addMainStats(debug ? 10 : 1, 0, 1, 1);
		builder.addDurabilityStat(debug ? 2048 : 300);
		builder.addSpeedStat(1.20);
		return builder.generate(Tier.EQUIP_T1, Rarity.UNIQUE, EquipmentType.WEAPON, "Noble Phantasm");
	}
	
	/**
	 * Gives an equipment piece with a specific enhancement, if valid enhancementId provided.
	 * 
	 * @param player The player that should receive the skillgem weapon.
	 * @param enhancementId The id of the enhancement.
	 * @param enhancementLevel The level of the enhancement.
	 * 
	 * @return If the player received their equip.
	 */
	public static boolean getEnhancedEquipment(Player player, String enhancementId, int enhancementLevel) {
		WauzEnhancement enhancement = WauzEquipmentEnhancer.getEnhancement(enhancementId);
		if(enhancement == null || enhancementLevel < 1) {
			return false;
		}
		
		player.getInventory().addItem(getEnhancedEquipment(enhancement, enhancementLevel));
		return true;
	}
	
	/**
	 * Creates an equipment piece with a specific enhancement.
	 * 
	 * @param enhancement The enhancement of the equipment.
	 * @param enhancementLevel The level of the enhancement.
	 * 
	 * @return An enhanced equipment piece.
	 */
	public static ItemStack getEnhancedEquipment(WauzEnhancement enhancement, int enhancementLevel) {
		boolean isWeapon = enhancement.getEquipmentType().equals(EquipmentType.WEAPON);
		WauzEquipmentBuilder builder = new WauzEquipmentBuilder(isWeapon ? Material.IRON_SWORD : Material.IRON_CHESTPLATE);
		WauzEnhancementParameters parameters = new WauzEnhancementParameters(enhancementLevel);
		parameters.setAttackStat(10);
		parameters.setDefenseStat(5);
		parameters.setDurabilityStat(2048);
		parameters.setSpeedStat(1.20);
		parameters = WauzEquipmentEnhancer.enhanceEquipment(builder, enhancement, parameters);
		builder.addMainStats(parameters.getAttackStat(), parameters.getDefenseStat(), 1, 1);
		builder.addDurabilityStat(parameters.getDurabilityStat());
		if(isWeapon) {
			builder.addSpeedStat(parameters.getSpeedStat());
		}
		else {
			builder.addArmorCategory(ArmorCategory.LIGHT);
		}
		EquipmentType equipmentType = isWeapon ? EquipmentType.WEAPON : EquipmentType.ARMOR;
		return builder.generate(Tier.EQUIP_T1, Rarity.UNIQUE, equipmentType, "Enchanted Iron");
	}
	
	/**
	 * Gives a rune of a specific type, if valid runeId provided.
	 * 
	 * @param player The player that should receive the rune.
	 * @param runeId The id of the rune.
	 * 
	 * @return If the player received their rune.
	 */
	public static boolean getRune(Player player, String runeId) {
		WauzRune rune = WauzRuneInserter.getRune(runeId);
		if(rune == null) {
			return false;
		}
		
		player.getInventory().addItem(getRune(rune, true));
		return true;
	}
	
	/**
	 * Creates a rune of a specific type.
	 * 
	 * @param rune The type of the rune.
	 * @param debug If the rune should have debug stats. Otherwise a starter rune is created.
	 * 
	 * @return A rune of a specific type.
	 */
	public static ItemStack getRune(WauzRune rune, boolean debug) {
		WauzRuneBuilder builder = new WauzRuneBuilder(rune);
		builder.addMightStat(debug ? 50 : 10);
		return builder.generate(Tier.EQUIP_T1, Rarity.DEAFENING);
	}

}
