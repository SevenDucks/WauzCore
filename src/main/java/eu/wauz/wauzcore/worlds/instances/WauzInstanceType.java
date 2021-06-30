package eu.wauz.wauzcore.worlds.instances;

/**
 * The type of an instance.
 * 
 * @author Wauzmons
 */
public enum WauzInstanceType {
	
	/**
	 * The default type for instances.
	 */
	DEFAULT("default"),
	
	/**
	 * The type of instances, which consist of areas, seperated by locked doors.
	 */
	KEYS("keys"),
	
	/**
	 * The type of instances, which feature an arena, spawning waves of mobs.
	 */
	ARENA("arena");
	
	/**
	 * The name of the instance type.
	 */
	private String name;
	
	/**
	 * Creates a new instance type with given name.
	 * 
	 * @param name The name of the instance type.
	 */
	WauzInstanceType(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the instance type in a description friendly format.
	 * 
	 * @return The name of the instance type.
	 */
	@Override
	public String toString() {
		return name;
	}

}
