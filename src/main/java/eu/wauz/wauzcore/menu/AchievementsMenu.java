package eu.wauz.wauzcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import net.md_5.bungee.api.ChatColor;

public class AchievementsMenu implements WauzInventory {
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new AchievementsMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Achievements");
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
	}

}
