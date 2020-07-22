package eu.wauz.wauzcore.system;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.WauzEquipmentBuilder;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.enums.Rarity;
import eu.wauz.wauzcore.items.enums.Tier;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * Used for debug logging and administrative command mechanics.
 * 
 * @author Wauzmons
 */
public class WauzDebugger {
	
	/**
	 * Sends a debug message to the player, if they have debug mode enabled.
	 * 
	 * @param player The player who should receive the message.
	 * @param message The content of the message.
	 */
	public static void log(Player player, String message) {
		if(player.hasPermission(WauzPermission.DEBUG.toString())) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "[Debug] " + message);
		}
	}
	
	/**
	 * Sends a debug message to the server console.
	 * 
	 * @param message The content of the message.
	 */
	public static void log(String message) {
		WauzCore.getInstance().getLogger().info("[Debug] " + message);
	}
	
	/**
	 * Sends a catched exception message to the server console.
	 * 
	 * @param clazz The class in which the exception occured.
	 * @param e The catched exception.
	 */
	public static void catchException(Class<?> clazz, Exception e) {
		WauzCore.getInstance().getLogger().warning("[Catcher] Irrelevant Exception in " + clazz.getName() + ": " + e.getMessage());
	}
	
	/**
	 * Toggles the general debug mode for a player.
	 * Grants access to the ingame debug log.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleDebugMode(Player player) {
		String permission = WauzPermission.DEBUG.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the magic debug mode for a player.
	 * Grants infinite mana and 1s skill cooldowns.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleMagicDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_MAGIC.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Magic debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the crafting debug mode for a player.
	 * Grants virtually unlimited materials and crafting levels.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleCraftingDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_CRAFTING.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Crafting debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the building debug mode for a player.
	 * Grants building rights in every region.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleBuildingDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_BUILDING.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Building debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the flying debug mode for a player.
	 * Grants flying rights + increased speed in all modes.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleFlyingDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_FLYING.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Flying debug mode toggled!");
		
		player.setFlySpeed(player.hasPermission(permission) ? 0.3f : 0.1f);
		return true;
	}
	
	/**
	 * Toggles the attack debug mode for a player.
	 * Grants drastical increase of damage output.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleAttackDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_ATTACK.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Attack debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the defense debug mode for a player.
	 * Grants reduction to zero of taken damage.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleDefenseDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_DEFENSE.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Defense debug mode toggled!");
		return true;
	}
	
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
		List<String> runeIds = WauzRuneInserter.getAllRuneIds();
		if(!runeIds.contains(runeId)) {
			return false;
		}
		
		player.getInventory().addItem(getRune(runeId, true));
		return true;
	}
	
	/**
	 * Creates a rune of a specific type.
	 * 
	 * @param runeId The id of the rune.
	 * @param debug If the rune should have debug stats. Otherwise a starter rune is created.
	 * 
	 * @return A rune of a specific type.
	 */
	public static ItemStack getRune(String runeId, boolean debug) {
		ItemStack itemStack = new ItemStack(Material.FIREWORK_STAR);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GOLD + "Rune of " + runeId);
		List<String> lores = new ArrayList<String>();
		String x = UnicodeUtils.ICON_DIAMOND;
		String rareStars = ChatColor.GREEN +x +x +x;
		lores.add(ChatColor.WHITE + (debug ? "Debuggers" : "Starters") + ChatColor.GRAY + " TX " + ChatColor.WHITE + "Deafening Rune " + rareStars);
		lores.add("");
		lores.add(ChatColor.GRAY + "Can be inserted into Equipment,");
		lores.add(ChatColor.GRAY + "which possesses an empty Rune Slot.");
		lores.add("");
		lores.add("Might:" + ChatColor.YELLOW + " " + (debug ? 50 : 10));
		itemMeta.setLore(lores);	
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

}
