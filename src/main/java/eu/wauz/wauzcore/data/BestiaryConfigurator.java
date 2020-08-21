package eu.wauz.wauzcore.data;

import java.util.Collection;
import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.mobs.bestiary.BestiaryCategory;

/**
 * Configurator to fetch or modify data from the Bestiary.yml files.
 * 
 * @author Wauzmons
 */
public class BestiaryConfigurator extends GlobalConfigurationUtils {
	
// Bestiary Files
	
	/**
	 * @param category The bestiary category.
	 * 
	 * @return The list of all species names of a given category.
	 */
	public static List<String> getSpeciesNameList(BestiaryCategory category) {
		return GlobalConfigurationUtils.getSpeciesNameList(category.toString());
	}
	
// Bestiary Entries
	
	/**
	 * @param category The bestiary category.
	 * @param species The name of the species.
	 * 
	 * @return A list of entry names for the species.
	 */
	public static Collection<String> getSpeciesEntries(BestiaryCategory category, String species) {
		return speciesConfigGetKeys(category.toString(), species, "entries");
	}
	
	/**
	 * @param category The bestiary category.
	 * @param species The name of the species.
	 * @param entry The name of the entry.
	 * 
	 * @return The name of the mythic mob bound to the entry.
	 */
	public static String getEntryMobName(BestiaryCategory category, String species, String entry) {
		return speciesConfigGetString(category.toString(), species, "entries." + entry + ".mob");
	}
	
	/**
	 * @param category The bestiary category.
	 * @param species The name of the species.
	 * @param entry The name of the entry.
	 * 
	 * @return The description of the entry.
	 */
	public static String getEntryMobDescription(BestiaryCategory category, String species, String entry) {
		return speciesConfigGetString(category.toString(), species, "entries." + entry + ".description");
	}

}
