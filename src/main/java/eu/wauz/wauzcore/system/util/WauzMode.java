package eu.wauz.wauzcore.system.util;

import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;

/**
 * An util class to determine the mode of entities or worlds.
 * 
 * @author Wauzmons
 */
public enum WauzMode {
	
	/**
	 * The Wauz MMORPG mode.
	 */
	MMORPG("MMORPG"),
	
	/**
	 * The Wauz Survival mode.
	 */
	SURVIVAL("Survival"),
	
	/**
	 * The Wauz Arcade mode.
	 */
	ARCADE("Arcade"),
	
	/**
	 * If the Wauz mode is unknown.
	 */
	UNKNOWN("Unknown");
	
	/**
	 * The name of the Wauz mode.
	 */
	private String name;
	
	/**
	 * Creates a Wauz mode with given name.
	 * 
	 * @param name The name of the Wauz mode.
	 */
	WauzMode(String name) {
		this.name = name;
	}
	
	/**
	 * @return The name of the Wauz mode.
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * @param entity The entity for checking the mode.
	 * 
	 * @return The mode of this entity.
	 */
	public static WauzMode getMode(Entity entity) {
		return getMode(entity.getWorld());
	}
	
	/**
	 * @param world The world for checking the mode.
	 * 
	 * @return The mode of this world.
	 */
	public static WauzMode getMode(World world) {
		return getMode(world.getName());
	}
	
	/**
	 * @param worldName The name of the world for checking the mode.
	 * 
	 * @return The mode of this world name.
	 */
	public static WauzMode getMode(String worldName) {
		if(isMMORPG(worldName)) {
			return MMORPG;
		}
		else if(isSurvival(worldName)) {
			return SURVIVAL;
		}
		else if(isArcade(worldName)) {
			return ARCADE;
		}
		else {
			return UNKNOWN;
		}
	}
	
	/**
	 * @param entity The entity for checking the mode.
	 * 
	 * @return If the entity is in the hub.
	 */
	public static boolean inHub(Entity entity) {
		return inHub(entity.getWorld());
	}
	
	/**
	 * @param world The world for checking the mode.
	 * 
	 * @return If the world is the hub.
	 */
	public static boolean inHub(World world) {
		return world.getName().startsWith("Hub");
	}
	
	/**
	 * @param entity The entity for checking the mode.
	 * 
	 * @return If the entity is in the one-block world.
	 */
	public static boolean inOneBlock(Entity entity) {
		return inOneBlock(entity.getWorld());
	}
	
	/**
	 * @param world The world for checking the mode.
	 * 
	 * @return If the world is the one-block world.
	 */
	public static boolean inOneBlock(World world) {
		return world.getName().equals("SurvivalOneBlock");
	}
	
	/**
	 * @param entity The entity for checking the mode.
	 * 
	 * @return If the entity is in MMORPG mode.
	 */
	public static boolean isMMORPG(Entity entity) {
		return isMMORPG(entity.getWorld());
	}
	
	/**
	 * @param world The world for checking the mode.
	 * 
	 * @return If the world is in MMORPG mode.
	 */
	public static boolean isMMORPG(World world) {
		return isMMORPG(world.getName());
	}
	
	/**
	 * @param worldName The name of the world for checking the mode.
	 * 
	 * @return If the world with this name is in MMORPG mode.
	 */
	public static boolean isMMORPG(String worldName) {
		return isInstanceOfType(worldName, "MMORPG") ||
				StringUtils.startsWithAny(worldName, "Hub", "MMORPG");
	}
	
	/**
	 * @param entity The entity for checking the mode.
	 * 
	 * @return If the entity is in Survival mode.
	 */
	public static boolean isSurvival(Entity entity) {
		return isSurvival(entity.getWorld());
	}
	
	/**
	 * @param world The world for checking the mode.
	 * 
	 * @return If the world is in Survival mode.
	 */
	public static boolean isSurvival(World world) {
		return isSurvival(world.getName());
	}
	
	/**
	 * @param worldName The name of the world for checking the mode.
	 * 
	 * @return If the world with this name is in Survival mode.
	 */
	public static boolean isSurvival(String worldName) {
		return isInstanceOfType(worldName, "Survival") ||
				StringUtils.startsWith(worldName, "Survival");
	}
	
	/**
	 * @param entity The entity for checking the mode.
	 * 
	 * @return If the entity is in Arcade mode.
	 */
	public static boolean isArcade(Entity entity) {
		return isArcade(entity.getWorld());
	}
	
	/**
	 * @param world The world for checking the mode.
	 * 
	 * @return If the world is in Arcade mode.
	 */
	public static boolean isArcade(World world) {
		return isArcade(world.getName());
	}
	
	/**
	 * @param worldName The name of the world for checking the mode.
	 * 
	 * @return If the world with this name is in Arcade mode.
	 */
	public static boolean isArcade(String worldName) {
		return isInstanceOfType(worldName, "Arcade");
	}
	
	/**
	 * @param worldName The name of the world for checking the mode.
	 * @param worldType The type to check for.
	 * 
	 * @return If the world with this name is an instance of the specified type.
	 */
	public static boolean isInstanceOfType(String worldName, String worldType) {
		return StringUtils.startsWith(worldName, "WzInstance_" + worldType);
	}
	
	/**
	 * @param worldName The name of the world for checking the mode.
	 * 
	 * @return If the world with this name is an instance.
	 */
	public static boolean isInstance(String worldName) {
		return StringUtils.startsWith(worldName, "WzInstance_");
	}

}
