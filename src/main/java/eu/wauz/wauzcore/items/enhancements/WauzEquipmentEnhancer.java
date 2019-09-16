package eu.wauz.wauzcore.items.enhancements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import net.md_5.bungee.api.ChatColor;

public class WauzEquipmentEnhancer {
	
	private static Random random = new Random();
	
	private static Map<String, WauzEnhancement> weaponEnhancementMap = new HashMap<>();
	
	private static Map<String, WauzEnhancement> armorEnhancementMap = new HashMap<>();
	
	public static WauzEnhancement getEnhancement(String enhancementId) {
		WauzEnhancement enhancement = weaponEnhancementMap.get(enhancementId);
		if(enhancement == null) {
			enhancement = armorEnhancementMap.get(enhancementId);
		}
		return enhancement;
	}
	
	public static List<WauzEnhancement> getWeaponEnhancements() {
		return new ArrayList<WauzEnhancement>(weaponEnhancementMap.values());
	}
	
	public static List<WauzEnhancement> getArmorEnhancements() {
		return new ArrayList<WauzEnhancement>(armorEnhancementMap.values());
	}
	
	public static List<WauzEnhancement> getAllEnhancements() {
		List<WauzEnhancement> enhancements = new ArrayList<>();
		enhancements.addAll(weaponEnhancementMap.values());
		enhancements.addAll(armorEnhancementMap.values());
		return enhancements;
	}
	
	public static List<String> getWeaponEnhancementIds() {
		return new ArrayList<>(weaponEnhancementMap.keySet());
	}
	
	public static List<String> getArmorEnhancementIds() {
		return new ArrayList<>(armorEnhancementMap.keySet());
	}
	
	public static List<String> getAllEnhancementIds() {
		List<String> enhancementIds = new ArrayList<>();
		enhancementIds.addAll(weaponEnhancementMap.keySet());
		enhancementIds.addAll(armorEnhancementMap.keySet());
		return enhancementIds;
	}
	
	public static void registerEnhancement(WauzEnhancement enhancement) {
		EquipmentType equipmentType = enhancement.getEquipmentType();
		
		if(equipmentType.equals(EquipmentType.WEAPON))
			weaponEnhancementMap.put(enhancement.getEnhancementId(), enhancement);
		else if(equipmentType.equals(EquipmentType.ARMOR))
			armorEnhancementMap.put(enhancement.getEnhancementId(), enhancement);
	}
	
	public static void enhanceEquipment(WauzEquipmentIdentifier identifier) {
		int enhancementLevel = identifier.getEnhancementLevel();
		WauzEnhancementParameters parameters = new WauzEnhancementParameters(enhancementLevel);
		parameters.setItemMeta(identifier.getItemMeta());
		parameters.setLores(identifier.getLores());
		parameters.setMainStatString(identifier.getMainStatString());
		parameters.setAttackStat(identifier.getAttackStat());
		parameters.setDefenseStat(identifier.getDefenseStat());
		parameters.setDurabilityStat(identifier.getDurabilityStat());
		
		WauzEnhancement enhancement = null;
		EquipmentType equipmentType = identifier.getEquipmentType();
		if(equipmentType.equals(EquipmentType.WEAPON)) {
			List<WauzEnhancement> weaponEnhancements = getWeaponEnhancements();
			enhancement = weaponEnhancements.get(random.nextInt(weaponEnhancements.size()));
		}
		else if(equipmentType.equals(EquipmentType.ARMOR)) {
			List<WauzEnhancement> armorEnhancements = getArmorEnhancements();
			enhancement = armorEnhancements.get(random.nextInt(armorEnhancements.size()));
		}
		
		parameters = enhanceEquipment(enhancement, parameters);
		identifier.setItemMeta(parameters.getItemMeta());
		identifier.setLores(parameters.getLores());
		identifier.setMainStatString(parameters.getMainStatString());
		identifier.setAttackStat(parameters.getAttackStat());
		identifier.setDefenseStat(parameters.getDefenseStat());
		identifier.setDurabilityStat(parameters.getDurabilityStat());
	}
	
	public static WauzEnhancementParameters enhanceEquipment(WauzEnhancement enhancement, WauzEnhancementParameters parameters) {
		parameters = enhancement.enhanceEquipment(parameters);
		EquipmentType equipmentType = enhancement.getEquipmentType();
		
		String displayName = parameters.getItemMeta().getDisplayName();
		String enhancementName = enhancement.getEnhancementId();
		int enhancementLevel = parameters.getEnhancementLevel();
		parameters.getItemMeta().setDisplayName(displayName + " of " + enhancementName + " + " + enhancementLevel);
		
		String enhancementLore = parameters.getEnhancementLore();
		if(equipmentType.equals(EquipmentType.WEAPON))
			parameters.getLores().add("Enhancement:" + ChatColor.RED + " " + enhancementLore);
		else if(equipmentType.equals(EquipmentType.ARMOR))
			parameters.getLores().add("Enhancement:" + ChatColor.BLUE + " " + enhancementLore);
		
		return parameters;
	}

}
