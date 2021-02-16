package eu.wauz.wauzcore.professions;

/**
 * The type of a gatherable resource.
 * 
 * @author Wauzmons
 */
public enum WauzResourceType {
	
	/**
	 * A container-type resource that can be opened.
	 */
	CONTAINER("Container"),
	
	/**
	 * A node-type resource that needs to be gathered with a tool.
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
