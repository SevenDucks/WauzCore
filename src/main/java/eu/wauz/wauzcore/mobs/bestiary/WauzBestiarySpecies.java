package eu.wauz.wauzcore.mobs.bestiary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.BestiaryConfigurator;

/**
 * A species in a bestiary category, generated from a bestiary config file.
 * 
 * @author Wauzmons
 * 
 * @see WauzBestiaryEntry
 */
public class WauzBestiarySpecies {
	
	/**
	 * A map of species lists, indexed by category.
	 */
	private static Map<WauzBestiaryCategory, List<WauzBestiarySpecies>> speciesMap = new HashMap<>();
	
	/**
	 * A map of bestiary entries, indexed by their full name.
	 */
	private static Map<String, WauzBestiaryEntry> entryMap = new HashMap<>();
	
	/**
	 * Initializes all species and their entries from the config and fills the internal species map.
	 * 
	 * @see BestiaryConfigurator#getSpeciesNameList(WauzBestiaryCategory)
	 */
	public static void init() {
		for(WauzBestiaryCategory category : WauzBestiaryCategory.values()) {
			List<WauzBestiarySpecies> categorySpecies = new ArrayList<>();
			for(String speciesName : BestiaryConfigurator.getSpeciesNameList(category)) {
				WauzBestiarySpecies species = new WauzBestiarySpecies(category, speciesName);
				for(WauzBestiaryEntry entry : species.getEntries()) {
					entryMap.put(entry.getEntryFullName(), entry);
				}
				categorySpecies.add(species);
			}
			speciesMap.put(category, categorySpecies);
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + entryMap.size() + " Bestiary Entries!");
	}
	
	/**
	 * @param category A bestiary category.
	 * 
	 * @return The list of species in that category.
	 */
	public static List<WauzBestiarySpecies> getSpecies(WauzBestiaryCategory category) {
		List<WauzBestiarySpecies> categorySpecies = speciesMap.get(category);
		return categorySpecies != null ? categorySpecies : new ArrayList<>();
	}
	
	/**
	 * @param entryName A full name of a bestiary entry.
	 * 
	 * @return The entry with that name.
	 */
	public static WauzBestiaryEntry getEntry(String entryName) {
		return entryMap.get(entryName);
	}
	
	/**
	 * The bestiary category of the species.
	 */
	private WauzBestiaryCategory speciesCategory;
	
	/**
	 * The name of the species.
	 */
	private String speciesName;
	
	/**
	 * The description of the species.
	 */
	private String speciesDescription;
	
	/**
	 * The bestiary entries belonging to the species.
	 */
	List<WauzBestiaryEntry> entries = new ArrayList<>();
	
	/**
	 * Constructs a species including entries, based on the bestiary file in the /WauzCore/BestiaryData/Category folder.
	 * 
	 * @param speciesName The name of the species.
	 */
	public WauzBestiarySpecies(WauzBestiaryCategory speciesCategory, String speciesName) {
		this.speciesCategory = speciesCategory;
		this.speciesName = speciesName;
		speciesDescription = BestiaryConfigurator.getSpeciesDescription(speciesCategory, speciesName);
		for(String entryName : BestiaryConfigurator.getSpeciesEntries(speciesCategory, speciesName)) {
			entries.add(new WauzBestiaryEntry(this, entryName));
		}
	}

	/**
	 * @return The bestiary category of the species.
	 */
	public WauzBestiaryCategory getSpeciesCategory() {
		return speciesCategory;
	}

	/**
	 * @return The name of the species.
	 */
	public String getSpeciesName() {
		return speciesName;
	}

	/**
	 * @return The description of the species.
	 */
	public String getSpeciesDescription() {
		return speciesDescription;
	}

	/**
	 * @return The bestiary entries belonging to the species.
	 */
	public List<WauzBestiaryEntry> getEntries() {
		return entries;
	}

}
