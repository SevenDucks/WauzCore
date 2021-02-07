package eu.wauz.wauzcore.professions;

/**
 * The type of a gatherable resource.
 * 
 * @author Wauzmons
 */
public enum WauzResourceType {
	
	/**
	 * Effect: Knocks back everyone who attacks the mob.
	 */
	CONTAINER("Container"),
	
	/**
	 * Effect:  Explodes on death, dealing 500% damage.
	 */
	NODE("Node");
	
	/**
	 * The name of the resource type.
	 */
	private String name;
	
	/**
	 * Creates a new resource type with given name.
	 * 
	 * @param name The name of the resource type.
	 */
	WauzResourceType(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the resource type in a title friendly format.
	 * 
	 * @return The name of the resource type.
	 */
	@Override
	public String toString() {
		return name;
	}

}
