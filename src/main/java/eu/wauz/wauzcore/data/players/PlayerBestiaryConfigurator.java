package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerBestiaryConfigurationUtils;

/**
 * Configurator to fetch or modify data from the PlayerBestiary.yml file.
 * 
 * @author Wauzmons
 */
public class PlayerBestiaryConfigurator extends PlayerBestiaryConfigurationUtils {
	
// Kill Counts
	
	/**
	 * @param player The player that owns the config file.
	 * @param entry The full name of the bestiary entry.
	 * 
	 * @return The kill count of the bestiary mob.
	 */
	public static int getBestiaryKills(Player player, String entry) {
		return playerBestiaryConfigGetInt(player, entry);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param entry The full name of the bestiary entry.
	 * @param kills The new kill count of the bestiary mob.
	 */
	public static void setBestiaryKills(Player player, String entry, int kills) {
		playerBestiaryConfigSet(player, entry, kills);
	}

}
