package eu.wauz.wauzcore.mobs;

import org.bukkit.Location;
import org.bukkit.World;

import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.MythicUtils;

/**
 * A cached spawn trigger for a mythic mob.
 * 
 * @author Wauzmons
 */
public class MobSpawn {
	
	/**
	 * The name of the instance where the mythic mob spawns.
	 */
	private String instanceName;
	
	/**
	 * The mythic mob type to spawn.
	 */
	private String type;
	
	/**
	 * The x position to spawn the mob.
	 */
	private float x = 0;
	
	/**
	 * The y position to spawn the mob.
	 */
	private float y = 0;
	
	/**
	 * The z position to spawn the mob.
	 */
	private float z = 0;
	
	/**
	 * Creates a new mythic mob spawn trigger.
	 * 
	 * @param instanceName The name of the instance where the mythic mob spawns.
	 * @param mobString The string to parse the mob from: "name x y z".
	 */
	public MobSpawn(String instanceName, String mobString) {
		String[] mobSpawnParams = mobString.split(" ");
		type = mobSpawnParams[0];
		x = Float.parseFloat(mobSpawnParams[1]);
		y = Float.parseFloat(mobSpawnParams[2]);
		z = Float.parseFloat(mobSpawnParams[3]);
	}
	
	/**
	 * Sets the coordinates for the mob to spawn.
	 * 
	 * @param x The x position to spawn the mob.
	 * @param y The y position to spawn the mob.
	 * @param z The z position to spawn the mob.
	 */
	public void setCoordinates(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Spawns the mob in the given world at the predefined coordinates.
	 * 
	 * @param world The world to spawn the mob in.
	 */
	public void spawn(World world) {
		Location location = new Location(world, x, y, z);
		location.setYaw(Chance.negativePositive(180));
		spawn(location);
	}
	
	/**
	 * Spawns the mob exactly at the given location.
	 * 
	 * @param location The location to spawn the mob at.
	 */
	public void spawn(Location location) {
		MythicUtils.spawnMob(type, location, "Instance " + instanceName);
	}

}
