package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Titles.yml.
 * 
 * @author Wauzmons
 */
public class TitleConfigurator extends GlobalConfigurationUtils {

// General Parameters

	/**
	 * @return The keys of all titles.
	 */
	public static List<String> getAllTitleKeys() {
		return new ArrayList<>(mainConfigGetKeys("Titles", null));
	}
	
	/**
	 * @param titleKey The key of the title.
	 * 
	 * @return The chat display name of the title.
	 */
	public static String getTitleName(String titleKey) {
		return mainConfigGetString("Titles", titleKey + ".title");
	}
	
	/**
	 * @param titleKey The key of the title.
	 * 
	 * @return The required level of the title.
	 */
	public static int getTitleLevel(String titleKey) {
		return mainConfigGetInt("Titles", titleKey + ".level");
	}
	
	/**
	 * @param titleKey The key of the title.
	 * 
	 * @return The soulstone cost of the title.
	 */
	public static int getTitleCost(String titleKey) {
		return mainConfigGetInt("Titles", titleKey + ".cost");
	}
	
}
