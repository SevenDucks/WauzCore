package eu.wauz.wauzcore.worlds;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.building.WorldSpawnGenerator;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzFileUtils;

/**
 * This class is used to setup a new world when a season starts.
 * 
 * @author Wauzmons
 */
public class SurvivalSeason {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * The creator for the world.
	 */
	private WorldCreator worldCreator;
	
	/**
	 * The directory of data packs to use. Can be null.
	 */
	private String dataPackDir;
	
	/**
	 * If a spawn circle should be created automatically.
	 */
	private boolean createSpawn;
	
	/**
	 * The name of the world.
	 */
	private String worldName;
	
	/**
	 * The current season.
	 */
	private String currentSeason;
	
	/**
	 * The season file of the world.
	 */
	private File seasonFile;
	
	/**
	 * Creates a season for the given world.
	 * 
	 * @param worldCreator The creator for the world.
	 * @param dataPackDir The directory of data packs to use. Can be null.
	 * @param createSpawn If a spawn circle should be created automatically.
	 */
	public SurvivalSeason(WorldCreator worldCreator, String dataPackDir, boolean createSpawn) {
		this.worldCreator = worldCreator;
		this.dataPackDir = dataPackDir;
		this.createSpawn = createSpawn;
		worldName = worldCreator.name();
		currentSeason = WauzDateUtils.getSurvivalSeason();
		seasonFile = getSeasonFile();
	}

	/**
	 * Loads the current Survival world or starts a new one,
	 * if it is outdated or has no Season.yml file.
	 */
	public void createWorld() {
		if(seasonFile.exists()) {
			FileConfiguration seasonConfig = YamlConfiguration.loadConfiguration(seasonFile);
			String season = seasonConfig.getString("season");
			if(currentSeason.equals(season)) {
				core.getLogger().info(worldName + ": Season is running! Loading world...");
			}
			else {
				core.getLogger().info(worldName + ": Season has ended! Creating new world...");
				String filePath = seasonFile.getParentFile().getAbsolutePath();
				WauzFileUtils.removeFilesRecursive(new File(filePath));
				WauzFileUtils.removeFilesRecursive(new File(filePath.replace(worldName, worldName + "_nether")));
				WauzFileUtils.removeFilesRecursive(new File(filePath.replace(worldName, worldName + "_the_end")));
			}
		}
		
		try {
			if(dataPackDir != null) {
				File target = new File(core.getServer().getWorldContainer(), worldName + "/datapacks");
				target.mkdirs();
				for(File dataPack : new File(core.getDataFolder(), dataPackDir).listFiles()) {
					Files.copy(dataPack, new File(target, dataPack.getName()));
				}
			}
			worldCreator.createWorld();
			initWorld();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tries to set up all properties of the current world, if needed.
	 * This includes the spawn, season file and gamerules.
	 * 
	 * @throws IOException Failed to save season file.
	 */
	private void initWorld() throws IOException {
		if(seasonFile.exists()) {
			return;
		}
		seasonFile.getParentFile().mkdir();
		FileConfiguration seasonConfig = YamlConfiguration.loadConfiguration(seasonFile);
		seasonConfig.set("season", currentSeason);
		seasonConfig.set("lastplot", 1);
		seasonConfig.save(seasonFile);
		
		World world = core.getServer().getWorld(worldName);
		Location spawnLocation = world.getHighestBlockAt(new Location(world, 0, 0, 0)).getLocation();
		if(createSpawn) {
			world.getWorldBorder().setCenter(spawnLocation);
			world.getWorldBorder().setSize(5000);
			WorldSpawnGenerator.createMainSpawnCircle(world, spawnLocation);
		}
		world.setSpawnLocation(spawnLocation.add(0, 1, 0));
		world.setGameRule(GameRule.DO_INSOMNIA, false);
		world.setGameRule(GameRule.MOB_GRIEFING, false);
		world.setGameRule(GameRule.SPAWN_RADIUS, 0);
		if(world.getName().contains("OneBlock")) {
			world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		}
	}
	
	/**
	 * Finds the Season.yml of the world.
	 * 
	 * @return The season file.
	 */
	private File getSeasonFile() {
		return new File(Bukkit.getWorldContainer(), worldName + "/Season.yml");
	}
	
}
