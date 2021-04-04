package eu.wauz.wauzcore.items.enhancements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentBuilder;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * This class is used to register, find and apply enhancements.
 * 
 * @author Wauzmons
 * 
 * @see WauzEnhancement
 */
public class WauzEquipmentEnhancer {
	
	/**
	 * A map of all registered weapon enhancements.
	 */
	private static Map<String, WauzEnhancement> weaponEnhancementMap = new HashMap<>();
	
	/**
	 * A map of all registered armor enhancements.
	 */
	private static Map<String, WauzEnhancement> armorEnhancementMap = new HashMap<>();
	
	/**
	 * Gets an enhancement for given id from the map.
	 * 
	 * @param enhancementId The id of the enhancement.
	 * 
	 * @return The enhancement or null, if not found.
	 */
	public static WauzEnhancement getEnhancement(String enhancementId) {
		WauzEnhancement enhancement = weaponEnhancementMap.get(enhancementId);
		if(enhancement == null) {
			enhancement = armorEnhancementMap.get(enhancementId);
		}
		return enhancement;
	}
	
	/**
	 * @return A list of all weapon enhancements.
	 */
	public static List<WauzEnhancement> getWeaponEnhancements() {
		return new ArrayList<WauzEnhancement>(weaponEnhancementMap.values());
	}
	
	/**
	 * @return A list of all armor enhancements.
	 */
	public static List<WauzEnhancement> getArmorEnhancements() {
		return new ArrayList<WauzEnhancement>(armorEnhancementMap.values());
	}
	
	/**
	 * @return A list of all enhancements.
	 */
	public static List<WauzEnhancement> getAllEnhancements() {
		List<WauzEnhancement> enhancements = new ArrayList<>();
		enhancements.addAll(weaponEnhancementMap.values());
		enhancements.addAll(armorEnhancementMap.values());
		return enhancements;
	}
	
	/**
	 * @return A list of all weapon enhancement ids.
	 */
	public static List<String> getWeaponEnhancementIds() {
		return new ArrayList<>(weaponEnhancementMap.keySet());
	}
	
	/**
	 * @return A list of all armor enhancement ids.
	 */
	public static List<String> getArmorEnhancementIds() {
		return new ArrayList<>(armorEnhancementMap.keySet());
	}
	
	/**
	 * @return A list of all enhancement ids.
	 */
	public static List<String> getAllEnhancementIds() {
		List<String> enhancementIds = new ArrayList<>();
		enhancementIds.addAll(weaponEnhancementMap.keySet());
		enhancementIds.addAll(armorEnhancementMap.keySet());
		return enhancementIds;
	}
	
	/**
	 * Registers an enhancement.
	 * 
	 * @param enhancement The enhancement to register.
	 */
	public static void registerEnhancement(WauzEnhancement enhancement) {
		EquipmentType equipmentType = enhancement.getEquipmentType();
		
		if(equipmentType.equals(EquipmentType.WEAPON)) {
			weaponEnhancementMap.put(enhancement.getEnhancementId(), enhancement);
		}
		else if(equipmentType.equals(EquipmentType.ARMOR)) {
			armorEnhancementMap.put(enhancement.getEnhancementId(), enhancement);
		}
	}
	
	/**
	 * Adds an enhancement to a running equipment identifier.
	 * First the parameters are extracted.
	 * Then a random enhancement that fits the equipment type is applied.
	 * After that, the altered paramters are inserted back into the identifier.
	 * 
	 * @param identifier The equipment identifier, receiving the enhancement.
	 * @param enhancementLevel The level the enhancement should have.
	 * 
	 * @see WauzEquipmentEnhancer#enhanceEquipment(WauzEnhancement, WauzEnhancementParameters)
	 */
	public static void enhanceEquipment(WauzEquipmentIdentifier identifier, int enhancementLevel) {
		WauzEnhancementParameters parameters = new WauzEnhancementParameters(enhancementLevel);
		parameters.setAttackStat(identifier.getAttackStat());
		parameters.setDefenseStat(identifier.getDefenseStat());
		parameters.setDurabilityStat(identifier.getDurabilityStat());
		parameters.setSpeedStat(identifier.getSpeedStat());
		
		WauzEnhancement enhancement = null;
		EquipmentType equipmentType = identifier.getEquipmentType();
		if(equipmentType.equals(EquipmentType.WEAPON)) {
			List<WauzEnhancement> weaponEnhancements = getWeaponEnhancements();
			enhancement = weaponEnhancements.get(Chance.randomInt(weaponEnhancements.size()));
		}
		else if(equipmentType.equals(EquipmentType.ARMOR)) {
			List<WauzEnhancement> armorEnhancements = getArmorEnhancements();
			enhancement = armorEnhancements.get(Chance.randomInt(armorEnhancements.size()));
		}
		
		parameters = enhanceEquipment(identifier.getBuilder(), enhancement, parameters);
		identifier.setAttackStat(parameters.getAttackStat());
		identifier.setDefenseStat(parameters.getDefenseStat());
		identifier.setDurabilityStat(parameters.getDurabilityStat());
		identifier.setSpeedStat(parameters.getSpeedStat());
	}
	
	/**
	 * Applies an enhancement to a set of parameters, holding values of an equipment piece.
	 * Automatically adds stuff like the altered equipment name or the enhancement description to the parameters.
	 * 
	 * @param builder The builder to generate the equipment item.
	 * @param enhancement The enhancment that should be applied.
	 * @param parameters The initial enhancement parameters.
	 * 
	 * @return The altered enhancement parameters.
	 * 
	 * @see WauzEquipmentEnhancer#enhanceEquipment(WauzEquipmentIdentifier, int)
	 */
	public static WauzEnhancementParameters enhanceEquipment(WauzEquipmentBuilder builder, WauzEnhancement enhancement, WauzEnhancementParameters parameters) {
		parameters = enhancement.enhanceEquipment(parameters);
		EquipmentType equipmentType = enhancement.getEquipmentType();
		
		int enhancementLevel = parameters.getEnhancementLevel();
		String enhancementName = enhancement.getEnhancementId();
		String enhancementSuffix = " of " + enhancementName + " + " + enhancementLevel;
		
		String enhancementLore = parameters.getEnhancementLore();
		if(equipmentType.equals(EquipmentType.WEAPON)) {
			builder.addEnhancementString("Enhancement:" + ChatColor.RED + " " + enhancementLore, enhancementSuffix);
		}
		else if(equipmentType.equals(EquipmentType.ARMOR)) {
			builder.addEnhancementString("Enhancement:" + ChatColor.RED + " " + enhancementLore, enhancementSuffix);
		}
		return parameters;
	}

}
