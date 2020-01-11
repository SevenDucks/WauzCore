package eu.wauz.wauzcore.system.achievements;

/**
 * The different types an achievement can have.
 * 
 * @author Wauzmons
 */
public enum WauzAchievementType {
	
	/**
	 * Achievements for killing x ememies.
	 */
	KILL_ENEMIES("kills", "Enemies Killed"),
	
	/**
	 * Achievements for identifying x items.
	 */
	IDENTIFY_ITEMS("identifies", "Items Identified"),
	
	/**
	 * Achievements for using x mana.
	 */
	USE_MANA("mana", "Mana Used"),
	
	/**
	 * Achievements for completing x quests.
	 */
	COMPLETE_QUESTS("quests", "Quests Completed"),
	
	/**
	 * Achievements for crafting x items.
	 */
	CRAFT_ITEMS("crafts", "Items Crafted"),
	
	/**
	 * Achievements for collecting x pets.
	 */
	COLLECT_PETS("pets", "Pets Collected"),
	
	/**
	 * Achievements for earning x coins.
	 */
	EARN_COINS("coins", "Coins Earned"),
	
	/**
	 * Achievements for playing x hours.
	 */
	PLAY_HOURS("playtime", "Hours Played"),
	
	/**
	 * Achievements for gaining x levels.
	 */
	GAIN_LEVELS("levels", "Levels Gained");
	
	/**
	 * The key for this achievement type, used in player data configs.
	 */
	private String key;
	
	/**
	 * The message to show behind counters, i.e. 10/10 "Enemies Killed".
	 */
	private String message;
	
	/**
	 * Creates an achievement type with the given key.
	 * 
	 * @param key The key for this achievement type, used in player data configs.
	 * @param message The message to show behind counters, i.e. 10/10 "Enemies Killed".
	 */
	private WauzAchievementType(String key, String message) {
		this.key = key;
		this.message = message;
	}
	
	/**
	 * @return The key for this achievement type, used in player data configs.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return The message to show behind counters, i.e. 10/10 "Enemies Killed".
	 */
	public String getMessage() {
		return message;
	}

}
