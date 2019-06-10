package eu.wauz.wauzcore.system.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WauzDateUtils {
	
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar;
	}
	
	public static long getDateLong() {
		return Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(new Date()));
	}
	
	public static String formatTimeSince(long millis) {
		return formatDaysHoursMins(System.currentTimeMillis() + 60000 - millis);
	}
	
	public static String formatTimeUntil(long millis) {
		return formatDaysHoursMins(millis + 60000 - System.currentTimeMillis());
	}
	
	public static String formatDaysHoursMins(long millis) {
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		
		String hm = String.format("%02dh %02dm",
				hours - TimeUnit.DAYS.toHours(days),
	            minutes - TimeUnit.HOURS.toMinutes(hours));
		
		return days + "d " + hm;
	}
	
	public static String getServerTime() {
		return new SimpleDateFormat("HH:mm").format(new Date());
	}
	
	public static String getSurvivalSeason() {
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) / 3 + 1);
	}
	
	public static int getSurvivalSeasonInteger() {
		Calendar calendar = getCalendar();
		return (calendar.get(Calendar.YEAR) * 10) + (calendar.get(Calendar.MONTH) / 3 + 1);
	}
	
	public static String getTimeTillNextSeason() {
		return formatTimeUntil(getFirstDayOfQuarter());
	}
	
	public static long getFirstDayOfQuarter() {
		LocalDateTime dateTime = LocalDateTime.now();
		dateTime = dateTime
				.with(dateTime.getMonth().plus(3).firstMonthOfQuarter())
				.with(TemporalAdjusters.firstDayOfMonth())
				.withHour(0).withMinute(0).withSecond(0);
		return dateTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
	}

}
