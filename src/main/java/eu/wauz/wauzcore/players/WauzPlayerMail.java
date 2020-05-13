package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerMailConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A mail with optional attachment, that can be send to a player.
 * 
 * @author Wauzmons
 */
public class WauzPlayerMail {
	
	/**
	 * The maximum amount of mails, a player can send per day.
	 */
	public static final int MAX_MAILS_PER_DAY = 10;
	
	/**
	 * The configuration key for the amount of sent mails.
	 */
	private static final String MAIL_TOKEN_KEY = "mails";
	
	/**
	 * The receiver of the mail.
	 */
	private OfflinePlayer receiver;
	
	/**
	 * The text content of the mail.
	 */
	private String textContent;
	
	/**
	 * The coin attachment of the mail.
	 */
	private long coinAttachment;
	
	/**
	 * The item attachment of the mail.
	 */
	private ItemStack itemAttachment;
	
	/**
	 * Checks if a player can send more mails today.
	 * 
	 * @param player The player to check for.
	 * 
	 * @return If the player can send more mails today.
	 */
	public static boolean canSendMail(Player player) {
		if(!WauzMode.isMMORPG(player) || WauzMode.inHub(player)) {
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
			return false;
		}
		if(getMailsSentToday(player) >= MAX_MAILS_PER_DAY) {
			player.sendMessage(ChatColor.RED + "You already reached the limit of mails sent today!");
			return false;
		}
		return true;
	}
		
	/**
	 * Gets the amount of mails sent for the current date.
	 * 
	 * @param player The player that sent the mails.
	 * 
	 * @return The amount of mails sent today.
	 * 
	 * @see WauzDateUtils#getDateLong()
	 * @see PlayerConfigurator#getTokenLimitDate(Player, String)
	 * @see PlayerConfigurator#getTokenLimitAmount(Player, String)
	 */
	public static int getMailsSentToday(Player player) {
		long dateLong = PlayerConfigurator.getTokenLimitDate(player, MAIL_TOKEN_KEY);
		long currentDateLong = WauzDateUtils.getDateLong();
		return dateLong < currentDateLong ? 0 : PlayerConfigurator.getTokenLimitAmount(player, MAIL_TOKEN_KEY);
	}
	
	/**
	 * Tries to set the receiver of the mail.
	 * 
	 * @param player The player who is sending the mail.
	 * @param receiverName The name of the player, receiving the mail.
	 * 
	 * @return If it was successful.
	 */
	public boolean tryToSetReceiver(Player player, String receiverName) {
		OfflinePlayer receiver = WauzCore.getOfflinePlayer(receiverName);
		if(receiver != null) {
			this.receiver = receiver;
			return true;
		}
		else {
			player.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
	}
	
	/**
	 * Tries to set the coin attachment of the mail.
	 * 
	 * @param player The player who is sending the mail.
	 * @param coinAmount The amount of coins to attach.
	 * 
	 * @return If it was successful.
	 */
	public boolean tryToSetCoins(Player player, String coinAmount) {
		try {
			long coinAttachment = Long.parseLong(coinAmount);
			if(PlayerConfigurator.getCharacterCoins(player) >= coinAttachment) {
				this.coinAttachment = coinAttachment;
				return true;
			}
			else {
				player.sendMessage(ChatColor.RED + "You don't have enough money!");
			}
		}
		catch (Exception e) {
			player.sendMessage(ChatColor.RED + "Invalid coin amount specified!");
		}
		return false;
	}
	
	/**
	 * Tries to set the item attachment of the mail.
	 * 
	 * @param player The player who is sending the mail and holds the item.
	 * 
	 * @return If it was successful.
	 */
	public boolean tryToSetItemStack(Player player) {
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(ItemUtils.isNotAir(itemStack) && !MenuUtils.checkForStaticItem(itemStack)) {
			itemAttachment = itemStack;
			return true;
		}
		else {
			player.sendMessage(ChatColor.RED + "Invalid item attachment selected (in hand)!");
			return false;
		}
	}
	
	/**
	 * Joins the given text parts and sets them as text content of the mail.
	 * 
	 * @param textParts The text part array.
	 * @param startingIndex The array index of the first text part to use.
	 */
	public void joinAndSetTextContent(String[] textParts, int startingIndex) {
		List<String> splitTextContent = new ArrayList<>();
		for(int index = startingIndex; index < textParts.length; index++) {
			splitTextContent.add(textParts[index]);
		}
		textContent = StringUtils.join(splitTextContent, " ");
	}
	
	/**
	 * Sends the configured mail to its receiver.
	 * Also takes away the attachment from your inventory and updates the daily mail limit.
	 * 
	 * @param player The player who is sending the mail.
	 * 
	 * @see PlayerMailConfigurator
	 * @see PlayerConfigurator#setTokenLimitDate(Player, String, long)
	 * @see PlayerConfigurator#setTokenLimitAmount(Player, String, int)
	 */
	public void send(Player player) {
		long timestamp = System.currentTimeMillis();
		String mailName = timestamp + "_" + player.getName();
		
		PlayerMailConfigurator.setMailSender(receiver, mailName, player.getName());
		PlayerMailConfigurator.setMailTime(receiver, mailName, timestamp);
		PlayerMailConfigurator.setMailText(receiver, mailName, textContent);
		if(coinAttachment > 0) {
			PlayerMailConfigurator.setMailCoins(receiver, mailName, coinAttachment);
			PlayerConfigurator.setCharacterCoins(player, PlayerConfigurator.getCharacterCoins(player) - coinAttachment);
		}
		if(itemAttachment != null) {
			PlayerMailConfigurator.setMailItem(receiver, mailName, itemAttachment);
			itemAttachment.setAmount(0);
		}
		PlayerConfigurator.setTokenLimitDate(player, MAIL_TOKEN_KEY, WauzDateUtils.getDateLong());
		PlayerConfigurator.setTokenLimitAmount(player, MAIL_TOKEN_KEY, getMailsSentToday(player) + 1);
		player.sendMessage(ChatColor.GREEN + "Your mail was successfully sent to " + receiver.getName() + "!");
		if(receiver.getPlayer() != null) {
			String newMailMessage = ChatColor.YELLOW + "You received a mail! To read it:";
			WauzNmsClient.nmsChatCommand(receiver.getPlayer(), "menu mails", newMailMessage, false);
		}
	}

}
