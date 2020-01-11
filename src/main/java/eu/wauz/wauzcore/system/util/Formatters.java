package eu.wauz.wauzcore.system.util;

import java.text.DecimalFormat;

/**
 * A collection of different decimal formatters.
 * 
 * @author Wauzmons
 */
public class Formatters {
	
	/**
	 * A formatter for displaying integer values with separated digits.
	 */
	public static final DecimalFormat INT = new DecimalFormat("#,###");
	
	/**
	 * A formatter for displaying float values with separated digits and 3 decimal places.
	 */
	public static final DecimalFormat DEC = new DecimalFormat("#,###.000");
	
	/**
	 * A formatter for displaying float values with 2 decimal places.
	 */
	public static final DecimalFormat DEC_SHORT = new DecimalFormat("0.00");

}
