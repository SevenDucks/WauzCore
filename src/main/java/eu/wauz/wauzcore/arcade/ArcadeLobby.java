package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.system.WauzNoteBlockPlayer;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

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
	 * A map of all minigames that can be played, indexed by name.
	 */
	private static Map<String, ArcadeMinigame> minigameMap = new HashMap<>();
	
	/**
	 * All minigames that can randomly come up in the next round.
	 */
	private static List<ArcadeMinigame> queuedMinigames = new ArrayList<>();
	
	/**
	 * The currently played minigame.
	 */
	private static ArcadeMinigame minigame;
	
	/**
	 * The remaining time of the current game as readable string.
	 */
	private static String remainingTime = "0:00";
	
	/**
	 * Registers the given minigame.
	 * 
	 * @param minigame The minigame to register.
	 */
	public static void registerMinigame(ArcadeMinigame minigame) {
		minigameMap.put(minigame.getName(), minigame);
	}
	
	/**
	 * Gets a list of all minigame names.
	 * 
	 * @return The list of all minigame names.
	 */
	public static List<String> getAllMinigameNames() {
		return new ArrayList<>(minigameMap.keySet());
	}
	
	/**
	 * Starts a specific new game.
	 * 
	 * @param minigameName The name of the minigame to start.
	 * 
	 * @return If the game was valid.
	 * 
	 * @see ArcadeMinigame#startGame(List)
	 */
	public static boolean startGame(String minigameName) {
		if(!minigameMap.containsKey(minigameName)) {
			return false;
		}
		minigame = minigameMap.get(minigameName);
		queuedMinigames.remove(minigame);
		playingPlayers.addAll(waitingPlayers);
		waitingPlayers.clear();
		minigame.startGame(playingPlayers);
		return true;
	}
 	
	/**
	 * Starts a random new game.
	 * 
	 * @see ArcadeMinigame#startGame(List)
	 */
	public static void startGame() {
		if(queuedMinigames.isEmpty()) {
			queuedMinigames.addAll(minigameMap.values());
		}
		minigame = queuedMinigames.get(Chance.randomInt(queuedMinigames.size()));
		queuedMinigames.remove(minigame);
		playingPlayers.addAll(waitingPlayers);
		waitingPlayers.clear();
		minigame.startGame(playingPlayers);
	}
	
	/**
	 * Ends the current game and puts the players back into queue.
	 * 
	 * @see ArcadeMinigame#endGame()
	 */
	public static void endGame() {
		List<Player> winners = minigame.endGame();
		minigame = null;
		List<Player> players = new ArrayList<>(playingPlayers);
		playingPlayers.clear();
		
		for(Player player : players) {
			WauzNoteBlockPlayer.play(player, "None");
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.JUMP);
			addPlayerToQueue(player);
			if(winners.contains(player)) {
				player.sendTitle(ChatColor.DARK_GREEN + "Qualified", "You WON the minigame round!", 10, 70, 20);
				WauzRewards.earnArcadeToken(player);
			}
			else {
				player.sendTitle(ChatColor.DARK_PURPLE + "Eliminated", "You LOST the minigame round!", 10, 70, 20);
			}
		}
		ArcadeUtils.runNextTimer(30);
	}
	
	/**
	 * Adds a player to the arcade lobby.
	 * 
	 * @param player The player to add.
	 */
	public static void addPlayerToQueue(Player player) {
		Location destination = new Location(getWorld(), 0.5, 91, 0.5);
		player.getInventory().clear();
		player.setBedSpawnLocation(destination, true);
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
	 * @return All players waiting for a game to start.
	 */
	public static List<Player> getWaitingPlayers() {
		return new ArrayList<>(waitingPlayers);
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
	 * Handles the start event, that gets fired when the start countdown ends.
	 */
	public static void handleStartEvent() {
		if(minigame != null) {
			minigame.handleStartEvent();
		}
	}
	
	/**
	 * Handles the given death event, that occured in the minigame.
	 * 
	 * @param event The death event.
	 */
	public static void handleDeathEvent(PlayerDeathEvent event) {
		if(minigame != null) {
			minigame.handleDeathEvent(event);
		}
	}
	
	/**
	 * Handles the given damage event, that occured in the minigame.
	 * 
	 * @param event The damage event.
	 */
	public static void handleDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player && !event.getCause().equals(DamageCause.VOID)) {
			event.setCancelled(true);
		}
		if(minigame != null) {
			minigame.handleDamageEvent(event);
		}
	}
	
	/**
	 * Handles the given projectile hit event, that occured in the minigame.
	 * 
	 * @param event The projectile hit event.
	 */
	public static void handleProjectileHitEvent(ProjectileHitEvent event) {
		if(minigame != null) {
			minigame.handleProjectileHitEvent(event);
		}
	}
	
	/**
	 * Handles the given interact event, that occured in the minigame.
	 * 
	 * @param event The interact event.
	 */
	public static void handleInteractEvent(PlayerInteractEvent event) {
		if(minigame != null) {
			minigame.handleInteractEvent(event);
		}
	}
	
	/**
	 * Handles the given animation event, that occured in the minigame.
	 * 
	 * @param event The animation event.
	 */
	public static void handleAnimationEvent(PlayerAnimationEvent event) {
		if(minigame != null) {
			minigame.handleAnimationEvent(event);
		}
	}
	
	/**
	 * Handles the given move event, that occured in the minigame.
	 * 
	 * @param event The move event.
	 */
	public static void handleMoveEvent(PlayerMoveEvent event) {
		PotionEffect effect = event.getPlayer().getPotionEffect(PotionEffectType.SLOW);
		if(effect != null && effect.getAmplifier() >= 100) {
			Location from = event.getFrom();
			Location to = event.getTo();
			if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
				event.setCancelled(true);
			}
			return;
		}
		Player player = event.getPlayer();
		if(waitingPlayers.contains(player) && player.getLocation().getY() <= 72) {
			player.teleport(player.getBedSpawnLocation());
		}
		else if(minigame != null) {
			minigame.handleMoveEvent(event);
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
