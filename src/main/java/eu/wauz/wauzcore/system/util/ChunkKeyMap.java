package eu.wauz.wauzcore.system.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * A tool to map chunks to objects.
 * 
 * @author Wauzmons
 */
public class ChunkKeyMap<T> {
	
	private Map<String, T> storage = new HashMap<>();
	
	/**
	 * Maps a chunk to the given value.
	 * 
	 * @param key The chunk to use as key.
	 * @param value The value.
	 */
	public void put(Chunk key, T value) {
		storage.put(getChunkKey(key), value);
	}
	
	/**
	 * Removes the given chunk and its associated value from the map.
	 * 
	 * @param key The chunk to use as key.
	 */
	public void remove(Chunk key) {
		storage.remove(getChunkKey(key));
	}
	
	/**
	 * Gets the value, mapped to the given chunk.
	 * 
	 * @param key The chunk to use as key.
	 * 
	 * @return The associated value, or null.
	 */
	public T get(Chunk key) {
		return storage.get(getChunkKey(key));
	}
	
	/**
	 * Gets all values, mapped to chunks inside the the given radius.
	 * 
	 * @param key The chunk to use as key for the center.
	 * @param radius The radius in chunks, in which values should be found.
	 * 
	 * @return All found associated values.
	 */
	public List<T> getInRadius(Chunk key, int radius) {
		List<T> values = new ArrayList<>();
		World world = key.getWorld();
		for(int x = key.getX() - radius; x <= key.getX() + radius; x++) {
			for(int z = key.getZ() - radius; z <= key.getZ() + radius; z++) {
				Chunk chunk = world.getChunkAt(x, z);
				T value = get(chunk);
				if(value != null) {
					values.add(value);
				}
			}
		}
		return values;
	}
	
	/**
	 * Converts the chunk's data into an unique key.
	 * 
	 * @param chunk The chunk to get a key to.
	 * 
	 * @return The key of the chunk.
	 */
	public static String getChunkKey(Chunk chunk) {
		return chunk.getWorld().getName() + "::" + chunk.getX() + "::" + chunk.getZ();
	}

}
