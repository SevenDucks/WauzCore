package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.mobs.pets.WauzPetRarity;
import eu.wauz.wauzcore.mobs.pets.WauzPetStat;

/**
 * Configurator to fetch or modify data from the Pets.yml.
 * 
 * @author Wauzmons
 */
public class PetConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @return The keys of all pets.
	 */
	public static List<String> getPetKeys() {
		return new ArrayList<>(mainConfigGetKeys("Pets", "pets"));
	}
	
	/**
	 * @param petKey The key of the pet.
	 * 
	 * @return The name of the pet.
	 */
	public static String getName(String petKey) {
		return mainConfigGetString("Pets", "pets." + petKey + ".mob");
	}
	
	/**
	 * @param petKey The key of the pet.
	 * 
	 * @return The category of the pet.
	 */
	public static String getCategory(String petKey) {
		return mainConfigGetString("Pets", "pets." + petKey + ".category");
	}
	
	/**
	 * @param petKey The key of the pet.
	 * 
	 * @return The rarity of the pet.
	 */
	public static WauzPetRarity getRarity(String petKey) {
		return WauzPetRarity.valueOf(mainConfigGetString("Pets", "pets." + petKey + ".rarity"));
	}
	
// Pet Stats
	
	/**
	 * @return The keys of all pet stats.
	 */
	public static List<String> getPetStatKeys() {
		return new ArrayList<>(mainConfigGetKeys("Pets", "stats"));
	}
	
	/**
	 * @param stat The key of the pet stat.
	 * 
	 * @return The short display name of the pet stat.
	 */
	public static String getPetStatName(String stat) {
		return mainConfigGetString("Pets", "stats." + stat + ".name");
	}
	
	/**
	 * @param stat The key of the pet stat.
	 * 
	 * @return The effect description of the pet stat.
	 */
	public static String getPetStatDescription(String stat) {
		return mainConfigGetString("Pets", "stats." + stat + ".description");
	}
	
// Breeding Levels
	
	/**
	 * @param level The breeding level.
	 * 
	 * @return The exp needed to reach the breeding level.
	 */
	public static int getBreedingLevelExp(int level) {
		return mainConfigGetInt("Pets", "levels." + level + ".exp");
	}
	
	/**
	 * @param level The breeding level.
	 * @param rarity The rarity of the pet to breed.
	 * 
	 * @return The breeding time in seconds.
	 */
	public static int getBreedingLevelTime(int level, WauzPetRarity rarity) {
		return mainConfigGetInt("Pets", "levels." + level + ".breedtime." + rarity.toString());
	}
	
// Stat Categories
	
	/**
	 * @param stat The key of the stat.
	 * 
	 * @return The categories of the stat.
	 */
	public static List<String> getStatCategories(WauzPetStat stat) {
		return mainConfigGetStringList("Pets", "stats." + stat + ".categories");
	}

}
