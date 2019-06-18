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
		
		ItemStack unobtained = new ItemStack(Material.BARRIER, 1);
		ItemMeta uim = unobtained.getItemMeta();
		uim.setDisplayName(ChatColor.RED + "Unobtained Item");
		unobtained.setItemMeta(uim);
		
// Load obtained Dungeon-Items
		
		{
			ItemStack empty = new ItemStack(Material.CLOCK);
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(ChatColor.RED + "Unequip Item");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Unequip your current Dungeon Item");
			lores.add(ChatColor.GRAY + "and replace it with a clock.");
			lores.add("");
			lores.add(ChatColor.YELLOW + "Default Selection");
			im.setLore(lores);
			empty.setItemMeta(im);
			menu.setItem(1, empty);
		}
		
		{
			ItemStack tracker = new ItemStack(Material.COMPASS);
			ItemMeta im = tracker.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Location Tracker");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Points towards a tracked Location or Quest");
			lores.add(ChatColor.GRAY + "that can be selected in your Questlog.");
			lores.add("");
			lores.add(ChatColor.YELLOW + "Tracked: " + PlayerConfigurator.getDungeonItemTrackerDestinationName(player));
			im.setLore(lores);
			tracker.setItemMeta(im);
			menu.setItem(2, tracker);
		}
		
		boolean unlockAll = player.hasPermission("wauz.debug.ditems");
						
		if(unlockAll || PlayerConfigurator.getDungeonItemHookString(player).equals("obtained")) {
			ItemStack hook = new ItemStack(Material.FISHING_ROD);
			ItemMeta im = hook.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Grappling Hook");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Pull yourself towards Sponges");
			lores.add(ChatColor.GRAY + "or grab distant Enemies.");
//			lores.add("");
//			lores.add(ChatColor.RED + "Found in Frostskull-Fortress");
			im.setLore(lores);
			hook.setItemMeta(im);
			menu.setItem(4, hook);
		}
		else
			menu.setItem(4, unobtained);

		if(unlockAll || PlayerConfigurator.getDungeonItemBombString(player).equals("obtained")) {
			ItemStack bomb = new ItemStack(Material.SNOWBALL);
			ItemMeta im = bomb.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Bag of Bombs");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Blow up Sponges in your way");
			lores.add(ChatColor.GRAY + "or bomb nearby Enemies to the side");
//			lores.add("");
//			lores.add(ChatColor.RED + "Found in Hellfire-Kettle");
			im.setLore(lores);
			bomb.setItemMeta(im);
			menu.setItem(5, bomb);
		}
		else
			menu.setItem(5, unobtained);

		if(unlockAll || PlayerConfigurator.getDungeonItemTrodString(player).equals("obtained")) {
			ItemStack trod = new ItemStack(Material.BLAZE_ROD);
			ItemMeta im = trod.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Thunder Rod");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Let Lightning  Strike on Sponges,");
			lores.add(ChatColor.GRAY + "to power them and trigger Mechanisms.");
//			lores.add("");
//			lores.add(ChatColor.RED + "Found in Angelblood-Temple");
			im.setLore(lores);
			trod.setItemMeta(im);
			menu.setItem(6, trod);
		}
		else
			menu.setItem(6, unobtained);

		if(unlockAll || PlayerConfigurator.getDungeonItemGlidString(player).equals("obtained")) {
			ItemStack glid = new ItemStack(Material.FEATHER);
			ItemMeta im = glid.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Chicken Glider");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Use on a Sponge you are standing on,");
			lores.add(ChatColor.GRAY + "to ascend. While holding it you can glide.");
//			lores.add("");
//			lores.add(ChatColor.RED + "Found in Blackplague-Fountain");
			im.setLore(lores);
			glid.setItemMeta(im);
			menu.setItem(7, glid);
		}
		else
			menu.setItem(7, unobtained);
		
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
			ItemStack empty = new ItemStack(Material.CLOCK);
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(ChatColor.RED + "No Item Equipped");
			im.setUnbreakable(true);
			empty.setItemMeta(im);
			player.getInventory().setItem(7, empty);
			player.closeInventory();
		}
		
		else if(clicked.getType().equals(Material.COMPASS)) {
			ItemStack tracker = new ItemStack(Material.COMPASS);
			ItemMeta im = tracker.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Tracked: " + PlayerConfigurator.getDungeonItemTrackerDestinationName(player));
			im.setUnbreakable(true);
			tracker.setItemMeta(im);
			player.getInventory().setItem(7, tracker);
			player.closeInventory();
		}
		
		else if(clicked.getType().equals(Material.FISHING_ROD)) {
			ItemStack hook = new ItemStack(Material.FISHING_ROD);
			ItemMeta im = hook.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Grappling Hook");
			im.setUnbreakable(true);
			hook.setItemMeta(im);
			player.getInventory().setItem(7, hook);
			player.closeInventory();
		}

		else if(clicked.getType().equals(Material.SNOWBALL)) {
			ItemStack bomb = new ItemStack(Material.SNOWBALL);
			ItemMeta im = bomb.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Bag of Bombs");
			im.setUnbreakable(true);
			bomb.setItemMeta(im);
			player.getInventory().setItem(7, bomb);
			player.closeInventory();
		}
						
		else if(clicked.getType().equals(Material.BLAZE_ROD)) {
			ItemStack trod = new ItemStack(Material.BLAZE_ROD);
			ItemMeta im = trod.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Thunder Rod");
			im.setUnbreakable(true);
			trod.setItemMeta(im);
			player.getInventory().setItem(7, trod);
			player.closeInventory();
		}
						
		else if(clicked.getType().equals(Material.FEATHER)) {
			ItemStack glid = new ItemStack(Material.FEATHER);
			ItemMeta im = glid.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Chicken Glider");
			im.setUnbreakable(true);
			glid.setItemMeta(im);
			player.getInventory().setItem(7, glid);
			player.closeInventory();
		}	
	}

}
