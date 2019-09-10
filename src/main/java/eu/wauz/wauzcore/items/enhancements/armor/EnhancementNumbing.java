package eu.wauz.wauzcore.items.enhancements.armor;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;

public class EnhancementNumbing implements WauzEnhancement {

	public static String ENHANCEMENT_NAME = "Numbing";

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
		parameters.setEnhancementLore((enhancementLevel * 10) + " " + ChatColor.GRAY + "% Base Defense Boost");
		int defenseStat = parameters.getDefenseStat();
		double newDefense = 1 + defenseStat * (1 + enhancementLevel * 0.1);
		String mainStatString = parameters.getMainStatString();
		parameters.getLores().remove(mainStatString);
		String oldDefenseString = ChatColor.BLUE + " " + defenseStat;
		String newDefenseString = ChatColor.BLUE + " " + (int) newDefense;
		parameters.getLores().add(mainStatString.replace(oldDefenseString, newDefenseString));
		return parameters;
	}
	
}
