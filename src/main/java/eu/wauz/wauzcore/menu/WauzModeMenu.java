package eu.wauz.wauzcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * The main menu of the hub, that will let the player select a gamemode to play.
 * 
 * @author Wauzmons
 *
 * @see WauzMode
 */
public class WauzModeMenu implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * Shows three hardcoded modes to choose: "MineKart", "MMORPG", "Survival".
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzModeMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Select a Gamemode!");
		
		ItemStack modeKartItemStack = new ItemStack(Material.MINECART);
		ItemMeta modeKartItemMeta = modeKartItemStack.getItemMeta();
		modeKartItemMeta.setDisplayName(ChatColor.RED + "MineKart");
		modeKartItemStack.setItemMeta(modeKartItemMeta);
		menu.setItem(2, modeKartItemStack);
		
		ItemStack modeMmoItemStack = new ItemStack(Material.DRAGON_HEAD);
		ItemMeta modeMmoItemMeta = modeMmoItemStack.getItemMeta();
		modeMmoItemMeta.setDisplayName(ChatColor.DARK_PURPLE + "MMORPG");
		modeMmoItemStack.setItemMeta(modeMmoItemMeta);
		menu.setItem(4, modeMmoItemStack);
		
		ItemStack modeSurvivalItemStack = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta modeSurvivalItemMeta = modeSurvivalItemStack.getItemMeta();
		modeSurvivalItemMeta.setDisplayName(ChatColor.GOLD + "Survival");
		modeSurvivalItemStack.setItemMeta(modeSurvivalItemMeta);
		menu.setItem(6, modeSurvivalItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If the clicked item has a display name, it will be used as selected mode name.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzModeMenu#selectMode(Player, String)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		
		if(clicked == null || !ItemUtils.hasDisplayName(clicked)) {
			return;
		}
		
		String modeName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
		selectMode((Player) event.getWhoClicked(), modeName);
	}
	
	/**
	 * Starts the given mode for the player.
	 * For MMORPG and Survival mode, the character selection will be shown.
	 * The MineKart mode shows just a warning, that it is unfinished.
	 * 
	 * @param player The player who selected the mode.
	 * @param modeName The name of the mode that has been selected.
	 * 
	 * @see CharacterSlotMenu#open(Player, WauzMode)
	 */
	public static void selectMode(Player player, String modeName) {
		if(modeName == null) {
			return;
		}
		else if(modeName.equals("MMORPG")) {
			CharacterSlotMenu.open(player, WauzMode.MMORPG);
		}
		else if(modeName.equals("Survival")) {
			CharacterSlotMenu.open(player, WauzMode.SURVIVAL);
		}
		else if(modeName.equals("MineKart")) {
			player.closeInventory();
			player.sendMessage(ChatColor.RED + "This mode is still unfinished!");
		}
	}

}
