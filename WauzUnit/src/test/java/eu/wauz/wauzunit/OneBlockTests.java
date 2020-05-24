package eu.wauz.wauzunit;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import eu.wauz.wauzcore.oneblock.OnePlotManager;

/**
 * Tests the one-block gamemodes from WauzCore.
 * 
 * @author Wauzmons
 */
public class OneBlockTests {
	
	/**
	 * Tests the coordinate assignment to plot numbers.
	 */
	@Test
	public void testPlotNumberCoords() {
		assertArrayEquals(OnePlotManager.getPlotGridPosition(1), new int[]{0, 0});
		assertArrayEquals(OnePlotManager.getPlotGridPosition(3), new int[]{1, 1});
		assertArrayEquals(OnePlotManager.getPlotGridPosition(42), new int[]{-3, -2});
	}

}
