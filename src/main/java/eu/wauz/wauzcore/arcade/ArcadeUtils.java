package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

/**
 * An util class for the arcade gamemode.
 * 
 * @author Wauzmons
 */
public class ArcadeUtils {
	
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
	 * Lets a group of players equip a chestplate in the color of their team.
	 * 
	 * @param players The group of players.
	 * @param color Their team color.
	 */
	public static void equipTeamColor(List<Player> players, Color color) {
		ItemStack chestplateItemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) chestplateItemStack.getItemMeta();
		leatherArmorMeta.setColor(color);
		leatherArmorMeta.setUnbreakable(true);
		leatherArmorMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		leatherArmorMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		chestplateItemStack.setItemMeta(leatherArmorMeta);
		
		for(Player player : players) {
			player.getEquipment().setChestplate(chestplateItemStack);
		}
	}
	
	/**
	 * Places a group of players to the new spawn point for their team.
	 * They will be frozen, until the game starts.
	 * 
	 * @param players The group of players.
	 * @param location Their team spawn.
	 */
	public static void placeTeam(List<Player> players, Location location) {
		for(Player player : players) {
			SkillUtils.addPotionEffect(player, PotionEffectType.SLOW, 500, 200);
			SkillUtils.addPotionEffect(player, PotionEffectType.JUMP, 500, 200);
			player.getInventory().clear();
			player.setBedSpawnLocation(location);
			player.teleport(location);
		}
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
				if(secondsTillStart > 0) {
					for(Player player : ArcadeLobby.getPlayingPlayers()) {
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
						player.sendTitle("", ChatColor.YELLOW + "" + secondsTillStart, 2, 14, 4);
					}
					runStartTimer(secondsTillStart - 1, secondsTillEnd);
				}
				else {
					for(Player player : ArcadeLobby.getPlayingPlayers()) {
						player.removePotionEffect(PotionEffectType.SLOW);
						player.removePotionEffect(PotionEffectType.JUMP);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
						player.sendTitle("", ChatColor.GOLD + "START", 2, 14, 4);
						runEndTimer(secondsTillEnd);
					}
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
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(secondsTillEnd <= 0 || ArcadeLobby.getPlayingCount() <= 1) {
					ArcadeLobby.endGame();
				}
				else {
					runEndTimer(secondsTillEnd - 1);
				}
			}
			
		}, 20);
	}

}
