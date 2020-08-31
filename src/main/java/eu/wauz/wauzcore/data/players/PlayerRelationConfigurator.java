package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerRelationConfigurationUtils;
import eu.wauz.wauzcore.mobs.citizens.RelationLevel;

/**
 * Configurator to fetch or modify data from the PlayerRelation.yml files.
 * 
 * @author Wauzmons
 */
public class PlayerRelationConfigurator extends PlayerRelationConfigurationUtils {
	
// General Parameters
	
	/**
	 * @param player The player that owns the config file.
	 * @param citizen The citizen that the config belongs to.
	 * 
	 * @return The relation progress between player and citizen.
	 */
	public static int getRelationProgress(Player player, String citizen) {
		return playerRelationConfigGetInt(player, citizen, "relation");
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param citizen The citizen that the config belongs to.
	 * @param progress The new relation progress between player and citizen.
	 */
	public static void setRelationProgress(Player player, String citizen, int progress) {
		int maxProgress = RelationLevel.values()[RelationLevel.values().length - 1].getNeededExp();
		playerRelationConfigSet(player, citizen, "relation", progress > maxProgress ? maxProgress : progress);
	}

}
