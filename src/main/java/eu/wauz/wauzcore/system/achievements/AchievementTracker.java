package eu.wauz.wauzcore.system.achievements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * A class to track the progress of generic player achievements.
 * 
 * @author Wauzmons
 */
public class AchievementTracker {
	
	/**
	 * Checks if the player just completed a non generic achievement and awards it to them.
	 * 
	 * @param player The player who probably just completed an achievement.
	 * @param type The non generic achievement type.
	 * @param goal The goal to check for.
	 */
	public static void checkForAchievement(Player player, WauzAchievementType type, String goal) {
		WauzAchievement achievement = WauzAchievement.getAchievementForGoal(type, goal);
		if(achievement == null) {
			return;
		}
		
		String key = achievement.getKey();
		List<String> achievements = PlayerCollectionConfigurator.getCharacterAchievementList(player, type);
		if(!achievements.contains(key)) {
			achievements.add(key);
			PlayerCollectionConfigurator.setCharacterAchievementList(player, type, achievements);
			achievement.award(player);
		}
	}
	
	/**
	 * Adds progress to an achievement type and grants the fitting achievement when completed.
	 * 
	 * @param player The player that owns the config file.
	 * @param type The type of the generic achievement.
	 * @param increment How much to add to the progress.
	 */
	public static void addProgress(Player player, WauzAchievementType type, double increment) {
		WauzAchievement oldAchievementStage = WauzAchievement.getCurrentAchievementStage(player, type);
		double oldProgress = PlayerCollectionConfigurator.getCharacterAchievementProgress(player, type);
		double newProgress = oldProgress + increment;
		PlayerCollectionConfigurator.setCharacterAchievementProgress(player, type, newProgress);
		WauzAchievement newAchievementStage = WauzAchievement.getCurrentAchievementStage(player, type);
		
		if(!Objects.equals(oldAchievementStage, newAchievementStage)) {
			newAchievementStage.award(player);
		}
	}
	
	/**
	 * Generates a list of lores to display the achievement progress for a given player and type.
	 * 
	 * @param player The player that owns the config file.
	 * @param type The type of the generic achievement.
	 * 
	 * @return The list of progress lores.
	 */
	public static List<String> generateProgressLores(Player player, WauzAchievementType type) {
		WauzAchievement currentAchievementStage = WauzAchievement.getCurrentAchievementStage(player, type);
		WauzAchievement nextAchievementStage = WauzAchievement.getNextAchievementStage(player, type);
		double progress = PlayerCollectionConfigurator.getCharacterAchievementProgress(player, type);
		String progressString = Formatters.INT.format(Math.floor(progress));
		
		List<String> lores = new ArrayList<>();
		if(currentAchievementStage != null) {
			lores.add(ChatColor.GREEN + "Awarded: " + ChatColor.GOLD + currentAchievementStage.getName());
			int goal = currentAchievementStage.getGoal();
			String goalString = Formatters.INT.format(goal);
			lores.add(ChatColor.YELLOW + goalString + " " + type.getMessage());
		}
		else {
			lores.add(ChatColor.GREEN + "Awarded: " + ChatColor.RED + "None");
		}
		
		lores.add("");
		if(nextAchievementStage != null) {
			lores.add(ChatColor.YELLOW + "Next: " + ChatColor.GOLD + nextAchievementStage.getName());
			int goal = nextAchievementStage.getGoal();
			String goalString = Formatters.INT.format(goal);
			lores.add(ChatColor.YELLOW + "Progress: " + progressString + " / " + goalString + " " + type.getMessage());
			lores.add(UnicodeUtils.createProgressBar(progress, goal, 50, ChatColor.DARK_GREEN));
			lores.add(ChatColor.YELLOW + "Reward: " + nextAchievementStage.getReward() + " Soulstones");
		}
		else {
			lores.add(ChatColor.YELLOW + "Next: " + ChatColor.GREEN + "COMPLETED");
			lores.add(ChatColor.YELLOW + "Progress: " + progressString + " " + type.getMessage());
		}
		return lores;
	}

}
