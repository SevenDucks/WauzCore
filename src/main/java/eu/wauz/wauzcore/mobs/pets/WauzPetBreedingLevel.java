package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	 * Initializes all pet breeding levels from 0 to Max and fills the internal pet breeding level list.
	 */
	public static void init() {
		for(int level = 0; level <= PetConfigurator.getBreedingLevelCount(); level++) {
			petBreedingLevels.add(new WauzPetBreedingLevel(level));
		}
	}
	
	/**
	 * Gets the breeding level with the given index or null, if it exceeds the maximum.
	 * 
	 * @param level The level to get.
	 * 
	 * @return The breeding level.
	 */
	public static WauzPetBreedingLevel getBreedingLevel(int level) {
		return level >= petBreedingLevels.size() ? null : petBreedingLevels.get(level);
	}
	
	/**
	 * Gets the list of experience milestones for the breeding skill.
	 * 
	 * @return The list of milestones.
	 */
	public static List<Long> getExperienceMilestones() {
		return petBreedingLevels.stream()
				.map(level -> Long.valueOf(level.getExp()))
				.collect(Collectors.toList());
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
		for(WauzPetRarity rarity : WauzPetRarity.getAllPetRarities()) {
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
	 * @param rarity The rarity of the pet to breed.
	 * 
	 * @return The breeding time in seconds.
	 */
	public int getTime(WauzPetRarity rarity) {
		return rarityTimeMap.get(rarity);
	}

}
