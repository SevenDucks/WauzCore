package eu.wauz.wauzunit.abstracts;

import org.bukkit.Material;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import eu.wauz.wauzcore.WauzCore;

/**
 * An abstract test class for WauzCore.
 * 
 * @author Wauzmons
 */
public abstract class AbstractCoreTest {
	
	/**
	 * The mocked server.
	 */
	private static ServerMock server;
	
	/**
	 * Starts the mocked server.
	 */
	@BeforeClass
	public static void setUpClass() {
		server = MockBukkit.mock();
	}
	
	/**
	 * Stops the mocked server.
	 */
	@AfterClass
	public static void tearDownClass() {
		MockBukkit.unmock();
	}
	
	/**
	 * Loads the WauzCore plugin into the server.
	 * Doesn't work at the moment.
	 * 
	 * @return The WauzCore plugin.
	 */
	@Deprecated
	public WauzCore loadWauzCore() {
		return MockBukkit.load(WauzCore.class);
	}
	
	/**
	 * Gets the mock server.
	 * 
	 * @return The mock server.
	 */
	public ServerMock getMockServer() {
		return server;
	}
	
	/**
	 * Creates a superflat mock world.
	 * Level 0 is bedrock.
	 * Level 1-3 are grass blocks.
	 * Everything above is air.
	 * 
	 * @return The mock world.
	 */
	public WorldMock getMockWorld() {
		return new WorldMock(Material.GRASS_BLOCK, 3);
	}
	
	/**
	 * Creates a mock plugin.
	 * 
	 * @return The mock plugin.
	 */
	public MockPlugin getMockPlugin() {
		return MockBukkit.createMockPlugin();
	}

}
