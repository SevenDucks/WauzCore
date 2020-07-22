package eu.wauz.wauzcore.oneblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import eu.wauz.wauzcore.data.SeasonConfigurator;

/**
 * Used for managing one-block plots.
 * 
 * @author Wauzmons
 */
public class OnePlotManager {
	
	/**
	 * The size in blocks for one-block plots.
	 */
	public static final int PLOT_SIZE = 250;
	
	/**
	 * The world used for the one block gamemode.
	 */
	private static final World oneBlockWorld = Bukkit.getWorld("SurvivalOneBlock");
	
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
		int plotIndex = SeasonConfigurator.getLastTakenPlot(oneBlockWorld) + 1;
		SeasonConfigurator.setLastTakenPlot(oneBlockWorld, plotIndex);
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
		return new Location(oneBlockWorld, gridPosition[0] * PLOT_SIZE, 70, gridPosition[1] * PLOT_SIZE);
	}

}
