package eu.wauz.wauzcore.system;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import net.md_5.bungee.api.ChatColor;

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
		Bukkit.getLogger().info("[WauzCore Debug] " + message);
	}
	
	/**
	 * Sends a catched exception message to the server console.
	 * 
	 * @param clazz The class in which the exception occured.
	 * @param e The catched exception.
	 */
	public static void catchException(Class<?> clazz, Exception e) {
		Bukkit.getLogger().warning("[WauzCore Catcher] Irrelevant Exception in " + clazz.getName() + ": " + e.getMessage());
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
		
		player.getInventory().addItem(getSkillgemWeapon(skill, true));
		return true;
	}
	
	/**
	 * Creates a weapon with socketed skillgem.
	 * 
	 * @param skill The skill inside the weapon.
	 * @param debug If the weapon should have debug stats. Otherwise a starter weapon is created.
	 * 
	 * @return A weapon with socketed skillgem.
	 */
	public static ItemStack getSkillgemWeapon(WauzPlayerSkill skill, boolean debug) {
		ItemStack itemStack = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.DARK_RED + "Noble Phantasm");
		List<String> lores = new ArrayList<String>();
		String x = UnicodeUtils.ICON_DIAMOND;
		String rareStars = ChatColor.YELLOW +x +x +x +x +x;
		lores.add(ChatColor.WHITE + (debug ? "Debuggers" : "Starters") + ChatColor.GRAY + " TX " + ChatColor.WHITE + "Unique Weapon " + rareStars);
		lores.add("");
		lores.add("Attack:" + ChatColor.RED + (debug ? " 10 " : " 1 ") + ChatColor.DARK_GRAY
				+ "(" + ChatColor.YELLOW + "lvl " + ChatColor.AQUA + "1" + ChatColor.DARK_GRAY + ")");
		lores.add("Durability:" + ChatColor.DARK_GREEN + " " + (debug ? 2048 : 300)
				+ " " + ChatColor.DARK_GRAY + "/ " + (debug ? 2048 : 300));
		lores.add("");
		lores.add(ChatColor.WHITE + "Skillgem (" + ChatColor.LIGHT_PURPLE + skill.getSkillId() + ChatColor.WHITE + ")");
		lores.add(ChatColor.WHITE + skill.getSkillDescription());
		lores.add(ChatColor.WHITE + skill.getSkillStats());
		lores.add("");
		lores.add(WauzEquipmentIdentifier.EMPTY_RUNE_SLOT);
		lores.add(WauzEquipmentIdentifier.EMPTY_RUNE_SLOT);
		itemMeta.setLore(lores);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
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
		
		boolean isWeapon = enhancement.getEquipmentType().equals(EquipmentType.WEAPON);
		
		ItemStack itemStack = new ItemStack(isWeapon ? Material.IRON_SWORD : Material.IRON_CHESTPLATE);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.DARK_RED + "Enchanted Iron");
		List<String> lores = new ArrayList<String>();
		String x = UnicodeUtils.ICON_DIAMOND;
		String rareStars = ChatColor.YELLOW +x +x +x +x +x;
		lores.add(ChatColor.WHITE + "Debuggers" + ChatColor.GRAY + " TX " + ChatColor.WHITE + "Unique Weapon " + rareStars);
		lores.add("");
		
		String mainStatString = null;
		int attackStat = 10;
		int defenseStat = 5;
		int durabilityStat = 2048;
		if(isWeapon) {
			mainStatString = "Attack:" + ChatColor.RED + " " + attackStat + " " + ChatColor.DARK_GRAY
					+ "(" + ChatColor.YELLOW + "lvl " + ChatColor.AQUA + "1" + ChatColor.DARK_GRAY + ")";
		}
		else {
			mainStatString = "Defense:" + ChatColor.BLUE + " " + defenseStat + " " + ChatColor.DARK_GRAY
					+ "(" + ChatColor.YELLOW + "lvl " + ChatColor.AQUA + "1" + ChatColor.DARK_GRAY + ")";
		}
		lores.add(mainStatString);
		
		WauzEnhancementParameters parameters = new WauzEnhancementParameters(enhancementLevel);
		parameters.setItemMeta(itemMeta);
		parameters.setLores(lores);
		parameters.setMainStatString(mainStatString);
		parameters.setAttackStat(attackStat);
		parameters.setDefenseStat(defenseStat);
		parameters.setDurabilityStat(durabilityStat);
		
		WauzEquipmentEnhancer.enhanceEquipment(enhancement, parameters);
		itemMeta = parameters.getItemMeta();
		lores = parameters.getLores();
		mainStatString = parameters.getMainStatString();
		attackStat = parameters.getAttackStat();
		defenseStat = parameters.getDefenseStat();
		durabilityStat = parameters.getDurabilityStat();
		
		lores.add("Durability:" + ChatColor.DARK_GREEN + " " + durabilityStat
				+ " " + ChatColor.DARK_GRAY + "/ " + durabilityStat);
		
		lores.add("");
		lores.add(WauzEquipmentIdentifier.EMPTY_RUNE_SLOT);
		lores.add(WauzEquipmentIdentifier.EMPTY_RUNE_SLOT);
		itemMeta.setLore(lores);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(itemMeta);
		
		player.getInventory().addItem(itemStack);
		return true;
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
