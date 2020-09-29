package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.system.pets.WauzPetRarity;

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
		return new ArrayList<>(mainConfigGetKeys("Pets", null));
	}
	
	/**
	 * @param petKey The key of the pet.
	 * 
	 * @return The name of the pet.
	 */
	public static String getName(String petKey) {
		return mainConfigGetString("Pets", petKey + ".name");
	}
	
	/**
	 * @param petKey The key of the pet.
	 * 
	 * @return The category of the pet.
	 */
	public static String getCategory(String petKey) {
		return mainConfigGetString("Pets", petKey + ".category");
	}
	
	/**
	 * @param petKey The key of the pet.
	 * 
	 * @return The rarity of the pet.
	 */
	public static WauzPetRarity getRarity(String petKey) {
		return WauzPetRarity.valueOf(mainConfigGetString("Pets", petKey + ".rarity"));
	}

}
