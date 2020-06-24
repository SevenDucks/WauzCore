package eu.wauz.wauzcore.system.economy;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.CurrencyConfigurator;

/**
 * A category of a currency.
 * 
 * @author Wauzmons
 * 
 * @see WauzCurrency
 */
public class WauzCurrencyCategory {

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
	public void addCurrency(WauzCurrency currency) {
		currencies.add(currency);
	}
	
}
