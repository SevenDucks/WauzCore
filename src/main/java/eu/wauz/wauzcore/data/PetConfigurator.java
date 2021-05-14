package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.mobs.pets.WauzPetRarity;

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
		return WauzPetRarity.getPetRarity(mainConfigGetString("Pets", "pets." + petKey + ".rarity"));
	}
	
	/**
	 * @param petKey The key of the pet.
	 * 
	 * @return The random messages of the pet.
	 */
	public static List<String> getMessages(String petKey) {
		return mainConfigGetStringList("Pets", "pets." + petKey + ".messages");
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
	
	/**
	 * @param stat The key of the pet stat
	 * 
	 * @return The max stat growth per tier of the pet. 
	 */
	public static int getPetStatGrowth(String stat) {
		return mainConfigGetInt("Pets", "stats." + stat + ".statgrowth");
	}
	
	/**
	 * @param stat The key of the pet stat
	 * 
	 * @return The pet categories of the pet stat.
	 */
	public static List<String> getPetStatCategories(String stat) {
		return mainConfigGetStringList("Pets", "stats." + stat + ".categories");
	}
	
// Pet Rarities
	
	/**
	 * @return The keys of all pet rarities.
	 */
	public static List<String> getPetRarityKeys() {
		return new ArrayList<>(mainConfigGetKeys("Pets", "rarities"));
	}
	
	/**
	 * @param rarity The key of the pet rarity.
	 * 
	 * @return The name of the pet rarity.
	 */
	public static String getPetRarityName(String rarity) {
		return mainConfigGetString("Pets", "rarities." + rarity + ".name");
	}
	
	/**
	 * @param rarity The key of the pet rarity.
	 * 
	 * @return The color of the pet rarity.
	 */
	public static ChatColor getPetRarityColor(String rarity) {
		return ChatColor.valueOf(mainConfigGetString("Pets", "rarities." + rarity + ".color"));
	}
	
	/**
	 * @param rarity The key of the pet rarity.
	 * 
	 * @return The spawn egg material of the pet rarity.
	 */
	public static Material getPetRarityMaterial(String rarity) {
		return Material.valueOf(mainConfigGetString("Pets", "rarities." + rarity + ".material"));
	}
	
// Breeding Levels
	
	/**
	 * @return The count of breeding levels.
	 */
	public static int getBreedingLevelCount() {
		return mainConfigGetKeys("Pets", "levels").size();
	}
	
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

}
