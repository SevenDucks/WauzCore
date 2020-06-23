package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Currencies.yml.
 * 
 * @author Wauzmons
 */
public class CurrencyConfigurator extends GlobalConfigurationUtils {
	
// Categories

	/**
	 * @return The keys of all categories.
	 */
	public static List<String> getAllCurrencyCategoryKeys() {
		return new ArrayList<>(mainConfigGetKeys("Currencies", null));
	}
	
	/**
	 * @param category The key of the category.
	 * 
	 * @return The display name of the category.
	 */
	public static String getCurrencyCategoryName(String category) {
		return mainConfigGetString("Currencies", category + ".name");
	}
	
// Factions
	
	/**
	 * @param category The key of the category.
	 * 
	 * @return The keys of the factions in the category.
	 */
	public static List<String> getCurrencyFactionKeys(String category) {
		return new ArrayList<>(mainConfigGetKeys("Currencies", category + ".factions"));
	}
	
	/**
	 * @param category The key of the category.
	 * @param faction The key of the faction in the category.
	 * 
	 * @return The display name of the faction's curreny.
	 */
	public static String getCurrencyFactionDisplayName(String category, String faction) {
		return mainConfigGetString("Currencies", category + ".factions." + faction + ".name");
	}
	
	/**
	 * @param category The key of the category.
	 * @param faction The key of the faction in the category.
	 * 
	 * @return The player config key of the faction's curreny.
	 */
	public static String getCurrencyFactionConfigName(String category, String faction) {
		return mainConfigGetString("Currencies", category + ".factions." + faction + ".key");
	}

}
