package eu.wauz.wauzcore.items.enhancements.armor;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;
import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.system.annotations.Enhancement;

/**
 * An enhancement, that can be applied onto an unfinished piece of equipment.
 * Effect per level: 128 Bonus Durability.
 * 
 * @author Wauzmons
 * 
 * @see WauzEnhancement
 */
@Enhancement
public class EnhancementDurability implements WauzEnhancement {
	
	/**
	 * The static name of the enhancement.
	 */
	public static String ENHANCEMENT_NAME = "Durability";

	/**
	 * @return The id of the enhancement.
	 */
	@Override
	public String getEnhancementId() {
		return ENHANCEMENT_NAME;
	}
	
	/**
	 * @return The type of equipment, the enhancement can be applied on.
	 */
	@Override
	public EquipmentType getEquipmentType() {
		return EquipmentType.ARMOR;
	}

	/**
	 * Tries to apply this enhancement to the given paramteters, holding values of an equipment piece.
	 * 
	 * @param parameters The initial enhancement parameters.
	 * 
	 * @return The altered enhancement parameters.
	 */
	@Override
	public WauzEnhancementParameters enhanceEquipment(WauzEnhancementParameters parameters) {
		int enhancementLevel = parameters.getEnhancementLevel();
		parameters.setEnhancementLore((enhancementLevel * 128) + " " + ChatColor.GRAY + "Bonus Durability");
		parameters.setDurabilityStat(parameters.getDurabilityStat() + (enhancementLevel * 128));
		return parameters;
	}

}
