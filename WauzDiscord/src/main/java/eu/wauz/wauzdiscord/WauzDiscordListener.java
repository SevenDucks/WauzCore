package eu.wauz.wauzdiscord;

import java.awt.Color;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.wauz.wauzdiscord.data.DiscordConfigurator;

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
	@EventHandler(priority = EventPriority.LOW)
	public void onLogin(PlayerLoginEvent event) throws Exception {
		if(!event.getResult().equals(Result.ALLOWED)) {
            return;
        }
		if(DiscordConfigurator.showJoinLeaveNotification()) {
			Player player = event.getPlayer();
			WauzDiscord.getShiroDiscordBot().sendEmbedFromMinecraft(player, "[+] " + player.getName()
					+ " joined the game!", Color.CYAN, false);
		}
	}

	/**
	 * Sends a leave message to Discord, if enabled.
	 * 
	 * @param event The event for creating the message.
	 * 
	 * @see DiscordConfigurator#showJoinLeaveNotification()
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onLogout(PlayerQuitEvent event) {
		if(DiscordConfigurator.showJoinLeaveNotification()) {
			Player player = event.getPlayer();
			WauzDiscord.getShiroDiscordBot().sendEmbedFromMinecraft(player, "[-] " + player.getName()
					+ " left the game!", Color.ORANGE, false);
		}
	}
	
	/**
	 * Sends a death message to Discord, if enabled.
	 * 
	 * @param event The event for creating the message.
	 * 
	 * @see DiscordConfigurator#showDeathNotification()
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onDeath(PlayerDeathEvent event) {
		if(DiscordConfigurator.showDeathNotification()) {
			Player player = event.getEntity();
			WauzDiscord.getShiroDiscordBot().sendEmbedFromMinecraft(player, "[x] " + event.getDeathMessage()
					+ "!", Color.BLACK, false);
		}
	}
	
}
