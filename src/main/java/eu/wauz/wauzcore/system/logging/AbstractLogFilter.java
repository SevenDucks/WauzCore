package eu.wauz.wauzcore.system.logging;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

/**
 * An abstract log filter.
 * 
 * @author Wauzmons
 */
public abstract class AbstractLogFilter extends AbstractFilter {
	
	/**
	 * If the filter should be applied to log records.
	 */
	private boolean isOpen = true;
	
	/**
	 * Closes the filter, to skip any log records from now on.
	 */
	public void close() {
		isOpen = false;
	}
	
	/**
	 * Applies the filter to the given log event, when opened.
	 * 
	 * @param logEvent The log event.
	 * 
	 * @return The filter result.
	 */
	@Override
	public Result filter(LogEvent logEvent) {
		return isOpen ? apply(logEvent) : Result.NEUTRAL;
	}
	
	/**
	 * Applies the filter to the given log event.
	 * 
	 * @param logEvent The log event.
	 * 
	 * @return The filter result.
	 */
	public abstract Result apply(LogEvent logEvent);

}
