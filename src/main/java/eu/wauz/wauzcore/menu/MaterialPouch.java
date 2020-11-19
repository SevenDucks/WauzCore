package eu.wauz.wauzcore.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventMaterialsSell;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.economy.WauzShopActions;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Represents a virtual chest, to store materials and quest items of a player.
 * 
 * @author Wauzmons
 */
public class MaterialPouch implements WauzInventory {
	
	private static Map<String, Inventory> inventoryMap = new HashMap<>();
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "materials";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		MaterialPouch.open(player, inventoryName);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows the contents of the default section of the material pouch.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see MaterialPouch#open(Player, String)
	 */
	public static void open(Player player) {
		MaterialPouch.open(player, "materials");
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows the contents of the given section of the material pouch.
	 * 
	 * @param player The player that should view the inventory.
	 * @param inventoryName The name of the inventory, representing the section of the pouch to show.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, String inventoryName) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new MaterialPouch(inventoryName));
		String displayName = inventoryName.equals("materials") ? "Materials" : "Quest Items";
		Inventory menu = Bukkit.createInventory(holder, 36, ChatColor.BLACK + "" + ChatColor.BOLD + displayName);
		
		ItemStack emptyItemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta emptyItemMeta = emptyItemStack.getItemMeta();
		emptyItemMeta.setDisplayName(" ");
		emptyItemStack.setItemMeta(emptyItemMeta);
		
		int index = 0;
		for(ItemStack itemStack : getInventory(player, inventoryName).getContents()) {
			if(itemStack != null) {
				ItemStack materialItemStack = itemStack.clone();
				MenuUtils.addItemLore(materialItemStack, "", false);
				MenuUtils.addItemLore(materialItemStack, ChatColor.GRAY + "Right Click to Sell", false);
				menu.setItem(index, materialItemStack);
			}
			else {
				menu.setItem(index, emptyItemStack);
			}
			index++;
		}
		
		ItemStack sellAllItemStack = GenericIconHeads.getCitizenShopItem();
		MenuUtils.setItemDisplayName(sellAllItemStack, ChatColor.GREEN + "Sell All");
		menu.setItem(29, sellAllItemStack);
		
		ItemStack switchPouchItemStack = MenuIconHeads.getBagItem();
		if(inventoryName.equals("materials")) {
			MenuUtils.setItemDisplayName(switchPouchItemStack, ChatColor.YELLOW + "Show Quest Items");
		}
		else {
			MenuUtils.setItemDisplayName(switchPouchItemStack, ChatColor.AQUA + "Show Materials");
		}
		menu.setItem(33, switchPouchItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Gets or creates / loads the inventory with the given name for the player.
	 * 
	 * @param player The player who owns the pouch that contains the inventory.
	 * @param inventoryName The name of the inventory.
	 */
	public static Inventory getInventory(Player player, String inventoryName) {
		String inventoryKey = player.getUniqueId() + "::" + inventoryName;
		Inventory inventory = inventoryMap.get(inventoryKey);
		if(inventory == null) {
			inventory = Bukkit.createInventory(null, 27);
			inventory.setContents(PlayerCollectionConfigurator.getCharacterInventoryContents(player, inventoryName));
			inventoryMap.put(inventoryKey, inventory);
		}
		return inventory;
	}
	
	/**
	 * Unloads the cache of the inventory with the given name for the player.
	 * 
	 * @param player The player who owns the pouch that contains the inventory.
	 * @param inventoryName The name of the inventory.
	 */
	public static void unloadInventory(Player player, String inventoryName) {
		String inventoryKey = player.getUniqueId() + "::" + inventoryName;
		inventoryMap.remove(inventoryKey);
	}
	
	/**
	 * Let's the player add an item to the inventory, if there is enough space.
	 * 
	 * @param player The player who owns the pouch that contains the inventory.
	 * @param itemStack The item stack to add to the inventory.
	 * @param inventoryName The name of the inventory.
	 * 
	 * @return If the item fit inside the inventory.
	 */
	public static boolean addItem(Player player, ItemStack itemStack, String inventoryName) {
		Inventory inventory = MaterialPouch.getInventory(player, inventoryName);
		if(inventory.addItem(WauzNmsClient.nmsSerialize(itemStack)).isEmpty()) {
			String displayName = itemStack.getItemMeta().getDisplayName();
			String message = ChatColor.AQUA + "Found Material: " + displayName;
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
			player.sendTitle("", message, 2, 14, 4);
			return true;
		}
		else {
			String message = ChatColor.RED + "Material Bag is full!";
			player.playSound(player.getLocation(), Sound.BLOCK_LADDER_BREAK, 1, 1);
			player.sendTitle("", message, 2, 14, 4);
			return false;
		}
	}
	
	/**
	 * The name of the inventory, representing the section of the pouch to show.
	 */
	private String inventoryName;
	
	/**
	 * Creates a new menu to display the contents of the given section of the material pouch.
	 * 
	 * @param inventoryName The name of the inventory, representing the section of the pouch to show.
	 */
	public MaterialPouch(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and lets the player change views or sell materials.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzShopActions#sell(Player, ItemStack, boolean)
	 * @see WauzPlayerEventMaterialsSell
	 * @see MaterialPouch#open(Player, String)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		int slot = event.getRawSlot();
		
		if(slot < 0) {
			return;
		}
		else if(event.getClick().toString().contains("RIGHT") && slot >= 0 && slot < 27) {
			ItemStack[] itemStacks = getInventory(player, inventoryName).getContents();
			if(slot >= itemStacks.length || itemStacks[slot] == null) {
				return;
			}
			WauzShopActions.sell(player, itemStacks[slot], true);
			MaterialPouch.open(player, inventoryName);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Sell All")) {
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			playerData.setWauzPlayerEventName("Sell " + StringUtils.capitalize(inventoryName));
			playerData.setWauzPlayerEvent(new WauzPlayerEventMaterialsSell(inventoryName));
			WauzDialog.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Show Materials")) {
			open(player, "materials");
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Show Quest Items")) {
			open(player, "questitems");
		}
	}

}
