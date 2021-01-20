package eu.wauz.wauzcore.players;

import org.bukkit.entity.Player;

/**
 * A player data to save session scoped player information.
 * 
 * @author Wauzmons
 */
public class WauzPlayerData {
	
	/**
	 * The player who owns the data.
	 */
	private final Player player;
	
	/**
	 * The session id of the player.
	 */
	private final int wauzId;
	
	/**
	 * The stat section of the player data.
	 */
	private final WauzPlayerDataSectionStats stats;
	
	/**
	 * The skill section of the player data.
	 */
	private final WauzPlayerDataSectionSkills skills;
	
	/**
	 * The selection section of the player data.
	 */
	private final WauzPlayerDataSectionSelections selections;
	
	/**
	 * Creates a player data with given session id.
	 * 
	 * @param player The player who owns the data.
	 * @param wauzId The session id of the player.
	 */
	public WauzPlayerData(Player player, int wauzId) {
		this.player = player;
		this.wauzId = wauzId;
		this.stats = new WauzPlayerDataSectionStats(this);
		this.skills = new WauzPlayerDataSectionSkills(this);
		this.selections = new WauzPlayerDataSectionSelections(this);
	}
	
	/**
	 * @return The player who owns the data.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @return The session id of the player.
	 */
	public int getWauzId() {
		return wauzId;
	}

	/**
	 * @return The stat section of the player data.
	 */
	public WauzPlayerDataSectionStats getStats() {
		return stats;
	}

	/**
	 * @return The skill section of the player data.
	 */
	public WauzPlayerDataSectionSkills getSkills() {
		return skills;
	}

	/**
	 * @return The selection section of the player data.
	 */
	public WauzPlayerDataSectionSelections getSelections() {
		return selections;
	}

}
