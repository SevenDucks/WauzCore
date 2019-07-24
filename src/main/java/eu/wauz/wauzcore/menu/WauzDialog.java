package eu.wauz.wauzcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import net.md_5.bungee.api.ChatColor;

public class WauzDialog implements WauzInventory {
	
	public static void open(Player player) {
		open(player, null);
	}

	public static void open(Player player, ItemStack infoItemStack) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzDialog());
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Confirm: "
				+ playerData.getWauzPlayerEventName());
		
		ItemStack confirmItemStack = HeadUtils.getConfirmItem();
		ItemMeta confirmItemMeta = confirmItemStack.getItemMeta();
		confirmItemMeta.setDisplayName(ChatColor.GREEN + "CONFIRM");
		confirmItemStack.setItemMeta(confirmItemMeta);
		menu.setItem(0, confirmItemStack);
		
		ItemStack declineItemStack = HeadUtils.getDeclineItem();
		ItemMeta declineItemMeta = declineItemStack.getItemMeta();
		declineItemMeta.setDisplayName(ChatColor.RED + "DECLINE");
		declineItemStack.setItemMeta(declineItemMeta);
		menu.setItem(8, declineItemStack);
		
		if(infoItemStack != null)
			menu.setItem(4, infoItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(playerData == null || clicked == null)
			return;
		
		else if(HeadUtils.isHeadMenuItem(clicked, "CONFIRM")) {
			playerData.getWauzPlayerEvent().execute(player);
		}
		
		else if(HeadUtils.isHeadMenuItem(clicked, "DECLINE")) {
			player.closeInventory();
		}
	}
	
}