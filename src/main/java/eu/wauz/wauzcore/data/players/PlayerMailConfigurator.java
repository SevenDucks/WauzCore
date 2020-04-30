package eu.wauz.wauzcore.data.players;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.api.PlayerMetaConfigurationUtils;

/**
 * Configurator to fetch or modify data from the PlayerMail.yml files.
 * 
 * @author Wauzmons
 */
public class PlayerMailConfigurator extends PlayerMetaConfigurationUtils {

// General Parameters
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * 
	 * @return The sender of the mail.
	 */
	public static String getMailSender(OfflinePlayer player, String mail) {
		return playerMailConfigGetString(player, mail, "sender");
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param sender The new sender of the mail.
	 */
	public static void setMailSender(OfflinePlayer player, String mail, String sender) {
		playerMailConfigSet(player, mail, "sender", sender);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * 
	 * @return The text content of the mail.
	 */
	public static String getMailText(OfflinePlayer player, String mail) {
		return playerMailConfigGetString(player, mail, "text");
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param text The new text content of the mail.
	 */
	public static void setMailText(OfflinePlayer player, String mail, String text) {
		playerMailConfigSet(player, mail, "text", text);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * 
	 * @return The sending time of the mail.
	 */
	public static Long getMailTime(OfflinePlayer player, String mail) {
		return playerMailConfigGetLong(player, mail, "time");
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param time The new sending time of the mail.
	 */
	public static void setMailTime(OfflinePlayer player, String mail, Long time) {
		playerMailConfigSet(player, mail, "time", time);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * 
	 * @return The coin attachment of the mail.
	 */
	public static Long getMailCoins(OfflinePlayer player, String mail) {
		return playerMailConfigGetLong(player, mail, "coins");
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param coins The new coin attachment of the mail.
	 */
	public static void setMailCoins(OfflinePlayer player, String mail, Long coins) {
		playerMailConfigSet(player, mail, "coins", coins);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * 
	 * @return The item attachment of the mail.
	 */
	public static ItemStack getMailItem(OfflinePlayer player, String mail) {
		return playerMailConfigGetItemStack(player, mail, "sender");
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param mail The name of the mail.
	 * @param item The new item attachment of the mail.
	 */
	public static void setMailItem(OfflinePlayer player, String mail, ItemStack item) {
		playerMailConfigSet(player, mail, "sender", item);
	}
	
}
