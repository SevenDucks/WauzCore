package eu.wauz.wauzcore.system.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.CurrencyConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;

/**
 * A currency or reputation type, generated from a currency config file.
 * 
 * @author Wauzmons
 * 
 * @see WauzCurrencyCategory
 */
public class WauzCurrency {
	
	/**
	 * A map of currencies, indexed by name.
	 */
	private static Map<String, WauzCurrency> currencyMap = new HashMap<>();
	
	/**
	 * A list of all currency categories.
	 */
	private static List<WauzCurrencyCategory> currencyCategories = new ArrayList<>();
	
	/**
	 * Initializes all currencies from the configs and fills the internal currency maps.
	 * 
	 * @see CurrencyConfigurator#getAllCurrencyCategoryKeys()
	 * @see CurrencyConfigurator#getCurrencyFactionKeys(String)
	 */
	public static void init() {
		currencyMap.put("tokens", new WauzCurrency("tokens"));
		for(String currencyCategoryName : CurrencyConfigurator.getAllCurrencyCategoryKeys()) {
			WauzCurrencyCategory currencyCategory = new WauzCurrencyCategory(currencyCategoryName);
			currencyCategories.add(currencyCategory);
			
			for(String currencyName : CurrencyConfigurator.getCurrencyFactionKeys(currencyCategoryName)) {
				WauzCurrency currency = new WauzCurrency(currencyCategory, currencyName);
				currencyMap.put(currencyName, currency);
			}
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + currencyMap.size() + " Currencies!");
	}
	
	/**
	 * @param currencyName A currency name.
	 * 
	 * @return The currency with that name.
	 */
	public static WauzCurrency getCurrency(String currencyName) {
		return currencyMap.get(currencyName);
	}
	
	/**
	 * @return A list of all currency categories.
	 */
	public static List<WauzCurrencyCategory> getCurrencyCategories() {
		return currencyCategories;
	}
	
	/**
	 * The category of the currency.
	 */
	private WauzCurrencyCategory currencyCategory;
	
	/**
	 * The name of the currency.
	 */
	private String currencyName;
	
	/**
	 * The display name of the faction's curreny.
	 */
	private String currencyDisplayName;
	
	/**
	 * The player config key of the faction's curreny.
	 */
	private String currencyConfigName;

	/**
	 * Constructs a currency, based on the currency file in the /WauzCore folder.
	 * 
	 * @param currencyCategory The category of the currency.
	 * @param currencyName The name of the currency.
	 */
	public WauzCurrency(WauzCurrencyCategory currencyCategory, String currencyName) {
		currencyCategory.addCurrency(this);
		String categoryName = currencyCategory.getCurrencyCategoryName();
		
		this.currencyCategory = currencyCategory;
		this.currencyName = currencyName;
		this.currencyDisplayName = CurrencyConfigurator.getCurrencyFactionDisplayName(categoryName, currencyName);
		this.currencyConfigName = CurrencyConfigurator.getCurrencyFactionConfigName(categoryName, currencyName);
	}
	
	/**
	 * Constructs a global currency, without a category, with the given name.
	 * 
	 * @param currencyName The name of the currency.
	 */
	public WauzCurrency(String currencyName) {
		this.currencyName = currencyName;
		this.currencyDisplayName = StringUtils.capitalize(currencyName);
		this.currencyConfigName = currencyName;
	}
	
	/**
	 * @return The category of the currency.
	 */
	public WauzCurrencyCategory getCurrencyCategory() {
		return currencyCategory;
	}

	/**
	 * @return The name of the currency.
	 */
	public String getCurrencyName() {
		return currencyName;
	}

	/**
	 * @return The display name of the faction's curreny.
	 */
	public String getCurrencyDisplayName() {
		return currencyDisplayName;
	}

	/**
	 * @return The player config key of the faction's curreny.
	 */
	public String getCurrencyConfigName() {
		return currencyConfigName;
	}
	
	/**
	 * Gets how much of the currency a specific player owns.
	 * 
	 * @param player The player who owns the currency.
	 * 
	 * @return The owned currency amount.
	 */
	public long getCurrencyAmount(Player player) {
		return PlayerConfigurator.getCharacterCurrency(player, currencyConfigName);
	}
	
	/**
	 * Sets how much of the currency a specific player owns.
	 * 
	 * @param player The player who owns the currency.
	 * @param amount The new owned currency amount.
	 */
	public void setCurrencyAmount(Player player, long amount) {
		PlayerConfigurator.setCharacterCurrency(player, currencyConfigName, amount);
	}

}
