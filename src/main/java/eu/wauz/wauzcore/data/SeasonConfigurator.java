package eu.wauz.wauzcore.data;

import org.bukkit.World;

import eu.wauz.wauzcore.data.api.SeasonConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Season.yml files.
 * 
 * @author Wauzmons
 */
public class SeasonConfigurator extends SeasonConfigurationUtils {

// General Parameters
	
	/**
	 * @param world The seasonal world.
	 * 
	 * @return The last taken plot index.
	 */
	public static int getLastTakenPlot(World world) {
		return seasonConfigGetInt(world, "lastplot");
	}
	
	/**
	 * @param world The seasonal world.
	 * @param plotIndex The new last taken plot index.
	 */
	public static void setLastTakenPlot(World world, int plotIndex) {
		seasonConfigSet(world, "lastplot", plotIndex);
	}
	
}
