package eu.wauz.wauzcore.data;

import java.util.Collection;
import java.util.List;

import eu.wauz.wauzcore.data.api.BestiaryConfigurationUtils;
import eu.wauz.wauzcore.mobs.bestiary.WauzBestiaryCategory;

/**
 * Configurator to fetch or modify data from the Bestiary.yml files.
 * 
 * @author Wauzmons
 */
public class BestiaryConfigurator extends BestiaryConfigurationUtils {
	
// Bestiary Files
	
	/**
	 * @param category The bestiary category.
	 * 
	 * @return The list of all species names of a given category.
	 */
	public static List<String> getSpeciesNameList(WauzBestiaryCategory category) {
		return BestiaryConfigurationUtils.getSpeciesNameList(category.toString());
	}
	
// Bestiary Entries
	
	/**
	 * @param category The bestiary category.
	 * @param species The name of the species.
	 * 
	 * @return The description of the species.
	 */
	public static String getSpeciesDescription(WauzBestiaryCategory category, String species) {
		return speciesConfigGetString(category.toString(), species, "description");
	}
	
	/**
	 * @param category The bestiary category.
	 * @param species The name of the species.
	 * 
	 * @return A list of entry names for the species.
	 */
	public static Collection<String> getSpeciesEntries(WauzBestiaryCategory category, String species) {
		return speciesConfigGetKeys(category.toString(), species, "entries");
	}
	
	/**
	 * @param category The bestiary category.
	 * @param species The name of the species.
	 * @param entry The name of the entry.
	 * 
	 * @return The mythic mob name of the entry.
	 */
	public static String getEntryMobName(WauzBestiaryCategory category, String species, String entry) {
		return speciesConfigGetString(category.toString(), species, "entries." + entry + ".mob");
	}
	
	/**
	 * @param category The bestiary category.
	 * @param species The name of the species.
	 * @param entry The name of the entry.
	 * 
	 * @return The mob description of the entry.
	 */
	public static String getEntryMobDescription(WauzBestiaryCategory category, String species, String entry) {
		return speciesConfigGetString(category.toString(), species, "entries." + entry + ".description");
	}

}
