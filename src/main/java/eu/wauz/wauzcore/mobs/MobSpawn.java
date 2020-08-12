package eu.wauz.wauzcore.mobs;

import org.bukkit.Location;
import org.bukkit.World;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

/**
 * A cached spawn trigger for a mythic mob.
 * 
 * @author Wauzmons
 */
public class MobSpawn {
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
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
	 * The level of the mob to spawn.
	 */
	private int lvl = 1;
	
	/**
	 * The mythic mob to spawn.
	 */
	private MythicMob mob;
	
	/**
	 * Creates a new spawn trigger.
	 * 
	 * @param mobName The mythic mob to spawn when triggered.
	 */
	public MobSpawn(String mobName) {
		mob = mythicMobs.getMythicMob(mobName);
	}
	
	/**
	 * Sets the level for the mob to spawn.
	 * 
	 * @param lvl The level of the mob to spawn.
	 */
	public void setLevel(int lvl) {
		this.lvl = lvl;
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
		spawn(location);
	}
	
	/**
	 * Spawns the mob exactly at the given location.
	 * 
	 * @param location The location to spawn the mob at.
	 */
	public void spawn(Location location) {
		try {
			mythicMobs.spawnMythicMob(mob, location, lvl);
		}
		catch (InvalidMobTypeException e) {
			e.printStackTrace();
		}
	}

}
