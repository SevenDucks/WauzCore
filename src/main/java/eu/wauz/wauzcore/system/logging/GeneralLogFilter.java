package eu.wauz.wauzcore.system.logging;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;

/**
 * A log filter to exclude unnecessary messages.
 * 
 * @author Wauzmons
 */
public class GeneralLogFilter extends AbstractLogFilter {
	
	/**
	 * The list of loggers, that shouldn't be allowed to send low level messages.
	 */
	private List<String> ignoredLoggers = Arrays.asList(
			"net.dv8tion.jda",
			"org.reflections",
			"org.apache.ftpserver");

	/**
	 * Applies the filter to the given log event.
	 * 
	 * @param logEvent The log event.
	 * 
	 * @return The filter result.
	 */
	@Override
	public Result apply(LogEvent logEvent) {
		for(String loggerName : ignoredLoggers) {
			if(logEvent.getLoggerName().startsWith(loggerName) && logEvent.getLevel().isLessSpecificThan(Level.INFO)) {
				return Result.DENY;
			}
		}
		return Result.NEUTRAL;
	}

}
