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

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import net.md_5.bungee.api.ChatColor;

public class DungeonItemMenu implements WauzInventory {
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new DungeonItemMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Dungeon Items");
		
		ItemStack unobtainedItemStack = new ItemStack(Material.BARRIER, 1);
		ItemMeta unobtainedItemMeta = unobtainedItemStack.getItemMeta();
		unobtainedItemMeta.setDisplayName(ChatColor.RED + "Unobtained Item");
		unobtainedItemStack.setItemMeta(unobtainedItemMeta);
		
// Load obtained Dungeon-Items
		
		ItemStack clockItemStack = new ItemStack(Material.CLOCK);
		ItemMeta clockItemMeta = clockItemStack.getItemMeta();
		clockItemMeta.setDisplayName(ChatColor.RED + "Unequip Item");
		List<String> clockLores = new ArrayList<String>();
		clockLores.add(ChatColor.GRAY + "Unequip your current Dungeon Item");
		clockLores.add(ChatColor.GRAY + "and replace it with a clock.");
		clockLores.add("");
		clockLores.add(ChatColor.YELLOW + "Default Selection");
		clockItemMeta.setLore(clockLores);
		clockItemStack.setItemMeta(clockItemMeta);
		menu.setItem(1, clockItemStack);
		
		ItemStack trackerItemStack = new ItemStack(Material.COMPASS);
		ItemMeta trackerItemMeta = trackerItemStack.getItemMeta();
		trackerItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Location Tracker");
		List<String> trackerLores = new ArrayList<String>();
		trackerLores.add(ChatColor.GRAY + "Points towards a tracked Location or Quest");
		trackerLores.add(ChatColor.GRAY + "that can be selected in your Questlog.");
		trackerLores.add("");
		trackerLores.add(ChatColor.YELLOW + "Tracked: " + PlayerConfigurator.getDungeonItemTrackerDestinationName(player));
		trackerItemMeta.setLore(trackerLores);
		trackerItemStack.setItemMeta(trackerItemMeta);
		menu.setItem(2, trackerItemStack);
		
		boolean unlockAll = player.hasPermission("wauz.debug.ditems");
						
		if(unlockAll || PlayerConfigurator.getDungeonItemHookString(player).equals("obtained")) {
			ItemStack hookItemStack = new ItemStack(Material.FISHING_ROD);
			ItemMeta hookItemMeta = hookItemStack.getItemMeta();
			hookItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Grappling Hook");
			List<String> hookLores = new ArrayList<String>();
			hookLores.add(ChatColor.GRAY + "Pull yourself towards Sponges");
			hookLores.add(ChatColor.GRAY + "or grab distant Enemies.");
			if(player.getWorld().getName().equals("Wauzland")) {
				hookLores.add("");
				hookLores.add(ChatColor.RED + "Found in Frostskull-Fortress");
			}
			hookItemMeta.setLore(hookLores);
			hookItemStack.setItemMeta(hookItemMeta);
			menu.setItem(4, hookItemStack);
		}
		else {
			menu.setItem(4, unobtainedItemStack);
		}

		if(unlockAll || PlayerConfigurator.getDungeonItemBombString(player).equals("obtained")) {
			ItemStack bombItemStack = new ItemStack(Material.SNOWBALL);
			ItemMeta bombItemMeta = bombItemStack.getItemMeta();
			bombItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Bag of Bombs");
			List<String> bombLores = new ArrayList<String>();
			bombLores.add(ChatColor.GRAY + "Blow up Sponges in your way");
			bombLores.add(ChatColor.GRAY + "or bomb nearby Enemies to the side");
			if(player.getWorld().getName().equals("Wauzland")) {
				bombLores.add("");
				bombLores.add(ChatColor.RED + "Found in Hellfire-Kettle");
			}
			bombItemMeta.setLore(bombLores);
			bombItemStack.setItemMeta(bombItemMeta);
			menu.setItem(5, bombItemStack);
		}
		else {
			menu.setItem(5, unobtainedItemStack);
		}

		if(unlockAll || PlayerConfigurator.getDungeonItemTrodString(player).equals("obtained")) {
			ItemStack trodItemStack = new ItemStack(Material.BLAZE_ROD);
			ItemMeta trodItemMeta = trodItemStack.getItemMeta();
			trodItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Thunder Rod");
			List<String> trodLores = new ArrayList<String>();
			trodLores.add(ChatColor.GRAY + "Let Lightning  Strike on Sponges,");
			trodLores.add(ChatColor.GRAY + "to power them and trigger Mechanisms.");
			if(player.getWorld().getName().equals("Wauzland")) {
				trodLores.add("");
				trodLores.add(ChatColor.RED + "Found in Angelblood-Temple");
			}
			trodItemMeta.setLore(trodLores);
			trodItemStack.setItemMeta(trodItemMeta);
			menu.setItem(6, trodItemStack);
		}
		else {
			menu.setItem(6, unobtainedItemStack);
		}

		if(unlockAll || PlayerConfigurator.getDungeonItemGlidString(player).equals("obtained")) {
			ItemStack glidItemStack = new ItemStack(Material.FEATHER);
			ItemMeta glidItemMeta = glidItemStack.getItemMeta();
			glidItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Chicken Glider");
			List<String> glidLores = new ArrayList<String>();
			glidLores.add(ChatColor.GRAY + "Use on a Sponge you are standing on,");
			glidLores.add(ChatColor.GRAY + "to ascend. While holding it you can glide.");
			if(player.getWorld().getName().equals("Wauzland")) {
				glidLores.add("");
				glidLores.add(ChatColor.RED + "Found in Blackplague-Fountain");
			}
			glidItemMeta.setLore(glidLores);
			glidItemStack.setItemMeta(glidItemMeta);
			menu.setItem(7, glidItemStack);
		}
		else {
			menu.setItem(7, unobtainedItemStack);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null)
			return;
		
// Check Dungeon-Items
		
		else if(clicked.getType().equals(Material.CLOCK)) {
			ItemStack clockItemStack = new ItemStack(Material.CLOCK);
			ItemMeta clockItemMeta = clockItemStack.getItemMeta();
			clockItemMeta.setDisplayName(ChatColor.RED + "No Item Equipped");
			clockItemMeta.setUnbreakable(true);
			clockItemStack.setItemMeta(clockItemMeta);
			player.getInventory().setItem(7, clockItemStack);
			player.closeInventory();
		}
		
		else if(clicked.getType().equals(Material.COMPASS)) {
			ItemStack trackerItemStack = new ItemStack(Material.COMPASS);
			ItemMeta trackerItemMeta = trackerItemStack.getItemMeta();
			trackerItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Tracked: " + PlayerConfigurator.getDungeonItemTrackerDestinationName(player));
			trackerItemMeta.setUnbreakable(true);
			trackerItemStack.setItemMeta(trackerItemMeta);
			player.getInventory().setItem(7, trackerItemStack);
			player.closeInventory();
		}
		
		else if(clicked.getType().equals(Material.FISHING_ROD)) {
			ItemStack hookItemStack = new ItemStack(Material.FISHING_ROD);
			ItemMeta hookItemMeta = hookItemStack.getItemMeta();
			hookItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Grappling Hook");
			hookItemMeta.setUnbreakable(true);
			hookItemStack.setItemMeta(hookItemMeta);
			player.getInventory().setItem(7, hookItemStack);
			player.closeInventory();
		}

		else if(clicked.getType().equals(Material.SNOWBALL)) {
			ItemStack bombItemStack = new ItemStack(Material.SNOWBALL);
			ItemMeta bombItemMeta = bombItemStack.getItemMeta();
			bombItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Bag of Bombs");
			bombItemMeta.setUnbreakable(true);
			bombItemStack.setItemMeta(bombItemMeta);
			player.getInventory().setItem(7, bombItemStack);
			player.closeInventory();
		}
						
		else if(clicked.getType().equals(Material.BLAZE_ROD)) {
			ItemStack trodItemStack = new ItemStack(Material.BLAZE_ROD);
			ItemMeta trodItemMeta = trodItemStack.getItemMeta();
			trodItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Thunder Rod");
			trodItemMeta.setUnbreakable(true);
			trodItemStack.setItemMeta(trodItemMeta);
			player.getInventory().setItem(7, trodItemStack);
			player.closeInventory();
		}
						
		else if(clicked.getType().equals(Material.FEATHER)) {
			ItemStack glidItemStack = new ItemStack(Material.FEATHER);
			ItemMeta glidItemMeta = glidItemStack.getItemMeta();
			glidItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Chicken Glider");
			glidItemMeta.setUnbreakable(true);
			glidItemStack.setItemMeta(glidItemMeta);
			player.getInventory().setItem(7, glidItemStack);
			player.closeInventory();
		}	
	}

}
