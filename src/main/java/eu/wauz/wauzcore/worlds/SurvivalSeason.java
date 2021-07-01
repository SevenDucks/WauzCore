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
	 * The creator for the world.
	 */
	private WorldCreator worldCreator;
	
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
	 * @param createSpawn If a spawn circle should be created automatically.
	 */
	public SurvivalSeason(WorldCreator worldCreator, boolean createSpawn) {
		this.worldCreator = worldCreator;
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
				WauzCore.getInstance().getLogger().info(worldName + ": Season is running! Loading world...");
			}
			else {
				WauzCore.getInstance().getLogger().info(worldName + ": Season has ended! Creating new world...");
				String filePath = seasonFile.getParentFile().getAbsolutePath();
				WauzFileUtils.removeFilesRecursive(new File(filePath));
				WauzFileUtils.removeFilesRecursive(new File(filePath.replace(worldName, worldName + "_nether")));
				WauzFileUtils.removeFilesRecursive(new File(filePath.replace(worldName, worldName + "_the_end")));
			}
		}
		worldCreator.createWorld();
		
		try {
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
		
		World world = WauzCore.getInstance().getServer().getWorld(worldName);
		Location spawnLocation = new Location(world, 0.5, 71, 0.5);
		if(createSpawn) {
			world.getWorldBorder().setSize(5000);
			WorldSpawnGenerator.createMainSpawnCircle(world, spawnLocation.clone().add(0, -1, 0));
		}
		world.setSpawnLocation(spawnLocation);
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
