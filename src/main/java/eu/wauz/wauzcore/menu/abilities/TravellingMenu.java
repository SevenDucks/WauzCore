package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the abilities menu, that is used to teleport the player to other locations.
 * 
 * @author Wauzmons
 * 
 * @see WauzTeleporter
 */
@PublicMenu
public class TravellingMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "travelling";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG);
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		TravellingMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows teleport items, to the hub, spawn, home and instance exit, aswell as a link to the overview map.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerConfigurator#getCharacterHearthstoneRegion(Player)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Travelling Menu";
		Inventory menu = Components.inventory(new TravellingMenu(), menuTitle, 27);
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		ItemStack portNexusItemStack = new ItemStack(Material.ENDER_EYE);
		ItemMeta portNexusItemMeta = portNexusItemStack.getItemMeta();
		Components.displayName(portNexusItemMeta, ChatColor.GREEN + "Nexus Portal");
		List<String> portNexusLores = new ArrayList<>();
		portNexusLores.add(ChatColor.GRAY + "Sends you back to the Nexus Hub,");
		portNexusLores.add(ChatColor.GRAY + "where you can select your characters.");
		Components.lore(portNexusItemMeta, portNexusLores);
		portNexusItemStack.setItemMeta(portNexusItemMeta);
		menu.setItem(10, portNexusItemStack);
		
		ItemStack portSpawnItemStack = new ItemStack(Material.ENDER_PEARL);
		ItemMeta portSpawnItemMeta = portSpawnItemStack.getItemMeta();
		Components.displayName(portSpawnItemMeta, ChatColor.GREEN + "Spawn Portal");
		List<String> portSpawnLores = new ArrayList<>();
		portSpawnLores.add(ChatColor.GRAY + "Sends you to your Overworld Spawn.");
		portSpawnLores.add(ChatColor.GRAY + "Use it when stuck or for quick-travel.");
		Components.lore(portSpawnItemMeta, portSpawnLores);
		portSpawnItemStack.setItemMeta(portSpawnItemMeta);
		menu.setItem(11, portSpawnItemStack);
		
		ItemStack portHomeItemStack = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta portHomeItemMeta = portHomeItemStack.getItemMeta();
		Components.displayName(portHomeItemMeta, ChatColor.GREEN + "Hearthstone");
		List<String> portHomeLores = new ArrayList<>();
		String region = PlayerConfigurator.getCharacterHearthstoneRegion(player);
		region = StringUtils.isNotBlank(region) ? ChatColor.GREEN + region : ChatColor.DARK_GRAY + "(None)";
		portHomeLores.add(ChatColor.GRAY + "Sends you to your home: " + region);
		portHomeLores.add(ChatColor.GRAY + "Speak to an Innkeeper to change it.");
		Components.lore(portHomeItemMeta, portHomeLores);
		portHomeItemStack.setItemMeta(portHomeItemMeta);
		menu.setItem(12, portHomeItemStack);
		
		boolean inInstance = WauzPlayerDataPool.isCharacterSelected(player) && !StringUtils.equals(player.getWorld().getName(), PlayerConfigurator.getCharacterWorldString(player));
		ItemStack portInstanceExitItemStack = new ItemStack(inInstance ? Material.OAK_DOOR : Material.IRON_DOOR);
		ItemMeta portInstanceExitItemMeta = portInstanceExitItemStack.getItemMeta();
		Components.displayName(portInstanceExitItemMeta, ChatColor.YELLOW + "Leave Instance" + (inInstance ? "" : ChatColor.RED + " (Disabled)"));
		List<String> portInstanceExitLore = new ArrayList<>();
		portInstanceExitLore.add(ChatColor.GRAY + "Leave the instance and return to the");
		portInstanceExitLore.add(ChatColor.GRAY + "place, from where you entered it.");
		Components.lore(portInstanceExitItemMeta, portInstanceExitLore);
		portInstanceExitItemStack.setItemMeta(portInstanceExitItemMeta);
		menu.setItem(14, portInstanceExitItemStack);
		
		ItemStack mapItemStack = new ItemStack(Material.MAP);
		ItemMeta mapItemMeta = mapItemStack.getItemMeta();
		Components.displayName(mapItemMeta, ChatColor.BLUE + "Overview Map");
		List<String> mapLores = new ArrayList<>();
		mapLores.add(ChatColor.GRAY + "Sends a link in chat to the Overview Map,");
		mapLores.add(ChatColor.GRAY + "where you can see your position.");
		Components.lore(mapItemMeta, mapLores);
		mapItemStack.setItemMeta(mapItemMeta);
		menu.setItem(16, mapItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and initiates the corresponding teleport over the teleporter.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzTeleporter#hubTeleportManual(Player)
	 * @see WauzTeleporter#spawnTeleportManual(Player)
	 * @see WauzTeleporter#hearthstoneTeleport(Player)
	 * @see WauzTeleporter#exitInstanceTeleportManual(Player)
	 * @see UnicodeUtils#sendChatHyperlink(Player, String, String, boolean)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot >= 27) {
			return;
		}
		
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		if(slot == 10) {
			WauzTeleporter.hubTeleportManual(player);
		}
		else if(slot == 11) {
			WauzTeleporter.spawnTeleportManual(player);
		}
		else if(slot == 12) {
			WauzTeleporter.hearthstoneTeleport(player);
		}
		else if(slot == 14) {
			WauzTeleporter.exitInstanceTeleportManual(player);
		}
		else if(slot == 16) {
			UnicodeUtils.sendChatHyperlink(player, "http://map.wauz.eu",
					ChatColor.YELLOW + "To open the Overview Map:", true);
			player.closeInventory();
		}
	}
	
}
