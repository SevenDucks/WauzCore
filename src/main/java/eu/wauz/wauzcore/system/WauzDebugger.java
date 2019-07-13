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
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
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
	
	public static boolean toggleDungeonItemsDebugMode(Player player) {
		player.addAttachment(WauzCore.getInstance(), "wauz.debug.ditems", !player.hasPermission("wauz.debug.ditems"));
		log(player, "Dungeon Items debug mode toggled!");
		return true;
	}
	
	public static boolean getSkillgemWeapon(Player player, String skillId) {
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.playerSkillMap.get(skillId);
		if(skill == null)
			return false;
		
		ItemStack itemStack = new ItemStack(Material.DIAMOND_HOE);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.DARK_RED + "Noble Phantasm");
		List<String> lores = new ArrayList<String>();
		String x = ChatFormatter.ICON_DIAMS;
		String rareStars = ChatColor.YELLOW +x +x +x +x +x;
		lores.add(ChatColor.WHITE + "Debuggers" + ChatColor.GRAY + " TX " + ChatColor.WHITE + "Unique Weapon " + rareStars);
		lores.add("");
		lores.add("Attack:" + ChatColor.RED + " 10 " + ChatColor.DARK_GRAY
				+ "(" + ChatColor.YELLOW + "lvl " + ChatColor.AQUA + "1" + ChatColor.DARK_GRAY + ")");
		lores.add("Durability:" + ChatColor.DARK_GREEN + " " + 2048
				+ " " + ChatColor.DARK_GRAY + "/ " + 2048);
		lores.add("");
		lores.add(ChatColor.WHITE + "Skillgem (" + ChatColor.LIGHT_PURPLE + skill.getSkillId() + ChatColor.WHITE + ")");
		lores.add(ChatColor.WHITE + skill.getSkillDescription());
		lores.add(ChatColor.WHITE + skill.getSkillStats());
		lores.add("");
		lores.add(ChatColor.WHITE + "Rune Slot (" + ChatColor.GREEN + "Empty" + ChatColor.WHITE + ")");
		lores.add(ChatColor.WHITE + "Rune Slot (" + ChatColor.GREEN + "Empty" + ChatColor.WHITE + ")");
		itemMeta.setLore(lores);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(itemMeta);
		
		player.getInventory().addItem(itemStack);
		return true;
	}

}
