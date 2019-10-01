package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import net.md_5.bungee.api.ChatColor;

public class CharacterWorldMenu implements WauzInventory {
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new CharacterWorldMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your World!");
		
		ItemStack world2 = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
		ItemMeta im2 = world2.getItemMeta();
		im2.setDisplayName(ChatColor.AQUA + "Dalyreos");
		List<String> lores2 = new ArrayList<String>();
		lores2.add(ChatColor.WHITE + "Main Map (15Kx15K)");
		im2.setLore(lores2);
		world2.setItemMeta(im2);
		menu.setItem(3, world2);
		
		ItemStack world1 = new ItemStack(Material.LIME_CONCRETE);
		ItemMeta im1 = world1.getItemMeta();
		im1.setDisplayName(ChatColor.GREEN + "Wauzland");
		List<String> lores1 = new ArrayList<String>();
		lores1.add(ChatColor.WHITE + "Alpha Test Map (2Kx2K)");
		im1.setLore(lores1);
		world1.setItemMeta(im1);
		menu.setItem(5, world1);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(playerData == null) {
			return;
		}
		if(clicked == null || !clicked.getType().toString().endsWith("CONCRETE")) {
			return;
		}
		else if(clicked.getItemMeta().getDisplayName().contains("Wauzland")) {
			playerData.setSelectedCharacterWorld("Wauzland");
		}
		else if(clicked.getItemMeta().getDisplayName().contains("Dalyreos")) {
			playerData.setSelectedCharacterWorld("Dalyreos");
		}
		CharacterRaceClassMenu.open(player);
	}
	
}
