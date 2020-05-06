package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerMailConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventMailClaim;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
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
		int mailAmount = mailNames.size();
		int visibleMailAmount = mailAmount > 16 ? 16 : mailAmount;
		
		ItemStack inboxItemStack = HeadUtils.getAchievementIdentifiesItem();
		ItemMeta inboxItemMeta = inboxItemStack.getItemMeta();
		inboxItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Inbox");
		List<String> inboxLores = new ArrayList<>();
		inboxLores.add(ChatColor.DARK_PURPLE + "Showing " + ChatColor.YELLOW
				+ visibleMailAmount + ChatColor.DARK_PURPLE + " out of " + ChatColor.YELLOW
				+ mailAmount + ChatColor.DARK_PURPLE + " Mails");
		inboxLores.add("");
		inboxLores.add(ChatColor.GRAY + "You can view 16 Mails at a time.");
		inboxLores.add(ChatColor.GRAY + "To view more, claim the existing ones.");
		inboxItemMeta.setLore(inboxLores);
		inboxItemStack.setItemMeta(inboxItemMeta);
		menu.setItem(0, inboxItemStack);
		
		ItemStack sendItemStack = HeadUtils.getCitizenCommandItem();
		ItemMeta sendItemMeta = sendItemStack.getItemMeta();
		sendItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Send Mails");
		List<String> sendLores = new ArrayList<>();
		sendLores.add("");
		sendLores.add("");
		sendLores.add(ChatColor.DARK_PURPLE + "Commands:");
		sendLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "send [player] [text] " + ChatColor.GRAY + "Send a Text Mail to a Player");
		sendLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "send.item [player] [text] " + ChatColor.GRAY + "Send your Hand Item to a Player");
		sendLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "send.coins [player] [amount] [text] " + ChatColor.GRAY + "Send Coins to a Player");
		sendItemMeta.setLore(sendLores);
		sendItemStack.setItemMeta(sendItemMeta);
		menu.setItem(1, sendItemStack);
		
		for(int index = 0; index < 16 && index < mailAmount; index++) {
			String mailName = mailNames.get(index);
			String sender = PlayerMailConfigurator.getMailSender(player, mailName);
			long timestamp = Long.parseLong(StringUtils.substringBefore(mailName, "_"));
			
			ItemStack mailItemStack = HeadUtils.getMailItem();
			ItemMeta mailItemMeta = mailItemStack.getItemMeta();
			mailItemMeta.setDisplayName(ChatColor.YELLOW + "Mail from " + sender);
			List<String> mailLores = new ArrayList<>();
			mailLores.add(ChatColor.GRAY + "Received " + WauzDateUtils.formatTimeSince(timestamp) + " ago");
			mailLores.add(ChatColor.GRAY + "Click to Claim");
			mailLores.add("");
			mailLores.add(ChatColor.DARK_GRAY + "UUID: " + mailName);
			mailItemMeta.setLore(mailLores);
			mailItemStack.setItemMeta(mailItemMeta);
			menu.setItem(index + 2, mailItemStack);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a mail was clicked, a dialog to claim it will be opened.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerEventMailClaim
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !ItemUtils.isMaterial(clicked, Material.PLAYER_HEAD)) {
			return;
		}
		String mailName = ItemUtils.getStringFromLore(clicked, "UUID", 1);
		if(mailName != null) {
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			WauzPlayerEventMailClaim playerEvent = new WauzPlayerEventMailClaim(player, mailName);
			playerData.setWauzPlayerEventName("Claim Mail");
			playerData.setWauzPlayerEvent(playerEvent);
			WauzDialog.open(player, playerEvent.getMailItemStack());
		}
	}

}
