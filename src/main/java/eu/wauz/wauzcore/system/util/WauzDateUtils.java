package eu.wauz.wauzcore.system.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * An util class for calculating dates.
 * 
 * @author Wauzmons
 */
public class WauzDateUtils {
	
	/**
	 * @return A calendar with current system date.
	 */
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar;
	}
	
	/**
	 * @return The seconds of the current minute.
	 */
	public static int getSeconds() {
		return getCalendar().get(Calendar.SECOND);
	}
	
	/**
	 * @return The current date as number in yyyyMMdd format.
	 */
	public static long getDateLong() {
		return Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(new Date()));
	}
	
	/**
	 * @param millis A timestamp.
	 * 
	 * @return A string that shows the server date of the timestamp.
	 */
	public static String formatTime(long millis) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(millis));
	}
	
	/**
	 * @param millis A timestamp.
	 * 
	 * @return A string that shows how much time has passed, since the timestamp.
	 */
	public static String formatTimeSince(long millis) {
		return formatDaysHoursMins(System.currentTimeMillis() + 60000 - millis);
	}
	
	/**
	 * @param millis A timestamp.
	 * 
	 * @return A string that shows how much time is left, till the timestamp is reached.
	 */
	public static String formatTimeUntil(long millis) {
		return formatDaysHoursMins(millis + 60000 - System.currentTimeMillis());
	}
	
	/**
	 * @param millis A millisecond count.
	 * 
	 * @return The milliseconds converted to readable time string.
	 */
	public static String formatDaysHoursMins(long millis) {
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		
		String hm = String.format("%02dh %02dm",
				hours - TimeUnit.DAYS.toHours(days),
	            minutes - TimeUnit.HOURS.toMinutes(hours));
		
		return days + "d " + hm;
	}
	
	/**
	 * @param millis  A millisecond count.
	 * 
	 * @return The milliseconds converted to readable time string.
	 */
	public static String formatMinsSecs(long millis) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		
		return String.format("%d:%02d", minutes, seconds - TimeUnit.MINUTES.toSeconds(minutes));
	}
	
	/**
	 * @return A string that shows the official server time.
	 */
	public static String getServerTime() {
		return new SimpleDateFormat("HH:mm").format(new Date());
	}
	
	/**
	 * @return The current season. (YEAR-QUARTER)
	 */
	public static String getSurvivalSeason() {
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) / 3 + 1);
	}
	
	/**
	 * @return The current season as int. (YEARQUARTER)
	 */
	public static int getSurvivalSeasonInteger() {
		Calendar calendar = getCalendar();
		return (calendar.get(Calendar.YEAR) * 10) + (calendar.get(Calendar.MONTH) / 3 + 1);
	}
	
	/**
	 * @return A string that shows how much time is left to the next season.
	 */
	public static String getTimeTillNextSeason() {
		return formatTimeUntil(getFirstDayOfNextQuarter());
	}
	
	/**
	 * @return The timestamp of the first month of next quarter.
	 */
	public static long getFirstDayOfNextQuarter() {
		LocalDateTime dateTime = LocalDateTime.now();
		int year = dateTime.getMonth().getValue() + 3 > 12 ? dateTime.getYear() + 1 : dateTime.getYear();
		dateTime = dateTime
				.with(dateTime.getMonth().plus(3).firstMonthOfQuarter())
				.withYear(year)
				.with(TemporalAdjusters.firstDayOfMonth())
				.withHour(0).withMinute(0).withSecond(0);
		return dateTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
	}

}
