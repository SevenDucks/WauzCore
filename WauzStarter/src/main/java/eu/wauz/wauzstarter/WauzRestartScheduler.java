package eu.wauz.wauzstarter;

import java.time.Duration;
import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

/**
 * The Restart Scheduler schedules Restarts. (Useful Documentation)
 * 
 * @author Wauzmons
 */
public class WauzRestartScheduler {
	
	/**
	 * Sets up a Reapeating Task, to restart the Server at Midnight, System Time.
	 * 15 Minutes before the daily Restart a countdown of Minutes is sent to the Chat.
	 */
	public static void init() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime dateTime = now.withHour(23).withMinute(45).withSecond(0);
		dateTime = dateTime.isBefore(now) ? dateTime.plusDays(1) : dateTime;
		long initialDelay = Duration.between(now, dateTime).getSeconds();
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(WauzStarter.getInstance(), new Runnable() {
			
			int minutesTillRestart = 15;
			
			@Override
			public void run() {
				if(minutesTillRestart > 0) {
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.sendMessage(ChatColor.DARK_RED + "The Server will restart in " + ChatColor.BOLD + minutesTillRestart + " Minutes!");
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
