package eu.wauz.wauzstarter;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * This class is used to create a new world when a Survival season starts.
 * It should be used once a day on server startup.
 * 
 * @author Wauzmons
 * 
 * @see SeasonalSurvivalUtils
 */
public class SeasonalSurvivalManager {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzStarter core = WauzStarter.getInstance();
	
	/**
	 * The creator for the survival world.
	 */
	private WorldCreator worldCreator;
	
	/**
	 * If a spawn circle should be created automatically.
	 */
	private boolean createSpawn;
	
	/**
	 * The name of the survival world.
	 */
	private String worldName;
	
	/**
	 * The current survival season.
	 */
	private String currentSeason;
	
	/**
	 * The season file of the survival world.
	 */
	private File seasonFile;
	
	/**
	 * Creates a seasonal survival manager for the given world.
	 * 
	 * @param worldCreator The creator for the survival world.
	 * @param createSpawn If a spawn circle should be created automatically.
	 */
	public SeasonalSurvivalManager(WorldCreator worldCreator, boolean createSpawn) {
		this.worldCreator = worldCreator;
		this.createSpawn = createSpawn;
		worldName = worldCreator.name();
		currentSeason = getSurvivalSeason();
		seasonFile = getSurvivalSeasonFile();
	}

	/**
	 * Loads the current Survival world or starts a new one,
	 * if it is outdated or has no Season.yml file.
	 */
	public void generateSurvivalWorld() {
		if(seasonFile.exists()) {
			FileConfiguration seasonConfig = YamlConfiguration.loadConfiguration(seasonFile);
			String season = seasonConfig.getString("season");
			if(currentSeason.equals(season)) {
				core.getLogger().info(worldName + " World is up to date!");
			}
			else {
				core.getLogger().info(worldName + " World is outdated! Starting new one!");
				String filePath = seasonFile.getParentFile().getAbsolutePath();
				SeasonalSurvivalUtils.deleteWorld(new File(filePath));
				SeasonalSurvivalUtils.deleteWorld(new File(filePath.replace(worldName, worldName + "_nether")));
				SeasonalSurvivalUtils.deleteWorld(new File(filePath.replace(worldName, worldName + "_the_end")));
			}
		}
		worldCreator.createWorld();
		updateSurvivalWorld();
	}
	
	/**
	 * Tries to set up all properties of the current Survival world, if needed.
	 * This includes the spawn, season file and gamerules.
	 */
	private void updateSurvivalWorld() {
		try {		
			if(!seasonFile.exists()) {
				seasonFile.getParentFile().mkdir();
				FileConfiguration seasonConfig = YamlConfiguration.loadConfiguration(seasonFile);
				seasonConfig.set("season", currentSeason);
				seasonConfig.set("lastplot", 1);
				seasonConfig.save(seasonFile);
				
				World world = core.getServer().getWorld(worldName);
				Location spawnLocation = new Location(world, 0, 70, 0);
				if(createSpawn) {
					world.getWorldBorder().setSize(2000);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzStarter.getInstance(), new Runnable() {
						
						public void run() {
							SeasonalSurvivalUtils.createSpawnCircle(world, spawnLocation);
						}
						
					}, 600);
				}
				world.setSpawnLocation(spawnLocation.clone().add(0, 1, 0));
				world.setGameRule(GameRule.MOB_GRIEFING, false);
				world.setGameRule(GameRule.SPAWN_RADIUS, 0);
			}
			core.getLogger().info(worldName + " Season: " + currentSeason);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return The current season. (YEAR-QUARTER)
	 */
	private String getSurvivalSeason() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) / 3 + 1);
	}
	
	/**
	 * @return The path of the Season.yml in the Survival world folder.
	 */
	private File getSurvivalSeasonFile() {
		return new File(core.getDataFolder().getAbsolutePath().replace("plugins/WauzStarter", worldName + "/Season.yml"));
	}
	
}
