package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Citizen.yml files.
 * 
 * @author Wauzmons
 */
public class CitizenConfigurator extends GlobalConfigurationUtils {
	
// Citizen Files

	/**
	 * @return The list of all citizen names.
	 */
	public static List<String> getCitizenNameList() {
		return GlobalConfigurationUtils.getCitizenNameList();
	}

// General Parameters
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The lines of text above the citizen's head.
	 */
	public static List<String> getNameLines(String citizen) {
		return citizenConfigGetStringList(citizen, "name");
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The location where the npc should be spawned.
	 */
	public static Location getLocation(String citizen) {
		String worldName = citizenConfigGetString(citizen, "world");
				
		List<Double> coords = new ArrayList<>();
		for(String coord : citizenConfigGetString(citizen, "location").split(" ")) {
			coords.add(Double.parseDouble(coord));
		}
		
		Location location = new Location(Bukkit.getWorld(worldName), coords.get(0), coords.get(1), coords.get(2));
		if(coords.size() == 5) {
			location.setYaw(coords.get(3).floatValue());
			location.setPitch(coords.get(4).floatValue());
		}
		return location;
	}
	
	/**
	 * @param citizen The identifier of a skin from <a href="https://mineskin.org">mineskin.org</a>
	 * 
	 * @return The id of the citizen's skin.
	 */
	public static int getSkinId(String citizen) {
		return citizenConfigGetInt(citizen, "skinid");
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return If the citizen is invisible.
	 */
	public static boolean isInvisible(String citizen) {
		return citizenConfigGetBoolean(citizen, "invisible");
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return If the citizen is burning.
	 */
	public static boolean isBurning(String citizen) {
		return citizenConfigGetBoolean(citizen, "burning");
	}
	
// Equipped Items
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The equipped mainhand item.
	 */
	public static ItemStack getEquippedMainhandItem(String citizen) {
		String materialName = citizenConfigGetString(citizen, "items.mainhand");
		return new ItemStack(StringUtils.isEmpty(materialName) ? Material.AIR : Material.getMaterial(materialName));
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The equipped offhand item.
	 */
	public static ItemStack getEquippedOffhandItem(String citizen) {
		String materialName = citizenConfigGetString(citizen, "items.offhand");
		return new ItemStack(StringUtils.isEmpty(materialName) ? Material.AIR : Material.getMaterial(materialName));
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The equipped helmet item.
	 */
	public static ItemStack getEquippedHelmetItem(String citizen) {
		String materialName = citizenConfigGetString(citizen, "items.helmet");
		return new ItemStack(StringUtils.isEmpty(materialName) ? Material.AIR : Material.getMaterial(materialName));
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The equipped chestplate item.
	 */
	public static ItemStack getEquippedChestplateItem(String citizen) {
		String materialName = citizenConfigGetString(citizen, "items.chestplate");
		return new ItemStack(StringUtils.isEmpty(materialName) ? Material.AIR : Material.getMaterial(materialName));
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The equipped leggings item.
	 */
	public static ItemStack getEquippedLeggingsItem(String citizen) {
		String materialName = citizenConfigGetString(citizen, "items.leggings");
		return new ItemStack(StringUtils.isEmpty(materialName) ? Material.AIR : Material.getMaterial(materialName));
	}
	
	/**
	 * @param citizen The name of the citizen.
	 * 
	 * @return The equipped boots item.
	 */
	public static ItemStack getEquippedBootsItem(String citizen) {
		String materialName = citizenConfigGetString(citizen, "items.boots");
		return new ItemStack(StringUtils.isEmpty(materialName) ? Material.AIR : Material.getMaterial(materialName));
	}
	
}
