package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerMailConfigurator;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerMail;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for viewing received mails.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerMail
 */
public class MailMenu implements WauzInventory {

	/**
	 * Opens the menu for the given player.
	 * Shows a list of unread mails, that can be opened by clicking them.
	 * Also shows instructions on how to send mails.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerMailConfigurator#getPlayerMailNameList(Player)
	 * @see PlayerMailConfigurator#getMailSender(org.bukkit.OfflinePlayer, String)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzMenu());
		Inventory menu = Bukkit.createInventory(holder, 18, ChatColor.BLACK + "" + ChatColor.BOLD + "Mailbox");
		
		List<String> mailNames = PlayerMailConfigurator.getPlayerMailNameList(player);
		for(int index = 0; index < 16 && index < mailNames.size(); index++) {
			String mailName = mailNames.get(index);
			String sender = PlayerMailConfigurator.getMailSender(player, mailName);
			long timestamp = Long.parseLong(StringUtils.substringBefore(mailName, "_"));
			
			ItemStack mailItemStack = HeadUtils.getMailItem();
			ItemMeta mailItemMeta = mailItemStack.getItemMeta();
			mailItemMeta.setDisplayName(ChatColor.YELLOW + "Mail from " + sender);
			List<String> mailLores = new ArrayList<>();
			mailLores.add(ChatColor.GRAY + "Received " + WauzDateUtils.formatTimeSince(timestamp) + " ago");
			mailLores.add(ChatColor.GRAY + "Click to open Mail");
			mailItemMeta.setLore(mailLores);
			mailItemStack.setItemMeta(mailItemMeta);
			menu.setItem(index + 2, mailItemStack);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		// TODO Auto-generated method stub
	}

}
