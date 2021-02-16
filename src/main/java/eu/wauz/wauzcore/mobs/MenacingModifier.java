package eu.wauz.wauzcore.mobs;

/**
 * A possible modifier that can be applied to a menacing mob.
 * 
 * @author Wauzmons
 */
public enum MenacingModifier {
	
	/**
	 * Effect: Knocks back everyone who attacks the mob.
	 */
	DEFLECTING("Deflecting"),
	
	/**
	 * Effect: Explodes on death, dealing 500% damage.
	 */
	EXPLOSIVE("Explosive"),
	
	/**
	 * Effect: Takes only 20% of inflicted damage.
	 */
	MASSIVE("Massive"),
	
	/**
	 * Effect: Chases the player with 200% speed.
	 */
	RAVENOUS("Ravenous"),
	
	/**
	 * Effect: Splits into 4 mobs on death.
	 */
	SPLITTING("Splitting");
	
	/**
	 * The name of the menacing modifier.
	 */
	private String name;
	
	/**
	 * Creates a new menacing modifier with given name.
	 * 
	 * @param name The name of the menacing modifier.
	 */
	MenacingModifier(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the menacing modifier in a title friendly format.
	 * 
	 * @return The name of the menacing modifier.
	 */
	@Override
	public String toString() {
		return name;
	}

}
