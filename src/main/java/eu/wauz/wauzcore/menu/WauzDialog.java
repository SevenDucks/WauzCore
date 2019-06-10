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
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Confirm: "
				+ pd.getWauzPlayerEventName());
		
		ItemStack confirm = HeadUtils.getConfirmItem();
		ItemMeta cim = confirm.getItemMeta();
		cim.setDisplayName(ChatColor.GREEN + "CONFIRM");
		confirm.setItemMeta(cim);
		menu.setItem(0, confirm);
		
		ItemStack decline = HeadUtils.getDeclineItem();
		ItemMeta dim = decline.getItemMeta();
		dim.setDisplayName(ChatColor.RED + "DECLINE");
		decline.setItemMeta(dim);
		menu.setItem(8, decline);
		
		if(infoItemStack != null)
			menu.setItem(4, infoItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null) return;
		
		if(clicked == null)
			return;
		
		else if(HeadUtils.isHeadMenuItem(clicked, "CONFIRM")) {
			pd.getWauzPlayerEvent().execute(player);
		}
		
		else if(HeadUtils.isHeadMenuItem(clicked, "DECLINE")) {
			player.closeInventory();
		}
	}
	
}
