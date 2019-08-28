package eu.wauz.wauzcore.system.util;

import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;

public enum WauzMode {
	
	MMORPG("MMORPG"),
	SURVIVAL("Survival");
	
	private String name;
	
	WauzMode(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static WauzMode getMode(String worldName) {
		if(isSurvival(worldName))
			return SURVIVAL;
		else
			return MMORPG;
	}
	
	public static boolean inHub(Entity player) {
		return player.getWorld().getName().equals("HubNexus");
	}
	
	public static boolean isMMORPG(Entity player) {
		return isMMORPG(player.getWorld());
	}
	
	public static boolean isMMORPG(World world) {
		return isMMORPG(world.getName());
	}
	
	public static boolean isMMORPG(String worldName) {
		return isInstanceOfType(worldName, "MMORPG") ||
				StringUtils.equalsAny(worldName, "HubNexus", "Wauzland", "Dalyreos");
	}
	
	public static boolean isSurvival(Entity player) {
		return isSurvival(player.getWorld());
	}
	
	public static boolean isSurvival(World world) {
		return isSurvival(world.getName());
	}
	
	public static boolean isSurvival(String worldName) {
		return isInstanceOfType(worldName, "Survival") ||
				StringUtils.equals(worldName, "Survival");
	}
	
	public static boolean isInstanceOfType(String worldName, String worldType) {
		return StringUtils.startsWith(worldName, "WzInstance_" + worldType + "_");
	}
	
	public static boolean isInstance(String worldName) {
		return StringUtils.startsWith(worldName, "WzInstance_");
	}

}
