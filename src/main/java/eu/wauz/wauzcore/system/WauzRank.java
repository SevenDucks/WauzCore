package eu.wauz.wauzcore.system;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.data.RankConfigurator;

/**
 * A player rank, generated from a rank config file.
 * 
 * @author Wauzmons
 */
public class WauzRank {
	
	/**
	 * A map of ranks, indexed by name.
	 */
	private static Map<String, WauzRank> rankMap = new HashMap<>();
	
	/**
	 * Initializes all ranks from the config and fills the internal rank map.
	 * 
	 * @see RankConfigurator#getAllRankKeys()
	 */
	public static void init() {
		for(String rankName : RankConfigurator.getAllRankKeys()) {
			rankMap.put(rankName, new WauzRank(rankName));
		}
	}
	
	/**
	 * @param rankName A rank name.
	 * 
	 * @return The rank with that name.
	 */
	public static WauzRank getRank(String rankName) {
		WauzRank rank = rankMap.get(rankName);
		return rank != null ? rank : rankMap.get("Normal");
	}
	
	/**
	 * The name of the rank.
	 */
	private String rankName;
	
	/**
	 * The player name prefix of the rank.
	 */
	private String rankPrefix;
	
	/**
	 * The player name color of the rank.
	 */
	private ChatColor rankColor;
	
	/**
	 * The player permission of the rank.
	 */
	private WauzPermission rankPermission;
	
	/**
	 * The daily coin reward of the rank.
	 */
	private int dailyCoins;
	
	/**
	 * The daily soulstone reward of the rank.
	 */
	private int dailySoulstones;
	
	/**
	 * Constructs a rank, based on the rank file in the /WauzCore folder.
	 * 
	 * @param rankName The name of the rank.
	 */
	public WauzRank(String rankName) {
		this.rankName = rankName;
	}

	/**
	 * @return The name of the rank.
	 */
	public String getRankName() {
		return rankName;
	}

	/**
	 * @return The player name prefix of the rank.
	 */
	public String getRankPrefix() {
		return rankPrefix;
	}

	/**
	 * @return The player name color of the rank.
	 */
	public ChatColor getRankColor() {
		return rankColor;
	}

	/**
	 * @return The player permission of the rank.
	 */
	public WauzPermission getRankPermission() {
		return rankPermission;
	}

	/**
	 * @return The daily coin reward of the rank.
	 */
	public int getDailyCoins() {
		return dailyCoins;
	}

	/**
	 * @return The daily soulstone reward of the rank.
	 */
	public int getDailySoulstones() {
		return dailySoulstones;
	}

}
