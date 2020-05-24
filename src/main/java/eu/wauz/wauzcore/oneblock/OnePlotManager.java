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
	 * @see OnePlotManager#getPlotGridPosition(int)
	 */
	public static Location getPlotLocation(int plotIndex) {
		int[] gridPosition = getPlotGridPosition(plotIndex);
		return new Location(oneBlockWorld, gridPosition[0] * PLOT_SIZE, 70, gridPosition[1] * PLOT_SIZE);
	}

	/**
	 * Gets the grid position of the plot with the given index.
	 * A complex formula is used to place the plots on a "quadratic spiral".
	 * 
	 * @param plotIndex The index of the plot.
	 * 
	 * @return The grid postion as array, where 0 = x and 1 = z.
	 */
	public static int[] getPlotGridPosition(int plotIndex) {
		int k = (int) Math.ceil((Math.sqrt(plotIndex) - 1) / 2);
		int t = 2 * k + 1;
		int m = (int) Math.pow(t, 2);
        t--;
        
        if(plotIndex >= m - t) {
        	return new int[]{k - (m - plotIndex), -k};
        }
        else {
        	m -= t;
        }
        
        if(plotIndex >= m - t) {
        	return  new int[]{-k, -k + (m - plotIndex)};
        }
        else {
        	m -= t;
        }
        
        if(plotIndex >= m - t) {
        	return  new int[]{-k + (m - plotIndex), k};
        }
        else {
        	return  new int[]{k, k - (m - plotIndex - t)};
        }
	}
	
}
