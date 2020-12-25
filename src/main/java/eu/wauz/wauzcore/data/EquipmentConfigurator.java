package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.items.enums.EquipmentType;

/**
 * Configurator to fetch or modify data from the Equipment.yml.
 * 
 * @author Wauzmons
 */
public class EquipmentConfigurator extends GlobalConfigurationUtils {

// General Parameters
	
	/**
	 * A map of all possible leather dye colors.
	 */
	private static final Map<String, Color> COLORS = new HashMap<String, Color>();
	
	static {
		COLORS.put("AQUA", Color.AQUA);
		COLORS.put("BLACK", Color.BLACK);
		COLORS.put("BLUE", Color.BLUE);
		COLORS.put("FUCHSIA", Color.FUCHSIA);
		COLORS.put("GRAY", Color.GRAY);
		COLORS.put("GREEN", Color.GREEN);
		COLORS.put("LIME", Color.LIME);
		COLORS.put("MAROON", Color.MAROON);
		COLORS.put("NAVY", Color.NAVY);
		COLORS.put("OLIVE", Color.OLIVE);
		COLORS.put("ORANGE", Color.ORANGE);
		COLORS.put("PURPLE", Color.PURPLE);
		COLORS.put("RED", Color.RED);
		COLORS.put("SILVER", Color.SILVER);
		COLORS.put("TEAL", Color.TEAL);
		COLORS.put("WHITE", Color.WHITE);
		COLORS.put("YELLOW", Color.YELLOW);
	}
	
	/**
	 * @return The keys of all equipment base types.
	 */
	public static List<String> getEquipmentKeys() {
		return new ArrayList<>(mainConfigGetKeys("Equipment", null));
	}
	
// Equipment Stats
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The general type of the equipment.
	 */
	public static EquipmentType getEquipmentType(String equipment) {
		return EquipmentType.valueOf(mainConfigGetString("Equipment", equipment + ".type"));
	}
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The material of the equipment.
	 */
	public static Material getMaterial(String equipment) {
		return Material.valueOf(mainConfigGetString("Equipment", equipment + ".material"));
	}
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The name of the equipment.
	 */
	public static String getName(String equipment) {
		return mainConfigGetString("Equipment", equipment + ".name");
	}
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The main stat value of the equipment.
	 */
	public static double getMainStat(String equipment) {
		return mainConfigGetDouble("Equipment", equipment + ".mainstat");
	}
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The speed stat value of the equipment.
	 */
	public static double getSpeedStat(String equipment) {
		return mainConfigGetDouble("Equipment", equipment + ".speedstat");
	}
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The durability stat value of the equipment.
	 */
	public static int getDurabilityStat(String equipment) {
		return mainConfigGetInt("Equipment", equipment + ".durabilitystat");
	}
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The swiftness stat value of the equipment.
	 */
	public static int getSwiftnessStat(String equipment) {
		return mainConfigGetInt("Equipment", equipment + ".swiftnessstat");
	}
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The armor category of the equipment.
	 */
	public static ArmorCategory getCategory(String equipment) {
		String armorCategory = mainConfigGetString("Equipment", equipment + ".category");
		if(armorCategory == null) {
			return null;
		}
		return ArmorCategory.valueOf(armorCategory);
	}
	
	/**
	 * @param equipment The name of the equipment base type.
	 * 
	 * @return The leather color of the equipment.
	 */
	public static Color getLeatherDye(String equipment) {
		String leatherDye = mainConfigGetString("Equipment", equipment + ".leatherdye");
		if(leatherDye == null) {
			return null;
		}
		return COLORS.get(leatherDye);
	}
	
}
