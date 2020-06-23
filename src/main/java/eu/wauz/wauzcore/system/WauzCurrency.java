package eu.wauz.wauzcore.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.CurrencyConfigurator;

/**
 * A currency or reputation type, generated from a currency config file.
 * 
 * @author Wauzmons
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
	 * A category of a currency.
	 * 
	 * @author Wauzmons
	 */
	protected static class WauzCurrencyCategory {
		
		/**
		 * The name of the currency category.
		 */
		private String currencyCategoryName;
		
		/**
		 * The display name of the currency category.
		 */
		private String currencyCategoryDisplayName;
		
		/**
		 * All currencies in this category.
		 */
		private List<WauzCurrency> currencies = new ArrayList<>();
		
		/**
		 * Constructs a currency category, based on the currency file in the /WauzCore folder.
		 * Does NOT automatically load the currencies in it. 
		 * 
		 * @param currencyCategoryName The name of the currency category.
		 */
		public WauzCurrencyCategory(String currencyCategoryName) {
			this.currencyCategoryName = currencyCategoryName;
			this.currencyCategoryDisplayName = CurrencyConfigurator.getCurrencyCategoryName(currencyCategoryName);
		}

		/**
		 * @return The name of the currency category.
		 */
		public String getCurrencyCategoryName() {
			return currencyCategoryName;
		}

		/**
		 * @return The display name of the currency category.
		 */
		public String getCurrencyCategoryDisplayName() {
			return currencyCategoryDisplayName;
		}

		/**
		 * @return All currencies in this category.
		 */
		public List<WauzCurrency> getCurrencies() {
			return currencies;
		}
		
		/**
		 * Adds a currency to this category.
		 * 
		 * @param currency The currency to add.
		 */
		private void addCurrency(WauzCurrency currency) {
			currencies.add(currency);
		}
		
	}

}
