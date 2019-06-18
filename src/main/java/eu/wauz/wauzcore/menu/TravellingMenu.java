package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import net.md_5.bungee.api.ChatColor;

public class TravellingMenu implements WauzInventory {
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new TravellingMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Travelling Menu");
		
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null)
			return;
		
		{
			ItemStack port = new ItemStack(Material.ENDER_EYE);
			ItemMeta im = port.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "Nexus Portal");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Sends you back to the Nexus Hub,");
			lores.add(ChatColor.GRAY + "where you can select your characters.");
			im.setLore(lores);
			port.setItemMeta(im);
			menu.setItem(0, port);
		}
		
		{
			ItemStack port = new ItemStack(Material.ENDER_PEARL);
			ItemMeta im = port.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "Spawn Portal");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Sends you to your Overworld Spawn.");
			lores.add(ChatColor.GRAY + "Use it when stuck or for quick-travel.");
			im.setLore(lores);
			port.setItemMeta(im);
			menu.setItem(1, port);
		}
		
		{
			ItemStack port = new ItemStack(Material.MAGMA_CREAM);
			ItemMeta im = port.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "Hearthstone");
			List<String> lores = new ArrayList<String>();
			String region = PlayerConfigurator.getCharacterHearthstoneRegion(player);
			region = StringUtils.isNotBlank(region) ? ChatColor.GREEN + region : ChatColor.DARK_GRAY + "(None)";
			lores.add(ChatColor.GRAY + "Sends you to your home: " + region);
			lores.add(ChatColor.GRAY + "Speak to an (" + ChatColor.RED + "I" + ChatColor.GRAY + ")nnkeeper to change it.");
			im.setLore(lores);
			port.setItemMeta(im);
			menu.setItem(2, port);
		}
		
		boolean inInstance = player.getWorld().getName().contains("Instance");
		{
			ItemStack port = new ItemStack(inInstance ? Material.OAK_DOOR : Material.IRON_DOOR);
			ItemMeta im = port.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "Leave Instance" + (inInstance ? "" : ChatColor.RED + " (Disabled)"));
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Leave the instance and return to the");
			lores.add(ChatColor.GRAY + "place, from where you entered it.");
			im.setLore(lores);
			port.setItemMeta(im);
			menu.setItem(3, port);
		}
		
		{
			ItemStack omap = new ItemStack(Material.MAP);
			ItemMeta im = omap.getItemMeta();
			im.setDisplayName(ChatColor.BLUE + "Overview Map");
			List<String> lores = new ArrayList<String>();
			String region = PlayerConfigurator.getCharacterHearthstoneRegion(player);
			region = StringUtils.isNotBlank(region) ? ChatColor.GREEN + region : ChatColor.DARK_GRAY + "(None)";
			lores.add(ChatColor.GRAY + "Sends a link in chat to the Overview Map,");
			lores.add(ChatColor.GRAY + "where you can see your position.");
			im.setLore(lores);
			omap.setItemMeta(im);
			menu.setItem(7, omap);
		}
		
		MenuUtils.setComingSoon(menu, "Lore Collection", 8);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null)
			return;
		
		else if(clicked.getType().equals(Material.ENDER_EYE))
			WauzTeleporter.hubTeleportManual(player);
		
		else if(clicked.getType().equals(Material.ENDER_PEARL))
			WauzTeleporter.spawnTeleportManual(player);
		
		else if(clicked.getType().equals(Material.MAGMA_CREAM))
			WauzTeleporter.hearthstoneTeleport(player);
		
		else if(clicked.getType().equals(Material.OAK_DOOR))
			WauzTeleporter.exitInstanceTeleportManual(player);
		
		else if(clicked.getType().equals(Material.MAP)) {
			WauzNmsClient.nmsChatHyperlink(player, "http://wauz.eu/map.html",
					ChatColor.YELLOW + "Open the Overview Map:", true);
			player.closeInventory();
		}
	}
	
}
