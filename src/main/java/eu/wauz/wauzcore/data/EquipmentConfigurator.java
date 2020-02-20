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
	{
		COLORS.put(Color.AQUA.toString(), Color.AQUA);
		COLORS.put(Color.BLACK.toString(), Color.BLACK);
		COLORS.put(Color.BLUE.toString(), Color.BLUE);
		COLORS.put(Color.FUCHSIA.toString(), Color.FUCHSIA);
		COLORS.put(Color.GRAY.toString(), Color.GRAY);
		COLORS.put(Color.GREEN.toString(), Color.GREEN);
		COLORS.put(Color.LIME.toString(), Color.LIME);
		COLORS.put(Color.MAROON.toString(), Color.MAROON);
		COLORS.put(Color.NAVY.toString(), Color.NAVY);
		COLORS.put(Color.OLIVE.toString(), Color.OLIVE);
		COLORS.put(Color.ORANGE.toString(), Color.ORANGE);
		COLORS.put(Color.PURPLE.toString(), Color.PURPLE);
		COLORS.put(Color.RED.toString(), Color.RED);
		COLORS.put(Color.SILVER.toString(), Color.SILVER);
		COLORS.put(Color.TEAL.toString(), Color.TEAL);
		COLORS.put(Color.WHITE.toString(), Color.WHITE);
		COLORS.put(Color.YELLOW.toString(), Color.YELLOW);
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
