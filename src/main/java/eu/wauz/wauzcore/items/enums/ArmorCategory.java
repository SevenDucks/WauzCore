package eu.wauz.wauzcore.items.enums;

/**
 * The category of a piece of armor.
 * 
 * @author Wauzmons
 */
public enum ArmorCategory {
	
	/**
	 * The category for light armor.
	 */
	LIGHT("Light"),
	
	/**
	 * The category for medium armor.
	 */
	MEDIUM("Medium"),
	
	/**
	 * The category for heavy armor.
	 */
	HEAVY("Heavy"),
	
	/**
	 * The category for unknown armor.
	 */
	UNKNOWN("???");
	
	/**
	 * The name of the armor category.
	 */
	private String name;
	
	/**
	 * Creates a new armor category with given name.
	 * 
	 * @param name The name of the armor category.
	 */
	ArmorCategory(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the armor category in a description friendly format.
	 * 
	 * @return The name of the armor category.
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Determines the fitting armor category based on the class of a player.
	 * 
	 * @param raceAndClass The race and class of the player.
	 * 
	 * @return The fitting armor category.
	 */
	public static ArmorCategory fromRaceAndClass(String raceAndClass) {
		if(raceAndClass.contains("Crusader")) {
			return HEAVY;
		}
		else if(raceAndClass.contains("Nephilim")) {
			return MEDIUM;
		}
		else if(raceAndClass.contains("Assassin")) {
			return LIGHT;
		}
		else {
			return UNKNOWN;
		}
	}

}
