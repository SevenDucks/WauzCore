package eu.wauz.wauzcore.system.commands;

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
import eu.wauz.wauzcore.system.ChatFormatter;
import net.md_5.bungee.api.ChatColor;

public class WauzDebugger {
	
	public static void log(Player player, String message) {
		if(player.hasPermission("wauz.debug"))
			player.sendMessage(ChatColor.LIGHT_PURPLE + "[Debug] " + message);
	}
	
	public static void log(String message) {
		Bukkit.getLogger().info("[WauzCore Debug] " + message);
	}
	
	public static void catchException(Class<?> clazz, Exception e) {
		Bukkit.getLogger().warning("[WauzCore Catcher] Irrelevant Exception in " + clazz.getName() + ": " + e.getMessage());
	}
	
	public static boolean toggleDebugMode(Player player) {
		player.addAttachment(WauzCore.getInstance(), "wauz.debug", !player.hasPermission("wauz.debug"));
		log(player, "Debug mode toggled!");
		return true;
	}
	
	public static boolean toggleMagicDebugMode(Player player) {
		player.addAttachment(WauzCore.getInstance(), "wauz.debug.magic", !player.hasPermission("wauz.debug.magic"));
		log(player, "Magic debug mode toggled!");
		return true;
	}
	
	public static boolean toggleCraftingDebugMode(Player player) {
		player.addAttachment(WauzCore.getInstance(), "wauz.debug.crafting", !player.hasPermission("wauz.debug.crafting"));
		log(player, "Crafting debug mode toggled!");
		return true;
	}
	
	public static boolean toggleBuildingDebugMode(Player player) {
		player.addAttachment(WauzCore.getInstance(), "wauz.debug.building", !player.hasPermission("wauz.debug.building"));
		log(player, "Building debug mode toggled!");
		return true;
	}
	
	public static boolean getSkillgemWeapon(Player player, String skillId) {
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillId);
		if(skill == null) {
			return false;
		}
		
		player.getInventory().addItem(getSkillgemWeapon(skill, true));
		return true;
	}
	
	public static ItemStack getSkillgemWeapon(WauzPlayerSkill skill, boolean debug) {
		ItemStack itemStack = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.DARK_RED + "Noble Phantasm");
		List<String> lores = new ArrayList<String>();
		String x = ChatFormatter.ICON_DIAMS;
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
		String x = ChatFormatter.ICON_DIAMS;
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
	
	public static boolean getRune(Player player, String runeId) {
		List<String> runeIds = WauzRuneInserter.getAllRuneIds();
		if(!runeIds.contains(runeId)) {
			return false;
		}
		
		player.getInventory().addItem(getRune(runeId, true));
		return true;
	}
	
	public static ItemStack getRune(String runeId, boolean debug) {
		ItemStack itemStack = new ItemStack(Material.FIREWORK_STAR);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GOLD + "Rune of " + runeId);
		List<String> lores = new ArrayList<String>();
		String x = ChatFormatter.ICON_DIAMS;
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
