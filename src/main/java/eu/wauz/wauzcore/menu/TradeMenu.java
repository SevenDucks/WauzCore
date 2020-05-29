package eu.wauz.wauzcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TradeMenu {
	public static void openTrade(Player requestingPlayer, Player requestedPlayer) {
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + requestingPlayer.getName() + ChatColor.DARK_GREEN + "    " + ChatColor.BOLD + "On Trade" + "    " + ChatColor.BLACK + "" + ChatColor.BOLD + requestedPlayer.getName() ;
		Inventory tradeMenu = Bukkit.createInventory(null, 27, menuTitle );
		
		requestingPlayer.openInventory(tradeMenu);
		requestedPlayer.openInventory(tradeMenu);
	}
}
