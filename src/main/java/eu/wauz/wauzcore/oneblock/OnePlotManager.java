package eu.wauz.wauzcore.oneblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.SeasonConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;

/**
 * Used for managing one-block plots.
 * 
 * @author Wauzmons
 */
public class OnePlotManager {
	
	/**
	 * The size in blocks for one-block plots.
	 */
	public static final int PLOT_SIZE = 750;
	
	/**
	 * The radius of the border around each plot.
	 */
	public static final int BORDER_RADIUS = 125;
	
	/**
	 * The world used for the one block gamemode.
	 */
	private static final World WORLD = Bukkit.getWorld("SurvivalOneBlock");
	
	/**
	 * Gets the location of the next plot, that hasn't been claimed yet.
	 * Automatically marks it as claimed afterwards.
	 * 
	 * @return The location of the next free plot.
	 * 
	 * @see SeasonConfigurator#getLastTakenPlot(World)
	 * @see SeasonConfigurator#setLastTakenPlot(World, int)
	 * @see OnePlotManager#getPlotLocation(int)
	 */
	public static Location getNextFreePlotLocation() {
		int plotIndex = SeasonConfigurator.getLastTakenPlot(WORLD) + 1;
		SeasonConfigurator.setLastTakenPlot(WORLD, plotIndex);
		return getPlotLocation(plotIndex);
	}
	
	/**
	 * Gets the location of the plot with the given index.
	 * 
	 * @param plotIndex The index of the plot.
	 * 
	 * @return The location of the plot.
	 * 
	 * @see OnePlotCalculator#getPlotGridPosition(int)
	 */
	public static Location getPlotLocation(int plotIndex) {
		int[] gridPosition = OnePlotCalculator.getPlotGridPosition(plotIndex);
		return new Location(WORLD, gridPosition[0] * PLOT_SIZE, 70, gridPosition[1] * PLOT_SIZE);
	}
	
	/**
	 * Creates a world border around the plot for the given player.
	 * 
	 * @param player The player to create the border for.
	 */
	public static void setUpBorder(Player player) {
		WauzNmsClient.nmsBorder(player, PlayerConfigurator.getCharacterSpawn(player), BORDER_RADIUS);
		WauzDebugger.log(player, "Created World Border");
	}

}
