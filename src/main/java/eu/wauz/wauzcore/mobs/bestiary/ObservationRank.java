package eu.wauz.wauzcore.mobs.bestiary;

/**
 * The rank representing the detail level of a bestiary entry.
 * 
 * @author Wauzmons
 */
public enum ObservationRank {
	
	/**
	 * Default rank, showing the mob name, if at least one kill was achieved.
	 */
	D("D", 0, 0, 0),
	
	/**
	 * First tier rank, showing a detailed description or backstory.
	 */
	C("C", 20, 1, 0),
	
	/**
	 * Second tier rank, showing all base stats.
	 */
	B("B", 100, 3, 1),
	
	/**
	 * Third tier rank, showing a list of all drops from the loot table.
	 */
	A("C", 500, 6, 2),
	
	/**
	 * Highest tier rank, showing exact amounts and drop rates of the loot.
	 */
	S("S", 2500, 10, 3);
	
	/**
	 * The name of the observation rank.
	 */
	private String rankName;
	
	/**
	 * The amount of kills needed to rach the rank for normal mobs.
	 */
	private int normalKills;
	
	/**
	 * The amount of kills needed to rach the rank for boss mobs.
	 */
	private int bossKills;
	
	/**
	 * The amount of soulstones rewarded for reaching the rank.
	 */
	private int souls;
	
	/**
	 * Determines the observation rank, based on the amount of mob kills.
	 * 
	 * @param mobKills The mob kills to determine the level for.
	 * @param isBoss If the kills where on a boss mob.
	 * 
	 * @return The current observation rank.
	 */
	public static ObservationRank getObservationRank(int mobKills, boolean isBoss) {
		ObservationRank currentRank = null;
		for(ObservationRank observationRank : values()) {
			int neededKills = isBoss ? observationRank.getBossKills() : observationRank.getNormalKills();
			if(mobKills >= neededKills) {
				currentRank = observationRank;
			}
		}
		return currentRank != null ? currentRank : D;
	}
	
	/**
	 * Creates a new observation rank with given values.
	 * 
	 * @param rankName The name of the observation rank.
	 * @param normalKills The amount of kills needed to rach the rank for normal mobs.
	 * @param bossKills The amount of kills needed to rach the rank for boss mobs.
	 * @param souls The amount of soulstones rewarded for reaching the rank.
	 */
	ObservationRank(String rankName, int normalKills, int bossKills, int souls) {
		this.rankName = rankName;
		this.normalKills = normalKills;
		this.bossKills = bossKills;
		this.souls = souls;
	}

	/**
	 * @return The name of the observation rank.
	 */
	public String getRankName() {
		return rankName;
	}

	/**
	 * @return The amount of kills needed to rach the rank for normal mobs.
	 */
	public int getNormalKills() {
		return normalKills;
	}

	/**
	 * @return The amount of kills needed to rach the rank for boss mobs.
	 */
	public int getBossKills() {
		return bossKills;
	}

	/**
	 * @return The amount of soulstones rewarded for reaching the rank.
	 */
	public int getSouls() {
		return souls;
	}
	
}
