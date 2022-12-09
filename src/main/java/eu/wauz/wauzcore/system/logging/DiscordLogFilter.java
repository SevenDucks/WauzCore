package eu.wauz.wauzcore.system.logging;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.LogEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.DiscordConfigurator;

/**
 * A log filter to forward relevant messages to Discord.
 * 
 * @author Wauzmons
 */
public class DiscordLogFilter extends AbstractLogFilter {

	/**
	 * The list of log entries, that shouldn't be forwarded.
	 */
	private List<String> ignoredMessages = new ArrayList<>();
	
	/**
	 * A date format for displaying timestamps.
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Creates a new instance of the filter and sends a startup embed to Discord.
	 */
	public DiscordLogFilter() {
		ignoredMessages = DiscordConfigurator.getIgnoredLogMessages();
		
		String coreVersion = "Running WauzCore v" + WauzCore.getInstance().getDescription().getVersion();
		String coreVersionDisplay = System.lineSeparator() + ":nazar_amulet: " + coreVersion;
		String embeddedMessage = ":speaker: Activated logging for " + WauzCore.getServerKey() + " " + coreVersionDisplay;
		WauzCore.getDiscordBot().sendEmbedFromMinecraft(null, embeddedMessage, null, Color.BLUE, true);
	}
	
	/**
	 * Applies the filter to the given log event.
	 * 
	 * @param logEvent The log event.
	 * 
	 * @return The filter result.
	 */
	@Override
	public Result apply(LogEvent logEvent) {
		String message = logEvent.getMessage().getFormattedMessage();
		for(String ignored : ignoredMessages) {
			if(StringUtils.contains(message, ignored)) {
				return Result.NEUTRAL;
			}
		}
		String date = dateFormat.format(logEvent.getTimeMillis());
		String level = " [" + logEvent.getLevel().name() + "] ";
		WauzCore.getDiscordBot().sendMessageFromMinecraft(date + level + message, true);
		return Result.NEUTRAL;
	}

}
