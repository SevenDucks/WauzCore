package eu.wauz.wauzcore.system;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import eu.wauz.wauzcore.WauzCore;

/**
 * A class used to display system info and usage at the point of its creation.
 * 
 * @author Wauzmons
 */
public class SystemAnalytics {
	
	/**
	 * The operating system's name and architecture.
	 */
	private final String systemArchitecture;
	
	/**
	 * The amount of used player slots of the server.
	 */
	private final String playersOnline;
	
	/**
	 * The usage of the server's central processing unit.
	 */
	private final String cpuUsage;
	
	/**
	 * The usage of the server's rapid access memory.
	 */
	private final String ramUsage;
	
	/**
	 * The usage of the server's solid state drive.
	 */
	private final String ssdUsage;
	
	/**
	 * Creates a new system analytics instance, that will be instantly ready to read.
	 * Automatically generates system info and usage at the point of its creation.
	 */
	public SystemAnalytics() {
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		systemArchitecture = "[System: " + os.getName() + " " + os.getArch() + "]";
		
		int playersNow = Bukkit.getOnlinePlayers().size();
		int playersMax = Bukkit.getMaxPlayers();
		playersOnline = "[Players: " + playersNow + " / " + playersMax + "]";
		
		String processors = os.getAvailableProcessors() + " Proc, ";
		String loadAverage = os.getSystemLoadAverage() + " Load";
		cpuUsage =  "[CPU: " + processors + loadAverage + "]";
		
		String ramFree = getByteUnit(Runtime.getRuntime().freeMemory());
		String ramUsed = getByteUnit(Runtime.getRuntime().maxMemory());
		ramUsage = "[RAM: " + ramFree + " / " + ramUsed + "]";
		
		long spaceTotal = WauzCore.getInstance().getDataFolder().getTotalSpace();
		long spaceInUse = spaceTotal - WauzCore.getInstance().getDataFolder().getFreeSpace();
		ssdUsage = "[SSD: " + getByteUnit(spaceInUse) + " / " + getByteUnit(spaceTotal) + "]";
	}
	
	/**
	 * Prints information about the system to the requestor.
	 * 
	 * @param sender The person who requested the analytics.
	 */
	public void print(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_RED + systemArchitecture);
		sender.sendMessage(ChatColor.DARK_RED + playersOnline);
		sender.sendMessage(ChatColor.DARK_RED + ramUsage);
		sender.sendMessage(ChatColor.DARK_RED + cpuUsage);
		sender.sendMessage(ChatColor.DARK_RED + ssdUsage);
	}
	
	/**
	 * Formats byte units for displaying in the system analytics.
	 * 
	 * @param bytes The raw amount of bytes.
	 * @return A formatted string with byte unit.
	 */
	private String getByteUnit(long bytes) {
		if (bytes < 1024) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(1024));
		return String.format("%.0f %sB", bytes / Math.pow(1024, exp), ("KMGTPE").charAt(exp - 1));
	}

	/**
	 * @return The operating system's name and architecture.
	 */
	public String getSystemArchitecture() {
		return systemArchitecture;
	}

	/**
	 * @return The amount of used player slots of the server.
	 */
	public String getPlayersOnline() {
		return playersOnline;
	}

	/**
	 * @return The usage of the server's central processing unit.
	 */
	public String getCpuUsage() {
		return cpuUsage;
	}

	/**
	 * @return The usage of the server's rapid access memory.
	 */
	public String getRamUsage() {
		return ramUsage;
	}

	/**
	 * @return The usage of the server's solid state drive.
	 */
	public String getSsdUsage() {
		return ssdUsage;
	}

}
