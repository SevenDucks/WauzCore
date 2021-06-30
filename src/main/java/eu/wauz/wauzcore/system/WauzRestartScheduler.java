package eu.wauz.wauzcore.system;

import java.time.Duration;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;

/**
 * The restart scheduler schedules restarts. (Useful Documentation)
 * 
 * @author Wauzmons
 */
public class WauzRestartScheduler {
	
	/**
	 * Sets up a reapeating task, to restart the server at midnight, system time.
	 * 15 minutes before the daily restart a countdown of minutes is sent to the chat.
	 * 
	 * @param core The plugin core.
	 */
	public static void schedule(WauzCore core) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime dateTime = now.withHour(23).withMinute(45).withSecond(0);
		dateTime = dateTime.isBefore(now) ? dateTime.plusDays(1) : dateTime;
		long initialDelay = Duration.between(now, dateTime).getSeconds();
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(core, new Runnable() {
			
			int minutesTillRestart = 15;
			
			@Override
			public void run() {
				if(minutesTillRestart > 0) {
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.sendMessage(ChatColor.DARK_RED + "The server will restart in " + ChatColor.BOLD + minutesTillRestart + " minute/s!");
					}
				}
				else {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart Daily Server Restart");
				}
				minutesTillRestart--;
			}
			
		}, initialDelay * 20L, 1200L);
	}

}
