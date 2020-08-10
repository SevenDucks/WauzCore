package eu.wauz.wauzcore.system.instances;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * A static cache of active instances.
 * 
 * @author Wauzmons
 *
 * @see WauzActiveInstance
 */
public class WauzActiveInstancePool {
	
	/**
	 * A map of cached instance datas by name.
	 */
	private static HashMap<String, WauzActiveInstance> storage = new HashMap<>();
	
	/**
	 * Fetches a cached instance data.
	 * 
	 * @param player A player in the instance world.
	 * 
	 * @return The requested instance data.
	 */
	public static WauzActiveInstance getInstance(Player player) {
		return storage.get(player.getWorld().getName());
	}
	
	/**
	 * Fetches a cached instance data.
	 * 
	 * @param world The instance world.
	 * 
	 * @return The requested instance data.
	 */
	public static WauzActiveInstance getInstance(World world) {
		return storage.get(world.getName());
	}
	
	/**
	 * Fetches a cached instance data.
	 * 
	 * @param worldName The name of the instance world.
	 * 
	 * @return The requested instance data.
	 */
	public static WauzActiveInstance getInstance(String worldName) {
		return storage.get(worldName);
	}
	
	/**
	 * Registers a freshly opened instance.
	 * 
	 * @param instance The instance to register
	 */
	public static void registerInstance(WauzActiveInstance instance) {
		storage.put(instance.getWorld().getName(), instance);
	}
	
	/**
	 * Unregisters a deleted instance.
	 * 
	 * @param worldName The world name of the instance to remove.
	 */
	public static void unregisterInstance(String worldName) {
		storage.remove(worldName);
	}

}
