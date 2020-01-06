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
	KILL_ENEMIES("kills"),
	
	/**
	 * Achievements for identifying x items.
	 */
	IDENTIFY_ITEMS("identifies"),
	
	/**
	 * Achievements for using x mana.
	 */
	USE_MANA("mana"),
	
	/**
	 * Achievements for completing x quests.
	 */
	COMPLETE_QUESTS("quests"),
	
	/**
	 * Achievements for crafting x items.
	 */
	CRAFT_ITEMS("crafts"),
	
	/**
	 * Achievements for collecting x pets.
	 */
	COLLECT_PETS("pets"),
	
	/**
	 * Achievements for earning x coins.
	 */
	EARN_COINS("coins"),
	
	/**
	 * Achievements for playing x hours.
	 */
	PLAY_HOURS("playtime"),
	
	/**
	 * Achievements for gaining x levels.
	 */
	GAIN_LEVELS("levels");
	
	/**
	 * The key for this achievement type, used in player data configs.
	 */
	private String key;
	
	/**
	 * Creates an achievement type with the given key.
	 * 
	 * @param key The key for this achievement type, used in player data configs.
	 */
	private WauzAchievementType(String key) {
		this.key = key;
	}
	
	/**
	 * @return The key for this achievement type, used in player data configs.
	 */
	public String getKey() {
		return key;
	}

}
