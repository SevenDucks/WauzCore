package eu.wauz.wauzcore.items.enhancements.weapon;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;
import eu.wauz.wauzcore.items.enums.EquipmentType;

/**
 * An enhancement, that can be applied onto an unfinished piece of equipment.
 * Effect per level: 10 % Base Attack Boost.
 * 
 * @author Wauzmons
 * 
 * @see WauzEnhancement
 */
public class EnhancementDestruction implements WauzEnhancement {
	
	/**
	 * The static name of the enhancement.
	 */
	public static String ENHANCEMENT_NAME = "Destruction";

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
		parameters.setEnhancementLore((enhancementLevel * 10) + " " + ChatColor.GRAY + "% Base Attack Boost");
		int attackStat = parameters.getAttackStat();
		double newAttack = 1 + attackStat * (1 + enhancementLevel * 0.1);
		String mainStatString = parameters.getMainStatString();
		parameters.getLores().remove(mainStatString);
		String oldAttackString = ChatColor.RED + " " + attackStat;
		String newAttackString = ChatColor.RED + " " + (int) newAttack;
		parameters.getLores().add(mainStatString.replace(oldAttackString, newAttackString));
		return parameters;
	}

}
