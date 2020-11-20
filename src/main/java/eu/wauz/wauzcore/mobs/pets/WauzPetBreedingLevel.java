package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
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
		for(int level = 0; level <= WauzCore.MAX_BREEDING_SKILL; level++) {
			petBreedingLevels.add(new WauzPetBreedingLevel(level));
		}
	}
	
	/**
	 * Determines the breeding level, based on the amount of experience.
	 * 
	 * @param breedingExp The experience to determine the level for.
	 * 
	 * @return The current breeding level.
	 */
	public static WauzPetBreedingLevel getBreedingLevel(int breedingExp) {
		WauzPetBreedingLevel currentLevel = null;
		for(WauzPetBreedingLevel breedingLevel : petBreedingLevels) {
			if(breedingExp >= breedingLevel.getExp()) {
				currentLevel = breedingLevel;
			}
		}
		return currentLevel;
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
	 * @return The following breeding level.
	 */
	public WauzPetBreedingLevel getNextLevel() {
		return level + 1 >= petBreedingLevels.size() ? null : petBreedingLevels.get(level + 1);
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
