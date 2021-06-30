package eu.wauz.wauzcore.worlds.instances;

import org.bukkit.ChatColor;

/**
 * The status of a key of an instance.
 * 
 * @author Wauzmons
 */
public enum WauzInstanceKeyStatus {
	
	/**
	 * The status of an ubobtained key.
	 */
	UNOBTAINED("UNOBTAINED", ChatColor.RED),
	
	/**
	 * The status of an obtained key.
	 */
	OBTAINED("OBTAINED", ChatColor.YELLOW),
	
	/**
	 * The status of an used key.
	 */
	USED("USED", ChatColor.GREEN);
	
	/**
	 * The name of the instance key status.
	 */
	private String name;
	
	/**
	 * The color of the instance key status.
	 */
	private ChatColor color;
	
	/**
	 * Creates a new instance key status with given name.
	 * 
	 * @param name The name of the instance key status.
	 * @param color The color of the instance key status.
	 */
	WauzInstanceKeyStatus(String name, ChatColor color) {
		this.name = name;
		this.color = color;
	}
	
	/**
	 * Returns the name of the instance key status in a description friendly format.
	 * 
	 * @return The name of the instance key status.
	 */
	@Override
	public String toString() {
		return color + name;
	}

}
