package eu.wauz.wauzcore.system.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A listener to catch events, before players can join.
 * 
 * @author Wauzmons
 */
public class PreJoinListener implements Listener {
	
	/**
	 * Prevents players from joining before the core is started.
	 * 
	 * @param event The pre login event.
	 */
	@EventHandler
	public void onPreLogin(AsyncPlayerPreLoginEvent event) {
		if(!WauzCore.isStarted()) {
			event.setLoginResult(Result.KICK_OTHER);
			Components.kickMessage(event, "Server unavailable: Try again in a minute!");
		}
	}

}
