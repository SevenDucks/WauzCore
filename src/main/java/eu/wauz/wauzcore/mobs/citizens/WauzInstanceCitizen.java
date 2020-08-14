package eu.wauz.wauzcore.mobs.citizens;

import org.bukkit.Location;
import org.bukkit.World;

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
	 * The yaw of the citizen.
	 */
	private float yaw = 0;
	
	/**
	 * The pitch of the citizen.
	 */
	private float pitch = 0;
	
	/**
	 * Creates a new citizen that can be instanced.
	 * 
	 * @param citizenString The string to parse the citizen from: "name x y z yaw pitch".
	 */
	public WauzInstanceCitizen(String citizenString) {
		String[] citizenSpawnParams = citizenString.split(" ");
		baseCitizen = WauzCitizen.getUnassignedCitizen(citizenSpawnParams[0]);
		x = Float.parseFloat(citizenSpawnParams[1]);
		y = Float.parseFloat(citizenSpawnParams[2]);
		z = Float.parseFloat(citizenSpawnParams[3]);
		yaw = Float.parseFloat(citizenSpawnParams[4]);
		pitch = Float.parseFloat(citizenSpawnParams[5]);
	}
	
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
	
	/**
	 * Sets the coordinates for the citizen to spawn.
	 * 
	 * @param x The x position to spawn the citizen.
	 * @param y The y position to spawn the citizen.
	 * @param z The z position to spawn the citizen.
	 * @param yaw The yaw of the citizen.
	 * @param pitch The pitch of the citizen.
	 */
	public void setCoordinates(float x, float y, float z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	/**
	 * Spawns the citizen in the given world at the predefined coordinates.
	 * 
	 * @param world The world to spawn the citizen in.
	 * 
	 * @return The spawned citizen.
	 * 
	 * @see WauzInstanceCitizen#spawn(Location)
	 */
	public WauzCitizen spawn(World world) {
		Location location = new Location(world, x, y, z, yaw, pitch);
		return spawn(location);
	}
	
	/**
	 * Spawns the citizen exactly at the given location.
	 * 
	 * @param location The location to spawn the citizen at.
	 * 
	 * @return The spawned citizen.
	 * 
	 * @see WauzCitizen#addToChunkMap(WauzCitizen, org.bukkit.Chunk)
	 */
	public WauzCitizen spawn(Location location) {
		WauzCitizen citizen = new WauzCitizen(baseCitizen);
		citizen.setLocation(location);
		WauzCitizen.addToChunkMap(citizen, location.getChunk());
		return citizen;
	}

}
