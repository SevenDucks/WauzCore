package eu.wauz.wauzcore.items.enhancements.weapon;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;

public class EnhancementExpertise implements WauzEnhancement {
	
	public static String ENHANCEMENT_NAME = "Expertise";

	@Override
	public String getEnhancementId() {
		return ENHANCEMENT_NAME;
	}
	
	@Override
	public EquipmentType getEquipmentType() {
		return EquipmentType.WEAPON;
	}

	@Override
	public WauzEnhancementParameters enhanceEquipment(WauzEnhancementParameters parameters) {
		int enhancementLevel = parameters.getEnhancementLevel();
		parameters.setEnhancementLore((enhancementLevel * 15) + " " + ChatColor.GRAY + "% Skill Damage");
		return parameters;
	}

}
