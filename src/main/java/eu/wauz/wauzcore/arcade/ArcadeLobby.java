package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.system.WauzNoteBlockPlayer;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import net.md_5.bungee.api.ChatColor;

/**
 * The static lobby for the arcade mode.
 * 
 * @author Wauzmons
 */
public class ArcadeLobby {
	
	/**
	 * All players waiting for a game to start.
	 */
	private static List<Player> waitingPlayers = new ArrayList<>();
	
	/**
	 * All players in the currently running game.
	 */
	private static List<Player> playingPlayers = new ArrayList<>();
	
	/**
	 * All minigames that can be played.
	 */
	private static List<ArcadeMinigame> minigames = new ArrayList<>();
	
	/**
	 * The currently played minigame.
	 */
	private static ArcadeMinigame minigame;
	
	/**
	 * The remaining time of the current game as readable string.
	 */
	private static String remainingTime = "0:00";
	
	/**
	 * A random instance, for rolling minigames.
	 */
	private static Random random = new Random();
	
	/**
	 * Initializes all minigames for the lobby.
	 */
	public static void init() {
		minigames.add(new MinigameJinxed());
	}
	
	/**
	 * Starts a new game, if requirements are met.
	 * 
	 * @param player The player who tries to start the game.
	 */
	public static void startGame(Player player) {
		if(minigame != null) {
			player.sendMessage(ChatColor.RED + "There already is a game in progress!");
			return;
		}
		if(getWaitingCount() < 2) {
			player.sendMessage(ChatColor.RED + "There are not enough players for a game!");
			return;
		}
		minigame = minigames.get(random.nextInt(minigames.size()));
		playingPlayers.addAll(waitingPlayers);
		waitingPlayers.clear();
		minigame.startGame(playingPlayers);
	}
	
	/**
	 * Ends the current game and puts the players back into queue.
	 */
	public static void endGame() {
		List<Player> winners = minigame.endGame();
		minigame = null;
		List<Player> players = new ArrayList<>(playingPlayers);
		playingPlayers.clear();
		
		for(Player player : players) {
			WauzNoteBlockPlayer.play(player, "None");
			addPlayerToQueue(player);
			if(winners.contains(player)) {
				player.sendTitle(ChatColor.DARK_GREEN + "Qualified", "You WON the minigame round!", 10, 70, 20);
				WauzRewards.earnArcadeToken(player);
			}
			else {
				player.sendTitle(ChatColor.DARK_PURPLE + "Eliminated", "You LOST the minigame round!", 10, 70, 20);
			}
		}
	}
	
	/**
	 * Adds a player to the arcade lobby.
	 * 
	 * @param player The player to add.
	 */
	public static void addPlayerToQueue(Player player) {
		Location destination = new Location(getWorld(), 0.5, 91, 0.5);
		player.getInventory().clear();
		player.setBedSpawnLocation(destination);
		player.teleport(destination);
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
		waitingPlayers.add(player);
	}
	
	/**
	 * Removes a player from the arcade lobby.
	 * 
	 * @param player The player to remove.
	 */
	public static void removePlayer(Player player) {
		if(playingPlayers.contains(player)) {
			if(minigame != null) {
				minigame.handleQuitEvent(player);
			}
			playingPlayers.remove(player);
		}
		waitingPlayers.remove(player);
	}
	
	/**
	 * Checks if a player is waiting for a game to start.
	 * 
	 * @param player The player to check.
	 * 
	 * @return If the player is waiting.
	 */
	public static boolean isWaiting(Player player) {
		return waitingPlayers.contains(player);
	}
	
	/**
	 * @return The count of waiting players.
	 */
	public static int getWaitingCount() {
		return waitingPlayers.size();
	}
	
	/**
	 * @return All players in the currently running game.
	 */
	public static List<Player> getPlayingPlayers() {
		return new ArrayList<>(playingPlayers);
	}
	
	/**
	 * @return The count of playing players.
	 */
	public static int getPlayingCount() {
		return playingPlayers.size();
	}
	
	/**
	 * @return The currently played minigame.
	 */
	public static ArcadeMinigame getMinigame() {
		return minigame;
	}
	
	/**
	 * @return The remaining time of the current game as readable string.
	 */
	public static String getRemainingTime() {
		return remainingTime;
	}
	
	/**
	 * @param seconds The new remaining time of the current game in seconds.
	 */
	public static void updateRemainingTime(int seconds) {
		remainingTime = WauzDateUtils.formatMinsSecs(seconds * 1000);
	}
	
	/**
	 * @return The arcade world.
	 */
	public static World getWorld() {
		return Bukkit.getWorld("WzInstance_Arcade");
	}
	
	/**
	 * Handles the given damage event, that occured in the minigame.
	 * 
	 * @param event The damage event.
	 */
	public static void handleDamageEvent(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		if(isWaiting(player) && event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
			event.setCancelled(true);
		}
		else if(minigame != null) {
			minigame.handleDamageEvent(event);
		}
	}
	
	/**
	 * A method that is called every second of the minigame.
	 */
	public static void handleTick() {
		if(minigame != null) {
			minigame.handleTick();
		}
	}

}
