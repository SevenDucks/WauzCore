package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;

/**
 * Configurator to fetch or modify data from the Achievements.yml.
 * 
 * @author Wauzmons
 */
public class AchievementConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @return The keys of all achievements.
	 */
	public static List<String> getAchievementKeys() {
		return new ArrayList<>(mainConfigGetKeys("Achievements", "generic"));
	}
	
	/**
	 * @param achievementKey The key of the achievement.
	 * 
	 * @return The name of the achievement.
	 */
	public static String getName(String achievementKey) {
		return mainConfigGetString("Achievements", "generic." + achievementKey + ".name");
	}
	
	/**
	 * @param achievementKey The key of the achievement.
	 * 
	 * @return The type of the achievement.
	 */
	public static WauzAchievementType getType(String achievementKey) {
		return WauzAchievementType.valueOf(mainConfigGetString("Achievements", "generic." + achievementKey + ".type"));
	}
	
	/**
	 * @param achievementKey The key of the achievement.
	 * 
	 * @return The required value to complete the achievement.
	 */
	public static int getGoal(String achievementKey) {
		return mainConfigGetInt("Achievements", "generic." + achievementKey + ".goal");
	}
	
	/**
	 * @param achievementKey The key of the achievement.
	 * 
	 * @return The amount of soulstones to receive as an achievement reward.
	 */
	public static int getReward(String achievementKey) {
		return mainConfigGetInt("Achievements", "generic." + achievementKey + ".reward");
	}
	
}
