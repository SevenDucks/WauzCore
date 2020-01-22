package eu.wauz.wauzcore.items.enhancements;

import eu.wauz.wauzcore.items.EquipmentParameters;

/**
 * Parameters of equipment items, that can be affected by enhancements.
 * 
 * @author Wauzmons
 * 
 * @see WauzEquipmentEnhancer
 */
public class WauzEnhancementParameters extends EquipmentParameters {
	
	/**
	 * The enhancement info, as displayed in lore.
	 */
	private String enhancementLore;
	
	/**
	 * Initializes the parameters, for an enhancement of the given level.
	 * 
	 * @param enhancementLevel The level of the equipment's enhancement.
	 */
	public WauzEnhancementParameters(int enhancementLevel) {
		this.enhancementLevel = enhancementLevel;
	}

	/**
	 * @return The enhancement info, as displayed in lore.
	 */
	public String getEnhancementLore() {
		return enhancementLore;
	}

	/**
	 * @param enhancementLore The new enhancement info, as displayed in lore.
	 */
	public void setEnhancementLore(String enhancementLore) {
		this.enhancementLore = enhancementLore;
	}

}
