package eu.wauz.wauzcore.mobs.citizens;

/**
 * A citizen that can be instanced, to be encountered in instanced worlds.
 * 
 * @author Wauzmons
 * 
 * @see WauzCitizen
 */
public class WauzInstanceCitizen {
	
	/**
	 * The base citizen, to create instances from.
	 */
	private WauzCitizen baseCitizen;
	
	/**
	 * The x position to spawn the citizen.
	 */
	private float x = 0;
	
	/**
	 * The y position to spawn the citizen.
	 */
	private float y = 0;
	
	/**
	 * The z position to spawn the citizen.
	 */
	private float z = 0;
	
	/**
	 * Creates a new citizen that can be instanced.
	 * 
	 * @param baseCitizen The base citizen, to create instances from.
	 */
	public WauzInstanceCitizen(WauzCitizen baseCitizen) {
		this.baseCitizen = baseCitizen;
	}

	/**
	 * @return The base citizen, to create instances from.
	 */
	public WauzCitizen getBaseCitizen() {
		return baseCitizen;
	}

}
