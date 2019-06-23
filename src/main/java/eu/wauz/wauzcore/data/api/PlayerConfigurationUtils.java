package eu.wauz.wauzcore.data.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

public class PlayerConfigurationUtils {
	
	private static WauzCore core = WauzCore.getInstance();
	
// Interact with Player-Config

	private static File getPlayerDataFile(Player player, String path, Boolean characterSpecific) {
		String characterSlot;
		if(!characterSpecific && path.startsWith("char"))
			characterSlot = StringUtils.substringBefore(path, ".");
		else
			characterSlot = characterSpecific ? WauzPlayerDataPool.getPlayer(player).getSelectedCharacterSlot() : "global";
			
		File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		return new File(playerDirectory, characterSlot + ".yml");
	}
	
	private static File getPlayerDataFile(OfflinePlayer player, String path, Boolean characterSpecific) {
		String characterSlot;
		if(!characterSpecific && path.startsWith("char"))
			characterSlot = StringUtils.substringBefore(path, ".");
		else
			characterSlot = "global";
		
		File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		return new File(playerDirectory, characterSlot + ".yml");
	}
	
	private static String trimPlayerDataPath(String path) {
		return path.startsWith("char") ? StringUtils.substringAfter(path, ".") : path;
	}

	protected static void playerConfigSet(Player player, String path, Object value, Boolean characterSpecific) {
		try {
			File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
			FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
			
			playerDataConfig.set(trimPlayerDataPath(path), value);
			playerDataConfig.save(playerDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void playerConfigSet(OfflinePlayer player, String path, Object value, Boolean characterSpecific) {
		try {
			File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
			FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
			
			playerDataConfig.set(trimPlayerDataPath(path), value);
			playerDataConfig.save(playerDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static String playerConfigGetString(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getString(trimPlayerDataPath(path));
	}
	
	protected static String playerConfigGetString(OfflinePlayer player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getString(trimPlayerDataPath(path));
	}

	protected static Integer playerConfigGetInt(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getInt(trimPlayerDataPath(path));
	}

	protected static Long playerConfigGetLong(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getLong(trimPlayerDataPath(path));
	}
	
	protected static Long playerConfigGetLong(OfflinePlayer player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getLong(trimPlayerDataPath(path));
	}

	protected static Double playerConfigGetDouble(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getDouble(trimPlayerDataPath(path));
	}
	
	protected static Boolean playerConfigGetBoolean(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getBoolean(trimPlayerDataPath(path));
	}
	
	protected static Boolean playerConfigGetBoolean(OfflinePlayer player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		return playerDataConfig.getBoolean(trimPlayerDataPath(path));
	}
	
	protected static Location playerConfigGetLocation(Player player, String path, Boolean characterSpecific) {
		File playerDataFile = getPlayerDataFile(player, path, characterSpecific);
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		
		List<Double> coords = new ArrayList<Double>();
		for(String coord : playerDataConfig.getString(trimPlayerDataPath(path)).split(" "))
			coords.add(Double.parseDouble(coord));
		
//			List<Double> coords = (List<Double>) Arrays.asList(playerDataConfig.getString(characterSlot + path).split(" "))
//					.stream().map(coord -> new Double(Double.parseDouble(coord))).collect(Collectors.toList());	
		
		return new Location(Bukkit.getWorld(playerConfigGetString(player, "pos.world", true))
				, coords.get(0), coords.get(1), coords.get(2));
	}
	
// Interact with Player-Quest-Config
	
	private static File getPlayerQuestDataFile(Player player, String quest) {
		String characterSlot = WauzPlayerDataPool.getPlayer(player).getSelectedCharacterSlot();
		File playerQuestDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/" + characterSlot + "-quests/");
		playerQuestDirectory.mkdirs();
		return new File(playerQuestDirectory, quest + ".yml");
	}
	
	protected static void playerQuestConfigSet(Player player, String quest, String path, Object value) {
		try {
			File playerQuestDataFile = getPlayerQuestDataFile(player, quest);
			FileConfiguration playerQuestDataConfig = YamlConfiguration.loadConfiguration(playerQuestDataFile);
			
			playerQuestDataConfig.set(trimPlayerDataPath(path), value);
			playerQuestDataConfig.save(playerQuestDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static Integer playerQuestConfigGetInt(Player player, String quest, String path) {
		File playerQuestDataFile = getPlayerQuestDataFile(player, quest);
		FileConfiguration playerQuestDataConfig = YamlConfiguration.loadConfiguration(playerQuestDataFile);
		return playerQuestDataConfig.getInt(trimPlayerDataPath(path));
	}
	
	protected static Long playerQuestConfigGetLong(Player player, String quest, String path) {
		File playerQuestDataFile = getPlayerQuestDataFile(player, quest);
		FileConfiguration playerQuestDataConfig = YamlConfiguration.loadConfiguration(playerQuestDataFile);
		return playerQuestDataConfig.getLong(trimPlayerDataPath(path));
	}
	
// Interact with Guild-Config
	
	protected static List<String> getGuildUuidList() {
		List<String> guildUuidList = new ArrayList<>();
		for(File file : new File(core.getDataFolder(), "PlayerGuildData/").listFiles())
			guildUuidList.add(file.getName().replaceAll(".yml", ""));
		return guildUuidList;
	}
	
	protected static void deleteGuild(String guild) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		guildDataFile.delete();
	}
	
	protected static void guildConfigSet(String guild, String path, Object value) {
		try {
			File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
			FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
			
			guildDataConfig.set(trimPlayerDataPath(path), value);
			guildDataConfig.save(guildDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static String guildConfigGetString(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getString(path);
	}
	
	protected static List<String> guildConfigGetStringList(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getStringList(path);
	}
	
	protected static int guildConfigGetInt(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getInt(path);
	}
	
	protected static ItemStack guildConfigGetItemStack(String guild, String path) {
		File guildDataFile = new File(core.getDataFolder(), "PlayerGuildData/" + guild + ".yml");
		FileConfiguration guildDataConfig = YamlConfiguration.loadConfiguration(guildDataFile);	
		return guildDataConfig.getItemStack(path);
	}

}
