package eu.wauz.wauzcore.system.listeners;

import java.awt.Color;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.DiscordConfigurator;
import eu.wauz.wauzcore.events.ShareItemEvent;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * This class listens to Bukkit events, to send messages to Discord.
 * 
 * @author Wauzmons
 */
public class WauzDiscordListener implements Listener {

	/**
	 * Sends a join message to Discord, if enabled.
	 * 
	 * @param event The event for creating the message.
	 * 
	 * @see DiscordConfigurator#showJoinLeaveNotification()
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerLoginEvent event) throws Exception {
		if(!event.getResult().equals(Result.ALLOWED)) {
            return;
        }
		if(DiscordConfigurator.showJoinLeaveNotification()) {
			Player player = event.getPlayer();
			WauzCore.getShiroDiscordBot().sendEmbedFromMinecraft(player, "[Join] " + player.getName()
					+ " joined the game!", null, Color.CYAN, false);
		}
	}

	/**
	 * Sends a leave message to Discord, if enabled.
	 * 
	 * @param event The event for creating the message.
	 * 
	 * @see DiscordConfigurator#showJoinLeaveNotification()
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogout(PlayerQuitEvent event) {
		if(DiscordConfigurator.showJoinLeaveNotification()) {
			Player player = event.getPlayer();
			WauzCore.getShiroDiscordBot().sendEmbedFromMinecraft(player, "[Leave] " + player.getName()
					+ " left the game!", null, Color.ORANGE, false);
		}
	}
	
	/**
	 * Sends a death message to Discord, if enabled.
	 * 
	 * @param event The event for creating the message.
	 * 
	 * @see DiscordConfigurator#showDeathNotification()
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDeath(PlayerDeathEvent event) {
		if(DiscordConfigurator.showDeathNotification()) {
			Player player = event.getEntity();
			WauzCore.getShiroDiscordBot().sendEmbedFromMinecraft(player, "[Death] " + event.getDeathMessage()
					+ "!", null, Color.BLACK, false);
		}
	}
	
	/**
	 * Sends an item message to Discord.
	 * 
	 * @param event The event for creating the message.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onItem(ShareItemEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = event.getItemStack();
		String description = "**" + ChatColor.stripColor(ItemUtils.getDisplayName(itemStack)) + "**";
		if(ItemUtils.hasLore(itemStack)) {
			for(String lore : Components.lore(itemStack.getItemMeta())) {
				String descriptionLine = StringUtils.substringBeforeLast(lore, ChatColor.GRAY + UnicodeUtils.ICON_DIAMOND);
				description += System.lineSeparator() + ChatColor.stripColor(descriptionLine);
			}
		}
		WauzCore.getShiroDiscordBot().sendEmbedFromMinecraft(player, "[Item] " + player.getName()
				+ " shared an item in chat!", description, null, false);
	}
	
}
