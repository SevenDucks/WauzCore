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

public class WauzModeMenu implements WauzInventory {
	
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
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		
		if(clicked == null || !ItemUtils.hasDisplayName(clicked))
			return;
		
		String modeName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
		selectMode((Player) event.getWhoClicked(), modeName);
	}
	
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
