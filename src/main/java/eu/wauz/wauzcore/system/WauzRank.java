package eu.wauz.wauzcore.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.RankConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;

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
		
		WauzCore.getInstance().getLogger().info("Loaded " + rankMap.size() + " Ranks!");
	}
	
	/**
	 * @param rankName A rank name.
	 * 
	 * @return If the rank name is valid.
	 */
	public static boolean isValidRank(String rankName) {
		WauzRank rank = rankMap.get(rankName);
		return rank != null;
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
	 * @param player The player to get the rank of.
	 * 
	 * @return The rank of the player.
	 */
	public static WauzRank getRank(OfflinePlayer player) {
		return getRank(PlayerConfigurator.getRank(player));
	}
	
	/**
	 * @return A list of all rank ids.
	 */
	public static List<String> getAllRankIds() {
		return new ArrayList<>(rankMap.keySet());
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
	 * The permission level of the rank, used for staff hierarchies.
	 */
	private int rankPermissionLevel;
	
	/**
	 * If the rank grants operator permissions.
	 */
	private boolean grantOp;
	
	/**
	 * If the rank is a staff only rank.
	 */
	private boolean isStaff;
	
	/**
	 * The daily coin reward of the rank.
	 */
	private int dailyCoins;
	
	/**
	 * The daily soulstone reward of the rank.
	 */
	private int dailySoulstones;
	
	/**
	 * The global shop discount of the rank.
	 */
	private double shopDiscount;
	
	/**
	 * Constructs a rank, based on the rank file in the /WauzCore folder.
	 * 
	 * @param rankName The name of the rank.
	 */
	public WauzRank(String rankName) {
		this.rankName = rankName;
		rankPrefix = RankConfigurator.getRankPrefix(rankName);
		rankColor = RankConfigurator.getRankColor(rankName);
		rankPermission = RankConfigurator.getRankPermission(rankName);
		rankPermissionLevel = RankConfigurator.getRankPermissionLevel(rankName);
		grantOp = RankConfigurator.hasRankOp(rankName);
		isStaff = RankConfigurator.isRankStaff(rankName);
		dailyCoins = RankConfigurator.getRankRewardCoins(rankName);
		dailySoulstones = RankConfigurator.getRankRewardSoulstones(rankName);
		shopDiscount = RankConfigurator.getRankRewardDiscount(rankName);
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
	 * @return The permission level of the rank, used for staff hierarchies.
	 */
	public int getRankPermissionLevel() {
		return rankPermissionLevel;
	}

	/**
	 * @return If the rank grants operator permissions.
	 */
	public boolean isGrantOp() {
		return grantOp;
	}

	/**
	 * @return If the rank is a staff only rank.
	 */
	public boolean isStaff() {
		return isStaff;
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

	/**
	 * @return The global shop discount of the rank.
	 */
	public double getShopDiscount() {
		return shopDiscount;
	}

}
