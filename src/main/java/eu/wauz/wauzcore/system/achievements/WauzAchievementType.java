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
	KILL_ENEMIES("kills", "Enemies Killed", true),
	
	/**
	 * Achievements for identifying x items.
	 */
	IDENTIFY_ITEMS("identifies", "Items Identified", true),
	
	/**
	 * Achievements for using x mana.
	 */
	USE_MANA("mana", "Mana Used", true),
	
	/**
	 * Achievements for completing x quests.
	 */
	COMPLETE_QUESTS("quests", "Quests Completed", true),
	
	/**
	 * Achievements for crafting x items.
	 */
	CRAFT_ITEMS("crafts", "Items Crafted", true),
	
	/**
	 * Achievements for collecting x pets.
	 */
	COLLECT_PETS("pets", "Pets Collected", true),
	
	/**
	 * Achievements for earning x coins.
	 */
	EARN_COINS("coins", "Coins Earned", true),
	
	/**
	 * Achievements for playing x hours.
	 */
	PLAY_HOURS("playtime", "Hours Played", true),
	
	/**
	 * Achievements for gaining x levels.
	 */
	GAIN_LEVELS("levels", "Levels Gained", true),
	
	/**
	 * Achievements for defeating a specific boss.
	 */
	DEFEAT_BOSSES("bosses", "Bosses Defeated", false),
	
	/**
	 * Achievements for collecting a specific artifact.
	 */
	COLLECT_ARTIFACTS("artifacts", "Artifacts Collected", false),
	
	/**
	 * Achievements for completing a specific campaign quest.
	 */
	COMPLETE_CAMPAIGN("campaigns", "Campaigns Completed", false),
	
	/**
	 * Achievements for exploring a specific region.
	 */
	EXPLORE_REGION("regions", "Regions Explored", false);
	
	/**
	 * The key for this achievement type, used in player data configs.
	 */
	private String key;
	
	/**
	 * The message to show behind counters, i.e. 10/10 "Enemies Killed".
	 */
	private String message;
	
	/**
	 * If the achievement type is generic / non unique.
	 */
	private boolean generic;
	
	/**
	 * Creates an achievement type with the given key.
	 * 
	 * @param key The key for this achievement type, used in player data configs.
	 * @param message The message to show behind counters, i.e. 10/10 "Enemies Killed".
	 * @param generic If the achievement type is generic / non unique.
	 */
	private WauzAchievementType(String key, String message, boolean generic) {
		this.key = key;
		this.message = message;
		this.generic = generic;
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

	/**
	 * @return If the achievement type is generic / non unique.
	 */
	public boolean isGeneric() {
		return generic;
	}

}
