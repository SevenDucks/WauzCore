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

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
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
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		ItemStack portNexusItemStack = new ItemStack(Material.ENDER_EYE);
		ItemMeta portNexusItemMeta = portNexusItemStack.getItemMeta();
		portNexusItemMeta.setDisplayName(ChatColor.GREEN + "Nexus Portal");
		List<String> portNexusLores = new ArrayList<String>();
		portNexusLores.add(ChatColor.GRAY + "Sends you back to the Nexus Hub,");
		portNexusLores.add(ChatColor.GRAY + "where you can select your characters.");
		portNexusItemMeta.setLore(portNexusLores);
		portNexusItemStack.setItemMeta(portNexusItemMeta);
		menu.setItem(0, portNexusItemStack);
		
		ItemStack portSpawnItemStack = new ItemStack(Material.ENDER_PEARL);
		ItemMeta portSpawnItemMeta = portSpawnItemStack.getItemMeta();
		portSpawnItemMeta.setDisplayName(ChatColor.GREEN + "Spawn Portal");
		List<String> portSpawnLores = new ArrayList<String>();
		portSpawnLores.add(ChatColor.GRAY + "Sends you to your Overworld Spawn.");
		portSpawnLores.add(ChatColor.GRAY + "Use it when stuck or for quick-travel.");
		portSpawnItemMeta.setLore(portSpawnLores);
		portSpawnItemStack.setItemMeta(portSpawnItemMeta);
		menu.setItem(1, portSpawnItemStack);
		
		ItemStack portHomeItemStack = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta portHomeItemMeta = portHomeItemStack.getItemMeta();
		portHomeItemMeta.setDisplayName(ChatColor.GREEN + "Hearthstone");
		List<String> portHomeLores = new ArrayList<String>();
		String region = PlayerConfigurator.getCharacterHearthstoneRegion(player);
		region = StringUtils.isNotBlank(region) ? ChatColor.GREEN + region : ChatColor.DARK_GRAY + "(None)";
		portHomeLores.add(ChatColor.GRAY + "Sends you to your home: " + region);
		portHomeLores.add(ChatColor.GRAY + "Speak to an (" + ChatColor.RED + "I" + ChatColor.GRAY + ")nnkeeper to change it.");
		portHomeItemMeta.setLore(portHomeLores);
		portHomeItemStack.setItemMeta(portHomeItemMeta);
		menu.setItem(2, portHomeItemStack);
		
		boolean inInstance = player.getWorld().getName().contains("Instance");
		ItemStack portInstanceExitItemStack = new ItemStack(inInstance ? Material.OAK_DOOR : Material.IRON_DOOR);
		ItemMeta portInstanceExitItemMeta = portInstanceExitItemStack.getItemMeta();
		portInstanceExitItemMeta.setDisplayName(ChatColor.YELLOW + "Leave Instance" + (inInstance ? "" : ChatColor.RED + " (Disabled)"));
		List<String> portInstanceExitLore = new ArrayList<String>();
		portInstanceExitLore.add(ChatColor.GRAY + "Leave the instance and return to the");
		portInstanceExitLore.add(ChatColor.GRAY + "place, from where you entered it.");
		portInstanceExitItemMeta.setLore(portInstanceExitLore);
		portInstanceExitItemStack.setItemMeta(portInstanceExitItemMeta);
		menu.setItem(3, portInstanceExitItemStack);
		
		ItemStack mapItemStack = new ItemStack(Material.MAP);
		ItemMeta mapItemMeta = mapItemStack.getItemMeta();
		mapItemMeta.setDisplayName(ChatColor.BLUE + "Overview Map");
		List<String> mapLores = new ArrayList<String>();
		mapLores.add(ChatColor.GRAY + "Sends a link in chat to the Overview Map,");
		mapLores.add(ChatColor.GRAY + "where you can see your position.");
		mapItemMeta.setLore(mapLores);
		mapItemStack.setItemMeta(mapItemMeta);
		menu.setItem(7, mapItemStack);
		
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
