package eu.wauz.wauzdiscord;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzdiscord.data.DiscordConfigurator;

/**
 * Listens to log records from Bukkit, to forward them to Discord.
 * 
 * @author Wauzmons
 */
public class WauzLogFilter implements Filter {

	/**
	 * If the filter should accept log records.
	 */
	private boolean isOpen = true;
	
	/**
	 * The list of log entries, that shouldn't be forwarded to Discord.
	 */
	private List<String> ignoredMessages = new ArrayList<>();
	
	/**
	 * A date format for displaying log timestamps.
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Creates a log filter for all levels above trace.
	 * Sends a notification to the log channel.
	 */
	public WauzLogFilter() {
		ignoredMessages = DiscordConfigurator.getIgnoredLogMessages();
		
		String coreVersion = "Running WauzCore v" + WauzCore.getInstance().getDescription().getVersion();
		String coreVersionDisplay = System.lineSeparator() + ":nazar_amulet: " + coreVersion;
		String embeddedMessage = ":speaker: Activated logging for " + WauzCore.getServerKey() + " " + coreVersionDisplay;
		WauzDiscord.getShiroDiscordBot().sendEmbedFromMinecraft(embeddedMessage, Color.BLUE, true);
	}
	
	/**
	 * Closes the filter, to deny any log records from now on.
	 */
	public void close() {
		isOpen = false;
	}
	
	/**
	 * Forwards the received log record to Discord.
	 * 
	 * @param logEvent The log record from Bukkit.
	 */
	@Override
	public Result filter(LogEvent logEvent) {
		if(!isOpen) {
			return null;
		}
		String message = logEvent.getMessage().getFormattedMessage();
		for(String ignored : ignoredMessages) {
			if(StringUtils.contains(message, ignored)) {
				return null;
			}
		}
		
		String date = dateFormat.format(logEvent.getTimeMillis());
		String level = " [" + logEvent.getLevel().name() + "] ";
		WauzDiscord.getShiroDiscordBot().sendMessageFromMinecraft(date + level + message, true);
		return null;
	}

// Everything starting from here is useless.
	
	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object... arg4) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, Object arg3, Throwable arg4) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, Message arg3, Throwable arg4) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9, Object arg10) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9, Object arg10, Object arg11) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result getOnMatch() {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public Result getOnMismatch() {
		return null;
	}
	
	/**
	 * Unused implmentation.
	 */
	@Override
	public State getState() {
		return null;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public void initialize() {
		
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public boolean isStarted() {
		return false;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public boolean isStopped() {
		return false;
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public void start() {
		
	}

	/**
	 * Unused implmentation.
	 */
	@Override
	public void stop() {
		
	}

}
