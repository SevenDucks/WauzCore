package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.system.WauzNoteBlockPlayer;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * An util class for the arcade gamemode.
 * 
 * @author Wauzmons
 */
public class ArcadeUtils {
	
	private static List<String> songs = Arrays.asList(
			"Rakanoth", "Linirea", "Olympus Mons", "Portowauza", "Wauzland",
			"Forest of Souls", "Maretas", "Ruined Island", "Wrayvell");
	
	/**
	 * Splits the given players into random teams.
	 * 
	 * @param players The players to split.
	 * @param teamCount The amount of teams.
	 * 
	 * @return The list of created teams.
	 */
	public static List<List<Player>> splitIntoTeams(List<Player> players, int teamCount) {
		List<List<Player>> teams = new ArrayList<>();
		for(int teamNumber = 1; teamNumber <= teamCount; teamNumber++) {
			teams.add(new ArrayList<>());
		}
		
		List<Player> shuffledPlayers = new ArrayList<>(players);
		Collections.shuffle(shuffledPlayers);
		int nextTeam = 0;
		for(Player player : shuffledPlayers) {
			teams.get(nextTeam).add(player);
			nextTeam = nextTeam + 1 == teamCount ? 0 : nextTeam + 1;
		}
		return teams;
	}
	
	/**
	 * Selects random players from the given list.
	 * 
	 * @param players The list of players.
	 * @param count The number of players to select.
	 * 
	 * @return The randomly selected players.
	 */
	public static List<Player> selectRandomPlayers(List<Player> players, int count) {
		List<Player> selectedPlayers = new ArrayList<>();
		List<Player> selectablePlayers = new ArrayList<>(players);
		while(selectedPlayers.size() < count) {
			Player player = selectablePlayers.get(Chance.randomInt(selectablePlayers.size()));
			selectablePlayers.remove(player);
			selectedPlayers.add(player);
		}
		return selectedPlayers;
	}
	
	/**
	 * Lets a group of players equip a chestplate in the color of their team.
	 * 
	 * @param players The group of players.
	 * @param color Their team color.
	 * @param name Their team name.
	 */
	public static void equipTeamColor(List<Player> players, Color color, String name) {
		for(Player player : players) {
			player.getEquipment().setChestplate(WauzEquipment.getCosmeticItem(Material.LEATHER_CHESTPLATE, color));
			player.getEquipment().setLeggings(WauzEquipment.getCosmeticItem(Material.LEATHER_LEGGINGS, color));
			player.getEquipment().setBoots(WauzEquipment.getCosmeticItem(Material.LEATHER_BOOTS, color));
			player.sendMessage(ChatColor.YELLOW + "You are in " + name + ChatColor.YELLOW + "!");
		}
	}
	
	/**
	 * Places a group of players to the new spawn point for their team.
	 * They will be frozen, until the game starts.
	 * 
	 * @param players The group of players.
	 * @param location Their team spawn.
	 * @param xOffset Possible x-offset of the spawn point.
	 * @param zOffset Possible z-offset of the spawn point.
	 */
	public static void placeTeam(List<Player> players, Location location, int xOffset, int zOffset) {
		for(Player player : players) {
			Location spawnLocation = location.clone();
			if(xOffset > 0 || zOffset > 0) {
				float spawnXOffset = Chance.negativePositive(xOffset);
				float spawnZOffset = Chance.negativePositive(zOffset);
				spawnLocation.add(spawnXOffset, 0, spawnZOffset);
			}
			SkillUtils.addPotionEffect(player, PotionEffectType.SLOW, 500, 200);
			SkillUtils.addPotionEffect(player, PotionEffectType.JUMP, 500, 200);
			player.setBedSpawnLocation(spawnLocation, true);
			player.teleport(spawnLocation);
		}
	}
	
	/**
	 * Runs a recursive timer for all players in the lobby.
	 * If the time is up, a new game will be selected.
	 * 
	 * @param secondsTillNext How many seconds to wait until the next game.
	 */
	public static void runNextTimer(int secondsTillNext) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(ArcadeLobby.getMinigame() != null) {
					return;
				}
				ArcadeLobby.updateRemainingTime(secondsTillNext);
				if(secondsTillNext > 0) {
					runNextTimer(secondsTillNext - 1);
				}
				else {
					if(ArcadeLobby.getWaitingCount() < 2) {
						for(Player player : ArcadeLobby.getWaitingPlayers()) {
							player.sendMessage(ChatColor.YELLOW + "There are not enough players to auto-start a game!");
						}
					}
					else {
						for(Player player : ArcadeLobby.getWaitingPlayers()) {
							player.sendMessage(ChatColor.YELLOW + "Auto-starting next game!");
						}
						ArcadeLobby.startGame();
					}
				}
			}
			
		}, 20);
	}
	
	/**
	 * Runs a recursive timer for all players in the minigame.
	 * If the time is up, they will be unfrozen and the end timer starts.
	 * 
	 * @param secondsTillStart How many seconds to wait until game start.
	 * @param secondsTillEnd How many seconds to wait until game end.
	 */
	public static void runStartTimer(int secondsTillStart, int secondsTillEnd) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(ArcadeLobby.getMinigame() == null) {
					return;
				}
				ArcadeLobby.updateRemainingTime(secondsTillStart + secondsTillEnd);
				if(secondsTillStart > 0) {
					for(Player player : ArcadeLobby.getPlayingPlayers()) {
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
						player.sendTitle(" ", ChatColor.YELLOW + "" + secondsTillStart, 2, 14, 4);
					}
					runStartTimer(secondsTillStart - 1, secondsTillEnd);
				}
				else {
					String songName = songs.get(Chance.randomInt(songs.size()));
					for(Player player : ArcadeLobby.getPlayingPlayers()) {
						player.removePotionEffect(PotionEffectType.SLOW);
						player.removePotionEffect(PotionEffectType.JUMP);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
						player.sendTitle(" ", ChatColor.GOLD + "START", 2, 14, 4);
						WauzNoteBlockPlayer.play(player, songName);
					}
					ArcadeLobby.handleStartEvent();
					runEndTimer(secondsTillEnd - 1);
				}
			}
			
		}, 20);
	}
	
	/**
	 * Runs a recursive timer for all players in the minigame.
	 * If the time is up, the game will end.
	 * 
	 * @param secondsTillEnd How many seconds to wait until game end.
	 */
	private static void runEndTimer(int secondsTillEnd) {
		ArcadeLobby.updateRemainingTime(secondsTillEnd);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(ArcadeLobby.getMinigame() == null) {
					return;
				}
				if(secondsTillEnd <= 0 || ArcadeLobby.getPlayingCount() <= 1) {
					ArcadeLobby.endGame();
				}
				else {
					ArcadeLobby.handleTick();
					runEndTimer(secondsTillEnd - 1);
				}
			}
			
		}, 20);
	}
	
}
