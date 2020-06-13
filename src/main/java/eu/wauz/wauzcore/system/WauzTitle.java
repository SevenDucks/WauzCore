package eu.wauz.wauzcore.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.TitleConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;

/**
 * A player title, generated from a title config file.
 * 
 * @author Wauzmons
 */
public class WauzTitle {
	
	/**
	 * A map of titles, indexed by name.
	 */
	private static Map<String, WauzTitle> titleMap = new HashMap<>();
	
	/**
	 * Initializes all titles from the config and fills the internal title map.
	 * 
	 * @see TitleConfigurator#getAllTitleKeys()
	 */
	public static void init() {
		for(String titleName : TitleConfigurator.getAllTitleKeys()) {
			titleMap.put(titleName, new WauzTitle(titleName));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + titleMap.size() + " Titles!");
	}
	
	/**
	 * @param titleName A title name.
	 * 
	 * @return The title with that name.
	 */
	public static WauzTitle getTitle(String titleName) {
		return titleMap.get(titleName);
	}
	
	/**
	 * @param player The player to get the title of.
	 * 
	 * @return The display name of the player's title.
	 */
	public static String getTitle(Player player) {
		String titleName = PlayerConfigurator.getCharacterTitle(player);
		WauzTitle title = titleMap.get(titleName);
		if(title != null) {
			return title.getTitleDisplayName();
		}
		if(StringUtils.equals("class", titleName)) {
			return PlayerConfigurator.getCharacterClass(player);
		}
		else {
			return "";
		}
	}
	
	/**
	 * @return A list of all titles.
	 */
	public static List<WauzTitle> getAllTitles() {
		return new ArrayList<>(titleMap.values());
	}
	
	/**
	 * @return The count of all titles.
	 */
	public static int getTitleCount() {
		return titleMap.size();
	}
	
	/**
	 * The key of the title.
	 */
	private String titleName;
	
	/**
	 * The chat display name of the title.
	 */
	private String titleDisplayName;
	
	/**
	 * The required level of the title.
	 */
	private int titleLevel;
	
	/**
	 * The soulstone cost of the title.
	 */
	private int titleCost;
	
	/**
	 * Constructs a title, based on the title file in the /WauzCore folder.
	 * 
	 * @param titleName The key of the title.
	 */
	public WauzTitle(String titleName) {
		this.titleName = titleName;
		this.titleDisplayName = TitleConfigurator.getTitleName(titleName);
		this.titleLevel = TitleConfigurator.getTitleLevel(titleName);
		this.titleCost = TitleConfigurator.getTitleCost(titleName);
	}

	/**
	 * @return The key of the title.
	 */
	public String getTitleName() {
		return titleName;
	}

	/**
	 * @return The chat display name of the title.
	 */
	public String getTitleDisplayName() {
		return titleDisplayName;
	}

	/**
	 * @return The required level of the title.
	 */
	public int getTitleLevel() {
		return titleLevel;
	}

	/**
	 * @return The soulstone cost of the title.
	 */
	public int getTitleCost() {
		return titleCost;
	}
	
}
