package eu.wauz.wauzdiscord;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.wauz.wauzdiscord.data.DiscordConfigurator;

/**
 * This class listens to Bukkit events, to send join/leave messages to Discord.
 * 
 * @author Wauzmons
 */
public class WauzDiscordListener implements Listener {

	/**
	 * Sends a join message to Discord, if enabled.
	 * 
	 * @param event
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
			WauzDiscord.getShiroDiscordBot().sendMessageFromMinecraft("[+] " + player.getName() + " joined the game!");
		}
	}

	/**
	 * Sends a leave message to Discord, if enabled.
	 * 
	 * @param event
	 * 
	 * @see DiscordConfigurator#showJoinLeaveNotification()
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onLogout(PlayerQuitEvent event) {
		if(DiscordConfigurator.showJoinLeaveNotification()) {
			Player player = event.getPlayer();
			WauzDiscord.getShiroDiscordBot().sendMessageFromMinecraft("[-] " + player.getName() + " left the game!");
		}
	}
	
}
