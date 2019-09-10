package eu.wauz.wauzcore.items.enhancements.armor;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;
import eu.wauz.wauzcore.system.commands.WauzDebugger;

public class EnhancementDurability implements WauzEnhancement {
	
	public static String ENHANCEMENT_NAME = "Durability";

	@Override
	public String getEnhancementId() {
		return ENHANCEMENT_NAME;
	}
	
	@Override
	public EquipmentType getEquipmentType() {
		return EquipmentType.ARMOR;
	}

	@Override
	public WauzEnhancementParameters enhanceEquipment(WauzEnhancementParameters parameters) {
		int enhancementLevel = parameters.getEnhancementLevel();
		parameters.setEnhancementLore((enhancementLevel * 128) + " " + ChatColor.GRAY + "Bonus Durability");
		parameters.setDurabilityStat(parameters.getDurabilityStat() + (enhancementLevel * 128));
		return parameters;
	}

}
