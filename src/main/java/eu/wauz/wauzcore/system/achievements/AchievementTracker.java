package eu.wauz.wauzcore.system.achievements;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;

/**
 * A class to track the progress of generic player achievements.
 * 
 * @author Wauzmons
 */
public class AchievementTracker {
	
	/**
	 * Adds progress to an achievement type and grants the fitting achievement when completed.
	 * 
	 * @param player The player that owns the config file.
	 * @param type The type of the generic achievement.
	 * @param increment How much to add to the progress.
	 */
	public static void addProgress(Player player, WauzAchievementType type, double increment) {
		double oldProgress = PlayerConfigurator.getCharacterAchievementProgress(player, type);
		double newProgress = oldProgress + increment;
		PlayerConfigurator.setCharacterAchievementProgress(player, type, newProgress);
	}

}
