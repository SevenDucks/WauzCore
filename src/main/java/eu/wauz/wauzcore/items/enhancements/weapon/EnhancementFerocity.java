package eu.wauz.wauzcore.items.enhancements.weapon;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;

/**
 * An enhancement, that can be applied onto an unfinished piece of equipment.
 * Effect per level: 20 % Crit Multiplier.
 * 
 * @author Wauzmons
 * 
 * @see WauzEnhancement
 */
public class EnhancementFerocity implements WauzEnhancement {
	
	/**
	 * The static name of the enhancement.
	 */
	public static String ENHANCEMENT_NAME = "Ferocity";

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
		return EquipmentType.WEAPON;
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
		parameters.setEnhancementLore((enhancementLevel * 20) + " " + ChatColor.GRAY + "% Crit Multiplier");
		return parameters;
	}

}
