package eu.wauz.wauzcore.mobs.citizens;

/**
 * The level of a relationship between a player and a citizen.
 * 
 * @author Wauzmons
 */
public enum RelationLevel {
	
	/**
	 * Starting level, basic interactions.
	 */
	STRANGER("Stranger", 0, 0, 1.0),
	
	/**
	 * First level, small discounts / more quests.
	 */
	ACQUAINTANCE("Acquaintance", 1, 20, 0.9),
	
	/**
	 * Second level, bigger discounts / more quests.
	 */
	ALLY("Ally", 2, 100, 0.8),
	
	/**
	 * Third level, great discounts / more quests.
	 */
	FRIEND("Friend", 3, 300, 0.7),
	
	/**
	 * Fourth level, giant discounts / more quests.
	 */
	BEST_FRIEND("Best Friend", 4, 500, 0.6),
	
	/**
	 * Highest level, highest discounts / more quests. 
	 */
	IDOLIZED("Idolized", 5, 750, 0.5);
	
	/**
	 * Determines the relationship level, based on the amount of experience.
	 * 
	 * @param relationExp The experience to determine the level for.
	 * 
	 * @return The current relation level.
	 */
	public static RelationLevel getRelationLevel(int relationExp) {
		RelationLevel currentLevel = null;
		for(RelationLevel relationLevel : values()) {
			if(relationExp >= relationLevel.getNeededExp()) {
				currentLevel = relationLevel;
			}
		}
		return currentLevel != null ? currentLevel : STRANGER;
	}
	
	/**
	 * The name of the relation level.
	 */
	private final String relationName;
	
	/**
	 * The tier of the relation level.
	 */
	private final int relationTier;
	
	/**
	 * The experience needed to reach this level.
	 */
	private final int neededExp;
	
	/**
	 * The multiplier for shop prices.
	 */
	private final double discountMultiplier;
	
	/**
	 * Creates a new relation level with given values.
	 * 
	 * @param relationName The name of the relation level.
	 * @param relationTier The tier of the relation level.
	 * @param neededExp The experience needed to reach this level.
	 * @param discountMultiplier The multiplier for shop prices.
	 */
	RelationLevel(String relationName, int relationTier, int neededExp, double discountMultiplier) {
		this.relationName = relationName;
		this.relationTier = relationTier;
		this.neededExp = neededExp;
		this.discountMultiplier = discountMultiplier;
	}

	/**
	 * @return The name of the relation level.
	 */
	public String getRelationName() {
		return relationName;
	}

	/**
	 * @return The tier of the relation level.
	 */
	public int getRelationTier() {
		return relationTier;
	}

	/**
	 * @return The experience needed to reach this level.
	 */
	public int getNeededExp() {
		return neededExp;
	}
	
	/**
	 * @return The following relation level.
	 */
	public RelationLevel getNextLevel() {
		return relationTier >= values().length ? null : values()[relationTier + 1];
	}

	/**
	 * @return The multiplier for shop prices.
	 */
	public double getDiscountMultiplier() {
		return discountMultiplier;
	}
	
	/**
	 * @return The multiplier for quest rewards.
	 */
	public double getRewardMultiplier() {
		return 2 - discountMultiplier;
	}
	
}
