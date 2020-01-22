package eu.wauz.wauzcore.items.enhancements.armor;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;
import eu.wauz.wauzcore.items.enums.EquipmentType;

/**
 * An enhancement, that can be applied onto an unfinished piece of equipment.
 * Effect per level: 15 % Rune Effectiveness.
 * 
 * @author Wauzmons
 * 
 * @see WauzEnhancement
 */
public class EnhancementMastery implements WauzEnhancement {

	/**
	 * The static name of the enhancement.
	 */
	public static String ENHANCEMENT_NAME = "Mastery";

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
		parameters.setEnhancementLore((enhancementLevel * 15) + " " + ChatColor.GRAY + "% Rune Effectiveness");
		return parameters;
	}
	
}
