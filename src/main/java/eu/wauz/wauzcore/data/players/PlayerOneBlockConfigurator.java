package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerConfigurationUtils;

/**
 * Configurator to fetch or modify one-block data from the Player.yml files.
 * 
 * @author Wauzmons
 */
public class PlayerOneBlockConfigurator extends PlayerConfigurationUtils {
	
// Progression
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The highest reached one-block phase of the player.
	 */
	public static int getHighestPhase(Player player) {
		return playerConfigGetInt(player, "oneblock.maxphase", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param phase The new highest reached one-block phase of the player.
	 */
	public static void setHighestPhase(Player player, int phase) {
		playerConfigSet(player, "oneblock.maxphase", phase, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The one-block phase of the player.
	 */
	public static int getPhase(Player player) {
		return playerConfigGetInt(player, "oneblock.phase", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param phase The new one-block phase of the player.
	 */
	public static void setPhase(Player player, int phase) {
		playerConfigSet(player, "oneblock.phase", phase, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The one-block level of the player.
	 */
	public static int getLevel(Player player) {
		return playerConfigGetInt(player, "oneblock.level", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param level The new one-block level of the player.
	 */
	public static void setLevel(Player player, int level) {
		playerConfigSet(player, "oneblock.level", level, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The one-block current block of the player.
	 */
	public static int getBlock(Player player) {
		return playerConfigGetInt(player, "oneblock.block", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param block The new one-block current block of the player.
	 */
	public static void setBlock(Player player, int block) {
		playerConfigSet(player, "oneblock.block", block, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The one-block total blocks destroyed of the player.
	 */
	public static int getTotalBlocks(Player player) {
		return playerConfigGetInt(player, "oneblock.blocks", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param blocks The new one-block total blocks destroyed of the player.
	 */
	public static void setTotalBlocks(Player player, int blocks) {
		playerConfigSet(player, "oneblock.blocks", blocks, true);
	}

}
