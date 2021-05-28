package eu.wauz.wauzcore.professions.crafting;

/**
 * A required material for a craftable item.
 * 
 * @author Wauzmons
 *
 * @see WauzCraftingItem
 */
public class WauzCraftingRequirement {
	
	/**
	 * The material needed for crafting.
	 */
	private String material;
	
	/**
	 * The amount of the material needed for crafting.
	 */
	private int amount;
	
	/**
	 * Creates a new crafting requirement.
	 * 
	 * @param materialString The string resembling material and amount.
	 */
	public WauzCraftingRequirement(String materialString) {
		String[] materialParts = materialString.split(";");
		this.material = materialParts[0];
		this.amount = Integer.parseInt(materialParts[1]);
	}

	/**
	 * @return The material needed for crafting.
	 */
	public String getMaterial() {
		return material;
	}

	/**
	 * @return The amount of the material needed for crafting.
	 */
	public int getAmount() {
		return amount;
	}

}
