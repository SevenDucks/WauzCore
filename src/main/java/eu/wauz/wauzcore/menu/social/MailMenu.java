package eu.wauz.wauzcore.menu.social;

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

import eu.wauz.wauzcore.data.players.PlayerMailConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventMailClaim;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerMail;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.kyori.adventure.text.Component;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for viewing received mails.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerMail
 */
@PublicMenu
public class MailMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "mails";
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
		MailMenu.open(player);
	}

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
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Mailbox";
		Inventory menu = Components.inventory(new MailMenu(), menuTitle, 18);
		List<String> mailNames = PlayerMailConfigurator.getPlayerMailNameList(player);
		int mailAmount = mailNames.size();
		int visibleMailAmount = mailAmount > 16 ? 16 : mailAmount;
		
		ItemStack inboxItemStack = GenericIconHeads.getAchievementIdentifiesItem();
		ItemMeta inboxItemMeta = inboxItemStack.getItemMeta();
		inboxItemMeta.displayName(Component.text(ChatColor.LIGHT_PURPLE + "Inbox"));
		List<String> inboxLores = new ArrayList<>();
		inboxLores.add(ChatColor.DARK_PURPLE + "Showing " + ChatColor.YELLOW
				+ visibleMailAmount + ChatColor.DARK_PURPLE + " out of " + ChatColor.YELLOW
				+ mailAmount + ChatColor.DARK_PURPLE + " Mails");
		inboxLores.add("");
		inboxLores.add(ChatColor.GRAY + "You can view 16 Mails at a time.");
		inboxLores.add(ChatColor.GRAY + "Claimed mails will be removed.");
		inboxLores.add(ChatColor.GRAY + "To view more, claim the existing ones.");
		inboxItemMeta.setLore(inboxLores);
		inboxItemStack.setItemMeta(inboxItemMeta);
		menu.setItem(0, inboxItemStack);
		
		ItemStack sendItemStack = GenericIconHeads.getCitizenCommandItem();
		ItemMeta sendItemMeta = sendItemStack.getItemMeta();
		sendItemMeta.displayName(Component.text(ChatColor.LIGHT_PURPLE + "Send Mails"));
		List<String> sendLores = new ArrayList<>();
		sendLores.add(ChatColor.DARK_PURPLE + "Sent Today: " + ChatColor.YELLOW
				+ WauzPlayerMail.getMailsSentToday(player) + " / " + WauzPlayerMail.MAX_MAILS_PER_DAY);
		sendLores.add("");
		sendLores.add(ChatColor.DARK_PURPLE + "Commands:");
		sendLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "send [player] [text]");
		sendLores.add(ChatColor.GRAY + "Send a Text Mail to a Player");
		sendLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "send.item [player] [text]");
		sendLores.add(ChatColor.GRAY + "Send your Hand Item to a Player");
		sendLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "send.coins [player] [amount] [text]");
		sendLores.add(ChatColor.GRAY + "Send Coins to a Player");
		sendItemMeta.setLore(sendLores);
		sendItemStack.setItemMeta(sendItemMeta);
		menu.setItem(1, sendItemStack);
		
		for(int index = 0; index < 16 && index < mailAmount; index++) {
			String mailName = mailNames.get(index);
			String sender = PlayerMailConfigurator.getMailSender(player, mailName);
			long timestamp = Long.parseLong(StringUtils.substringBefore(mailName, "_"));
			
			ItemStack mailItemStack = MenuIconHeads.getMailItem();
			ItemMeta mailItemMeta = mailItemStack.getItemMeta();
			mailItemMeta.displayName(Component.text(ChatColor.YELLOW + "Mail from " + sender));
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
			playerData.getSelections().setWauzPlayerEventName("Claim Mail");
			playerData.getSelections().setWauzPlayerEvent(playerEvent);
			WauzDialog.open(player, playerEvent.getMailItemStack());
		}
	}

}
