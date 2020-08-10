package eu.wauz.wauzcore.system.instances;

/**
 * The status of a key of an instance.
 * 
 * @author Wauzmons
 */
public enum WauzInstanceKeyStatus {
	
	/**
	 * The status of an ubobtained key.
	 */
	UNOBTAINED("unobtained"),
	
	/**
	 * The status of an obtained key.
	 */
	OBTAINED("obtained"),
	
	/**
	 * The status of an used key.
	 */
	USED("used");
	
	/**
	 * The name of the instance type.
	 */
	private String name;
	
	/**
	 * Creates a new instance key status with given name.
	 * 
	 * @param name The name of the instance key status.
	 */
	WauzInstanceKeyStatus(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the instance key status in a description friendly format.
	 * 
	 * @return The name of the instance key status.
	 */
	@Override
	public String toString() {
		return name;
	}

}
