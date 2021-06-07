package eu.wauz.wauzcore.system.logging;

import org.apache.logging.log4j.LogManager;

/**
 * Manages active log filters.
 * 
 * @author Wauzmons
 *
 * @see AbstractLogFilter
 */
public class LogFilterManager {
	
	/**
	 * A log filter to exclude unnecessary messages.
	 */
	private static AbstractLogFilter generalFilter;
	
	/**
	 * A log filter to forward relevant messages to Discord.
	 */
	private static AbstractLogFilter discordFilter;
	
	/**
	 * Enables the general log filter.
	 */
	public static void enableGeneralFilter() {
		generalFilter = new GeneralLogFilter();
		addFilter(generalFilter);
	}
	
	/**
	 * Disables the general log filter.
	 */
	public static void disableGeneralFilter() {
		if(generalFilter != null) {
			generalFilter.close();
		}
	}
	
	/**
	 * Enables the Discord log filter.
	 */
	public static void enableDiscordFilter() {
		discordFilter = new DiscordLogFilter();
		addFilter(discordFilter);
	}
	
	/**
	 * Disables the Discord log filter.
	 */
	public static void disableDiscordFilter() {
		if(discordFilter != null) {
			discordFilter.close();
		}
	}
	
	/**
	 * Adds the given log filter.
	 * 
	 * @param filter The filter.
	 */
	private static void addFilter(AbstractLogFilter filter) {
		((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(filter);
	}

}
