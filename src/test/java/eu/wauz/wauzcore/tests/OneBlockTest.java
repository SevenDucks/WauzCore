package eu.wauz.wauzcore.tests;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import eu.wauz.wauzcore.oneblock.OnePlotCalculator;
import eu.wauz.wauzcore.tests.abstracts.AbstractCoreTest;

/**
 * Tests the one-block gamemodes from WauzCore.
 * 
 * @author Wauzmons
 */
public class OneBlockTest extends AbstractCoreTest {
	
	/**
	 * Tests the coordinate assignment to plot numbers.
	 */
	@Test
	public void testPlotNumberCoords() {
		assertArrayEquals(OnePlotCalculator.getPlotGridPosition(1), new int[]{0, 0});
		assertArrayEquals(OnePlotCalculator.getPlotGridPosition(3), new int[]{1, 1});
		assertArrayEquals(OnePlotCalculator.getPlotGridPosition(42), new int[]{-3, -2});
	}

}
