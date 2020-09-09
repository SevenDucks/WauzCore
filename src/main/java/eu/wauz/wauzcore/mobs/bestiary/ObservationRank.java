package eu.wauz.wauzcore.mobs.bestiary;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.menu.heads.GenericIconHeads;

/**
 * The rank representing the detail level of a bestiary entry.
 * 
 * @author Wauzmons
 */
public enum ObservationRank {
	
	/**
	 * Default rank, showing the mob name, if at least one kill was achieved.
	 */
	D(ChatColor.GRAY + "D", 0, 0, 0, GenericIconHeads.getUnknownItem(), 0),
	
	/**
	 * First tier rank, showing a detailed description or backstory.
	 */
	C(ChatColor.GREEN + "C", 1, 20, 1, GenericIconHeads.getRankCItem(), 0),
	
	/**
	 * Second tier rank, showing all base stats.
	 */
	B(ChatColor.YELLOW + "B", 2, 100, 3, GenericIconHeads.getRankBItem(), 1),
	
	/**
	 * Third tier rank, increasing damage against the mob by 25%.
	 */
	A(ChatColor.GOLD + "A", 3, 500, 6, GenericIconHeads.getRankAItem(), 2),
	
	/**
	 * Highest tier rank, doubles the loot dropped by the mob.
	 */
	S(ChatColor.RED + "S", 4, 2500, 10, GenericIconHeads.getRankSItem(), 3);
	
	/**
	 * The name of the observation rank.
	 */
	private String rankName;
	
	/**
	 * The tier of the observation rank.
	 */
	private int rankTier;
	
	/**
	 * The amount of kills needed to rach the rank for normal mobs.
	 */
	private int normalKills;
	
	/**
	 * The amount of kills needed to rach the rank for boss mobs.
	 */
	private int bossKills;
	
	/**
	 * The icon item stack representing the rank in menus.
	 */
	private ItemStack iconItemStack;
	
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
	 * @param rankTier The tier of the observation rank.
	 * @param normalKills The amount of kills needed to rach the rank for normal mobs.
	 * @param bossKills The amount of kills needed to rach the rank for boss mobs.
	 * @param iconItemStack The icon item stack representing the rank in menus.
	 * @param souls The amount of soulstones rewarded for reaching the rank.
	 */
	ObservationRank(String rankName, int rankTier, int normalKills, int bossKills, ItemStack iconItemStack, int souls) {
		this.rankName = rankName;
		this.rankTier = rankTier;
		this.normalKills = normalKills;
		this.bossKills = bossKills;
		this.iconItemStack = iconItemStack;
		this.souls = souls;
	}

	/**
	 * @return The name of the observation rank.
	 */
	public String getRankName() {
		return rankName;
	}

	/**
	 * @return The tier of the observation rank.
	 */
	public int getRankTier() {
		return rankTier;
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
	 * @return The icon item stack representing the rank in menus.
	 */
	public ItemStack getIconItemStack() {
		return iconItemStack.clone();
	}

	/**
	 * @return The amount of soulstones rewarded for reaching the rank.
	 */
	public int getSouls() {
		return souls;
	}
	
	/**
	 * @return The following observation rank.
	 */
	public ObservationRank getNextRank() {
		return rankTier + 1 >= values().length ? null : values()[rankTier + 1];
	}
	
}
