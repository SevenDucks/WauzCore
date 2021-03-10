package eu.wauz.wauzcore.players.ui.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An UI class to show world based information inside a sidebar to a player.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerTablist
 */
public class WauzPlayerScoreboard {
	
	/**
	 * Schedules a task to update the sidebar of the player, based on the world they are in.
	 * The scoreboard will be refeshed in 0.5 seconds from then.
	 * 
	 * @param player The player who should receive the scoreboard.
	 * 
	 * @see HubScoreboard
	 * @see DungeonScoreboard
	 * @see QuestScoreboard
	 * @see SurvivalScoreboard
	 * @see ArcadeScoreboard
	 * @see WauzPlayerTablist#createAndShow()
	 */
	public static void scheduleScoreboardRefresh(final Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
            public void run() {
            	if(player == null || !player.isValid() || WauzPlayerDataPool.getPlayer(player) == null) {
            		return;
            	}
            	String worldName = player.getWorld().getName();
            	Scoreboard scoreboard = null;
            	
            	if(WauzMode.isMMORPG(player)) {
            		if(worldName.startsWith("Hub")) {
            			scoreboard = new HubScoreboard(player).getScoreboard();
            		}
            		else if(worldName.startsWith("WzInstance")) {
            			scoreboard = new DungeonScoreboard(player).getScoreboard();
            		}
            		else {
            			scoreboard = new QuestScoreboard(player).getScoreboard();
            		}
            	}
            	else if(WauzMode.isSurvival(player)) {
            		scoreboard = (WauzMode.inOneBlock(player) ? new OneBlockScoreboard(player) : new SurvivalScoreboard(player)).getScoreboard();
            	}
            	else if(WauzMode.isArcade(player)) {
            		scoreboard = new ArcadeScoreboard(player).getScoreboard();
            	}
            	else {
            		scoreboard = new HubScoreboard(player).getScoreboard();
            	}
            	new WauzPlayerTablist(player, scoreboard).createAndShow();
            }
            
		}, 10);
	}
	
}
