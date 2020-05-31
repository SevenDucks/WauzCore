package eu.wauz.wauzcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class TradeMenu implements Listener {
	public static void openTrade(Player requestingPlayer, Player requestedPlayer) {
		String menuTitle = ChatColor.BLACK + "" + requestingPlayer.getName() + ChatColor.DARK_GREEN + "  " + ChatColor.BOLD + "On Trade" + "  " + ChatColor.BLACK + "" + requestedPlayer.getName() ;
		Inventory tradeMenu = Bukkit.createInventory(null, 27, menuTitle );
		
		requestingPlayer.openInventory(tradeMenu);
		requestedPlayer.openInventory(tradeMenu);
	}
}
