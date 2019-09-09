package eu.wauz.wauzdiscord;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class WauzDiscordListener implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent event) throws Exception {
		Player player = event.getPlayer();
		WauzDiscord.getShiroDiscordBot().sendMessageFromMinecraft("[+] " + player.getName() + " joined the game!");
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		WauzDiscord.getShiroDiscordBot().sendMessageFromMinecraft("[-] " + player.getName() + " left the game!");
	}
	
}
