package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.data.PetConfigurator;

/**
 * A pet breeding level, generated from the pet config file.
 * 
 * @author Wauzmons
 */
public class WauzPetBreedingLevel {
	
	/**
	 * An ordered list of all pet breeding levels.
	 */
	private static List<WauzPetBreedingLevel> petBreedingLevels = new ArrayList<>();
	
	/**
	 * Initializes all pet breeding levels from 1-10 and fills the internal pet breeding level list.
	 */
	public static void init() {
		for(int level = 1; level <= 10; level++) {
			petBreedingLevels.add(new WauzPetBreedingLevel(level));
		}
	}
	
	/**
	 * The breeding level number.
	 */
	private int level;
	
	/**
	 * The exp needed to reach the breeding level.
	 */
	private int exp;
	
	/**
	 * The available breeding slots.
	 */
	private int slots;
	
	/**
	 * The breeding time in seconds, indexed by pet rarity.
	 */
	private Map<WauzPetRarity, Integer> rarityTimeMap = new HashMap<>();
	
	/**
	 * Constructor for a new pet breeding level.
	 * 
	 * @param level The breeding level number.
	 */
	private WauzPetBreedingLevel(int level) {
		this.level = level;
		this.exp = PetConfigurator.getBreedingLevelExp(level);
		this.slots = PetConfigurator.getBreedingLevelSlots(level);
		for(WauzPetRarity rarity : WauzPetRarity.values()) {
			rarityTimeMap.put(rarity, PetConfigurator.getBreedingLevelTime(level, rarity));
		}
	}

	/**
	 * @return The breeding level number.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return The exp needed to reach the breeding level.
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * @return The available breeding slots.
	 */
	public int getSlots() {
		return slots;
	}
	
	/**
	 * @param rarity The rarity of the pet to breed.
	 * 
	 * @return The breeding time in seconds.
	 */
	public int getTime(WauzPetRarity rarity) {
		return rarityTimeMap.get(rarity);
	}

}
