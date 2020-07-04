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
	LIGHT("Light", 1),
	
	/**
	 * The category for medium armor.
	 */
	MEDIUM("Medium", 2),
	
	/**
	 * The category for heavy armor.
	 */
	HEAVY("Heavy", 3),
	
	/**
	 * The category for unknown armor.
	 */
	UNKNOWN("???", 0);
	
	/**
	 * The name of the armor category.
	 */
	private String name;
	
	/**
	 * The relative weight of the armor category.
	 */
	private int weight;
	
	/**
	 * Creates a new armor category with given name.
	 * 
	 * @param name The name of the armor category.
	 */
	ArmorCategory(String name, int weight) {
		this.name = name;
		this.weight = weight;
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
	 * @return The relative weight of the armor category.
	 */
	public int getWeight() {
		return weight;
	}

}
