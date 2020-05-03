package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerMailConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;

/**
 * A mail with optional attachment, that can be send to a player.
 * 
 * @author Wauzmons
 */
public class WauzPlayerMail {
	
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
		if(ItemUtils.isNotAir(itemStack)) {
			itemAttachment = itemStack;
			return true;
		}
		else {
			player.sendMessage(ChatColor.RED + "No item attachment selected (in hand)!");
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
	 * 
	 * @param player The player who is sending the mail.
	 */
	public void send(Player player) {
		long timestamp = System.currentTimeMillis();
		String mailName = timestamp + "_" + UUID.randomUUID();
		
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
		player.sendMessage(ChatColor.GREEN + "Your mail was successfully sent to " + receiver.getName() + "!");
	}

}
