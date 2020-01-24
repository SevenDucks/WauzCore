package eu.wauz.wauzcore.system.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.AchievementConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.Formatters;
import net.md_5.bungee.api.ChatColor;

/**
 * An achievement, generated from the achievement config file.
 * 
 * @author Wauzmons
 * 
 * @see AchievementTracker
 */
public class WauzAchievement {
	
	/**
	 * A map with lists of achievements, indexed by type.
	 */
	private static Map<WauzAchievementType, List<WauzAchievement>> achievementMap = new HashMap<>();
	
	/**
	 * Gets the number of all earnable achievements.
	 * 
	 * @return The achievement count.
	 */
	public static int getAchievementCount() {
		int count = 0;
		for(List<WauzAchievement> achievementList : achievementMap.values()) {
			count += achievementList.size();
		}
		return count;
	}
	
	/**
	 * Initializes all achievements and fills the internal achievement map.
	 * 
	 * @see AchievementConfigurator#getAchievementKeys()
	 */
	public static void init() {
		for(WauzAchievementType achievementType : WauzAchievementType.values()) {
			achievementMap.put(achievementType, new ArrayList<>());
		}
		
		for(String key : AchievementConfigurator.getAchievementKeys()) {
			WauzAchievement achievement = new WauzAchievement(key);
			achievement.setName(AchievementConfigurator.getName(key));
			achievement.setType(AchievementConfigurator.getType(key));
			achievement.setGoal(AchievementConfigurator.getGoal(key));
			achievement.setReward(AchievementConfigurator.getReward(key));
			achievementMap.get(achievement.getType()).add(achievement);
		}
	}
	
	/**
	 * Gets the highest achievement of this type, that the given player earned.
	 * 
	 * @param player The player that earned the achievement.
	 * @param type The type of the achievement.
	 * 
	 * @return The highest earned achievement.
	 */
	public static WauzAchievement getCurrentAchievementStage(Player player, WauzAchievementType type) {
		WauzAchievement currentAchievementStage = null;
		double progress = PlayerConfigurator.getCharacterAchievementProgress(player, type);
		
		for(WauzAchievement achievement : achievementMap.get(type)) {
			if(progress >= achievement.getGoal()) {
				currentAchievementStage = achievement;
			}
			else {
				break;
			}
		}
		return currentAchievementStage;
	}
	
	/**
	 * Gets the next achievement of this type, that the player has not earned yet.
	 * 
	 * @param player The player that has to earn the achievement.
	 * @param type The type of the achievement.
	 * 
	 * @return The next achievement to earn.
	 */
	public static WauzAchievement getNextAchievementStage(Player player, WauzAchievementType type) {
		WauzAchievement currentAchievementStage = getCurrentAchievementStage(player, type);
		List<WauzAchievement> achievements = achievementMap.get(type);
		int nextIndex = currentAchievementStage != null ? achievements.indexOf(currentAchievementStage) + 1 : 0;
		return nextIndex < achievements.size() ? achievements.get(nextIndex) : null;
	}
	
	/**
	 * The key of the achievement.
	 */
	private String key;
	
	/**
	 * The name of the achievement.
	 */
	private String name;
	
	/**
	 * The type of the achievement.
	 */
	private WauzAchievementType type;
	
	/**
	 * The required value to complete the achievement.
	 */
	private int goal;
	
	/**
	 * The amount of soulstones to receive as an achievement reward.
	 */
	private int reward;
	
	/**
	 * Constructor for a new achievement.
	 * 
	 * @param key The key of the achievement.
	 */
	private WauzAchievement(String key) {
		this.key = key;
	}
	
	/**
	 * Grants this achievement to the player and increases ther achievement count.
	 * 
	 * @param player The player that should earn the achievement.
	 */
	public void award(Player player) {
		PlayerConfigurator.addCharacterCompletedAchievements(player);
		long soulstones = PlayerConfigurator.getCharacterCurrency(player, "reput.souls");
		PlayerConfigurator.setCharacterCurrency(player, "reput.souls", soulstones + reward);
		
		player.sendTitle(ChatColor.GOLD + "Achievement Earned", name, 10, 70, 20);
		player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		String goalDisplay = ChatColor.YELLOW + " (" + Formatters.INT.format(goal) + " " + type.getMessage() + ")";
		player.sendMessage(ChatColor.YELLOW + "You earned " + ChatColor.GOLD + name + goalDisplay);
		player.sendMessage(ChatColor.YELLOW + "You received " + reward + " soulstones as reward!");
		WauzNmsClient.nmsChatCommand(player, "menu achievements", ChatColor.YELLOW + "To view your achievements:", false);
		player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
	}

	/**
	 * @return The key of the achievement.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key The new key of the achievement.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return The name of the achievement.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The new name of the achievement.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The type of the achievement.
	 */
	public WauzAchievementType getType() {
		return type;
	}

	/**
	 * @param type The new type of the achievement.
	 */
	public void setType(WauzAchievementType type) {
		this.type = type;
	}

	/**
	 * @return The required value to complete the achievement.
	 */
	public int getGoal() {
		return goal;
	}

	/**
	 * @param goal The new required value to complete the achievement.
	 */
	public void setGoal(int goal) {
		this.goal = goal;
	}

	/**
	 * @return The amount of soulstones to receive as an achievement reward.
	 */
	public int getReward() {
		return reward;
	}

	/**
	 * @param reward The new amount of soulstones to receive as an achievement reward.
	 */
	public void setReward(int reward) {
		this.reward = reward;
	}

}
