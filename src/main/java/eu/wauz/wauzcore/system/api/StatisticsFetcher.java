package eu.wauz.wauzcore.system.api;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

import eu.wauz.wauzcore.WauzCore;

public class StatisticsFetcher {

	private static WauzCore core = WauzCore.getInstance();
	
	private static DecimalFormat formatter = new DecimalFormat("#,###");
	
	private static String totalCustomEntitiesString;
	private static String totalPlayersString;
	private static String totalPlaytimeDaysString;
	
	public static void calculate() {
		totalCustomEntitiesString = getTotalCustomEntities();
		totalPlayersString = getTotalPlayers();
		totalPlaytimeDaysString = getTotalPlaytimeDays();
	}
	
	public static String getTotalCustomEntitiesString() {
		return totalCustomEntitiesString;
	}
	
	public static String getTotalPlayersString() {
		return totalPlayersString;
	}

	public static String getTotalPlaytimeDaysString() {
		return totalPlaytimeDaysString;
	}
	
	private static String getTotalCustomEntities() {
		int customEntities = 0;
		String statisticsPath = core.getDataFolder().getAbsolutePath().replace("WauzCore", "MythicMobs/Mobs/Wauzland");
		
		List<File> foldersToScan = new ArrayList<>();
		foldersToScan.addAll(Arrays.asList(new File(statisticsPath).listFiles()));
		
		while(!foldersToScan.isEmpty()) {
			for(File file : foldersToScan.get(0).listFiles())
				if(file.isDirectory())
					foldersToScan.add(file);
				else customEntities++;
			
			foldersToScan.remove(foldersToScan.get(0));
		}
		return new Integer(customEntities).toString();
	}

	private static String getTotalPlayers() {
		File statisticsFolder = new File(core.getDataFolder().getAbsolutePath().replace("plugins/WauzCore", "HubNexus/stats/"));
		return new Integer(statisticsFolder.list().length).toString();
	}
	
	private static String getTotalPlaytimeDays() {
		long playedHours = 0;
		String statisticsPath = core.getDataFolder().getAbsolutePath().replace("plugins/WauzCore", "HubNexus/stats/%uuid%.json");
		for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			File statisticsFile = new File(statisticsPath.replace("%uuid%", player.getUniqueId().toString()));
			playedHours += getPlayedHoursFromStatistics(statisticsFile);
		}
		return new Long(playedHours / 24).toString();
	}
	
	private File statisticsFile;
	
	private String characterString1 = "None";
	
	private String characterString2 = "None";
	
	private String characterString3 = "None";
	
	public StatisticsFetcher(OfflinePlayer offlinePlayer) {
		String statisticsPath = core.getDataFolder().getAbsolutePath().replace("plugins/WauzCore", "HubNexus/stats/%uuid%.json");
		statisticsFile = new File(statisticsPath.replace("%uuid%", offlinePlayer.getUniqueId().toString()));
		createCharacterStrings(offlinePlayer.getUniqueId().toString());
	}
	
	private void createCharacterStrings(String uuidString) {
		characterString1 = createCharacterString(uuidString, 1);
		characterString2 = createCharacterString(uuidString, 2);
		characterString3 = createCharacterString(uuidString, 3);
	}
	
	private String createCharacterString(String uuidString, int slot) {
		String characterString = "None";
		
		File playerDataFile = new File(core.getDataFolder(), "PlayerData/" + uuidString + "/char" + slot + ".yml");
		if(!playerDataFile.exists())
			return characterString;
		
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		if(!playerDataConfig.getBoolean("exists"))
			return characterString;
		
		String race = playerDataConfig.getString("race");
		String world = playerDataConfig.getString("pos.world");
		String level = playerDataConfig.getString("level");
		characterString = "Level " + level + " " + race + " on " + world;
		return characterString;
	}
	
	public String getCharacterString(int charSlotNumber) {
		switch(charSlotNumber) {
		case 1:
			return characterString1;
		case 2:
			return characterString2;
		case 3:
			return characterString3;
		default:
			return "None";
		}
	}
	
	public String getPlayedHoursString() {
		return formatter.format(getPlayedHoursFromStatistics(statisticsFile));
	}
	
	private static long getPlayedHoursFromStatistics(File file) {
		long playedHours = 0;
		try {
			String line = Files.readFirstLine(file, StandardCharsets.UTF_8);
			long playedTicks = line.contains("\"stat.playOneMinute\":")
				? Long.parseLong(StringUtils.substringBetween(line, "\"stat.playOneMinute\":", ","))
				: Long.parseLong(StringUtils.substringBetween(line, "\"minecraft:play_one_minute\":", ","));
			playedHours = playedTicks / 72000;
		} catch (Exception e) {
			//core.getLogger().info("PlayedHoursStatisticsError in " + file.getPath());
		}
		return playedHours;
	}
	
	public String getKilledMobsString() {
		return formatter.format(getKilledMobsFromStatistics(statisticsFile));
	}
	
	private static long getKilledMobsFromStatistics(File file) {
		long killedMobs = 0;
		try {
			String line = Files.readFirstLine(file, StandardCharsets.UTF_8);
			killedMobs = line.contains("\"stat.mobKills\":")
				? Long.parseLong(StringUtils.substringBetween(line, "\"stat.mobKills\":", ","))
				: Long.parseLong(StringUtils.substringBetween(line, "\"minecraft:mob_kills\":", ","));
		} catch (Exception e) {
			//core.getLogger().info("KilledMobsStatisticsError in " + file.getPath());
		}
		return killedMobs;
	}
	
	public String getWalkedMetresString() {
		return formatter.format(getWalkedMetresFromStatistics(statisticsFile));
	}
	
	private static long getWalkedMetresFromStatistics(File file) {
		long walkedMetres = 0;
		try {
			String line = Files.readFirstLine(file, StandardCharsets.UTF_8);
			long walkedCentimetres = line.contains("\"stat.walkOneCm\":")
				? Long.parseLong(StringUtils.substringBetween(line, "\"stat.walkOneCm\":", ","))
				: Long.parseLong(StringUtils.substringBetween(line, "\"minecraft:walk_one_cm\":", ","));
			walkedMetres = walkedCentimetres / 100;
		} catch (Exception e) {
			//core.getLogger().info("WalkedMetresStatisticsError in " + file.getPath());
		}
		return walkedMetres;
	}
	
}
