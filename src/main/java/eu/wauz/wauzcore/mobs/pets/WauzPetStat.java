package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.data.PetConfigurator;

/**
 * A pet stat, generated from the pet config file.
 * 
 * @author Wauzmons
 */
public class WauzPetStat {
	
	/**
	 * A list of all pet stats.
	 */
	private static List<WauzPetStat> petStats = new ArrayList<>();
	
	/**
	 * A map of all pet stats, indexed by key.
	 */
	private static Map<String, WauzPetStat> petStatMap = new HashMap<>();
	
	/**
	 * Initializes all pet stats and fills the internal pet stat map.
	 * 
	 * @see PetConfigurator#getPetStatKeys()
	 */
	public static void init() {
		for(String key : PetConfigurator.getPetStatKeys()) {
			WauzPetStat stat = new WauzPetStat(key);
			petStats.add(stat);
			petStatMap.put(key, stat);
		}
	}
	
	/**
	 * @param petStatKey A pet stat key.
	 * 
	 * @return The pet stat with that key.
	 */
	public static WauzPetStat getPetStat(String petStatKey) {
		return petStatMap.get(petStatKey);
	}
	
	/**
	 * @return A list of all pet stat keys.
	 */
	public static List<String> getAllPetStatKeys() {
		return new ArrayList<>(petStatMap.keySet());
	}
	
	/**
	 * @return A list of all pet stats.
	 */
	public static List<WauzPetStat> getAllPetStats() {
		return new ArrayList<>(petStats);
	}
	
	/**
	 * The key of the pet stat.
	 */
	private String key;
	
	/**
	 * The short display name of the pet stat.
	 */
	private String name;
	
	/**
	 * The effect description of the pet stat.
	 */
	private String description;
	
	/**
	 * Constructor for a new pet stat.
	 * 
	 * @param key The key of the pet stat.
	 */
	private WauzPetStat(String key) {
		this.key = key;
		this.name = PetConfigurator.getPetStatName(key);
		this.description = PetConfigurator.getPetStatDescription(key);
	}

	/**
	 * @return The key of the pet stat.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return The short display name of the pet stat.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The effect description of the pet stat.
	 */
	public String getDescription() {
		return description;
	}

}
