package eu.wauz.wauzcore.items.enhancements;

import eu.wauz.wauzcore.items.enums.EquipmentType;

/**
 * An enhancement, that can be applied onto an unfinished piece of equipment.
 * 
 * @author Wauzmons
 * 
 * @see WauzEquipmentEnhancer
 */
public interface WauzEnhancement {
	
	/**
	 * @return The id of the enhancement.
	 */
	public String getEnhancementId();
	
	/**
	 * @return The type of equipment, the enhancement can be applied on.
	 */
	public EquipmentType getEquipmentType();
	
	/**
	 * Tries to apply this enhancement to the given paramteters, holding values of an equipment piece.
	 * 
	 * @param parameters The initial enhancement parameters.
	 * 
	 * @return The altered enhancement parameters.
	 */
	public WauzEnhancementParameters enhanceEquipment(WauzEnhancementParameters parameters);

}
