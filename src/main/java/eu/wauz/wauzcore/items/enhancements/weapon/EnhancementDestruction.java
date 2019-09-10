package eu.wauz.wauzcore.items.enhancements.weapon;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancementParameters;

public class EnhancementDestruction implements WauzEnhancement {
	
	public static String ENHANCEMENT_NAME = "Destruction";

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
