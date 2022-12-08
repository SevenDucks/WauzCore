package eu.wauz.wauzcore.system.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.bukkit.ChatColor;

/**
 * An util class to format numbers.
 * 
 * @author Wauzmons
 */
public class Formatters {
	
	/**
	 * Symbols to use in the formatters, based on standards in the US locale.
	 */
	private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.US);
	
	/**
	 * A formatter for displaying integer values with separated digits.
	 */
	public static final DecimalFormat INT = new DecimalFormat("#,###", SYMBOLS);
	
	/**
	 * A formatter for displaying integer values with at least three digits.
	 */
	public static final DecimalFormat INT_THREE = new DecimalFormat("000", SYMBOLS);
	
	/**
	 * A formatter for displaying float values with separated digits and 3 decimal places.
	 */
	public static final DecimalFormat DEC = new DecimalFormat("#,###.000", SYMBOLS);
	
	/**
	 * A formatter for displaying float values with 2 decimal places.
	 */
	public static final DecimalFormat DEC_SHORT = new DecimalFormat("0.00", SYMBOLS);
	
	/**
	 * Formats a coin value, by dividing it into silver, gold and crystal coins.
	 * 
	 * @param coins The raw amount of coins.
	 * 
	 * @return The formatted coin string.
	 */
	public static String formatCoins(long coins) {
		String formattedCoins = "";
		String[] coinParts = INT.format(coins).split(",");
		int lastIndex = coinParts.length - 1;
		
		for(int index = 0; index <= lastIndex; index++) {
			String coinPart = coinParts[index];
			
			if(lastIndex > 1 && index < lastIndex - 1) {
				formattedCoins += ChatColor.AQUA + coinPart + ChatColor.WHITE + ",";
			}
			else if(lastIndex > 0 && index < lastIndex) {
				formattedCoins += ChatColor.GOLD + coinPart + ChatColor.WHITE + ",";
			}
			else {
				formattedCoins += ChatColor.GRAY + coinPart;
			}
		}
		return formattedCoins;
	}

}
