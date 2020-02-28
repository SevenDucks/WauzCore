package eu.wauz.wauzstarter;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * This class is used to create a new world when a Survival season starts.
 * It is used once a day on server startup.
 * 
 * @author Wauzmons
 */
public class SeasonalSurvivalManager {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzStarter core = WauzStarter.getInstance();

	/**
	 * Loads the current Survival world or starts a new one,
	 * if it is outdated or has no Season.yml file.
	 */
	public static void generateSurvivalWorld() {
		String currentSeason = getSurvivalSeason();
		File seasonFile = getSurvivalSeasonFile();
		if(seasonFile.exists()) {
			FileConfiguration seasonConfig = YamlConfiguration.loadConfiguration(seasonFile);
			String season = seasonConfig.getString("season");
			if(StringUtils.equals(season, currentSeason)) {
				core.getLogger().info("Survival World is up to date!");
			}
			else {
				core.getLogger().info("Survival World is outdated! Starting new one!");
				String filePath = seasonFile.getParentFile().getAbsolutePath();
				deleteWorld(new File(filePath));
				deleteWorld(new File(filePath.replace("Survival", "Survival_nether")));
				deleteWorld(new File(filePath.replace("Survival", "Survival_the_end")));
			}
		}
		core.getServer().createWorld(new WorldCreator("Survival"));
		updateSurvivalWorld();
	}
	
	/**
	 * Sets up all properties of the current Survival world.
	 * This includes the spawn, season file and gamerules.
	 */
	private static void updateSurvivalWorld() {
		try {		
			String season = getSurvivalSeason();
			File seasonFile = getSurvivalSeasonFile();
			if(!seasonFile.exists()) {
				seasonFile.getParentFile().mkdir();
				FileConfiguration seasonConfig = YamlConfiguration.loadConfiguration(seasonFile);
				seasonConfig.set("season", season);
				seasonConfig.save(seasonFile);
			}
			World survival = core.getServer().getWorld("Survival");
			Location spawnLocation = new Location(survival, 0, 70, 0);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzStarter.getInstance(), new Runnable() {
	            public void run() {
	            	createSpawnCircle(survival, spawnLocation);
	            }
			}, 600);
			survival.setSpawnLocation(spawnLocation.clone().add(0, 1, 0));
			survival.setGameRule(GameRule.MOB_GRIEFING, false);
			survival.setGameRule(GameRule.SPAWN_RADIUS, 0);
			
			core.getLogger().info("Survival Season: " + season);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a circular spawn area in the Survival world.
	 * 
	 * @param world
	 * @param location
	 */
	private static void createSpawnCircle(World world, Location location) {
		Vector vector = new BlockVector(location.getX(), location.getY(), location.getZ());
		int radius = 10;
		for(int x = -radius; x <= radius; x++) {
			for(int z = -radius; z <= radius; z++) {
				
				Vector position = vector.clone().add(new Vector(x, 0, z));
				double distance = vector.distance(position);
				
				if(distance <= radius + 0.5) {
					boolean isCircleEdge = distance > radius - 0.5;
					Material material = isCircleEdge ? Material.GOLD_BLOCK : Material.CHISELED_QUARTZ_BLOCK;
					
					world.getBlockAt(x, 70, z).setType(material);
					for(int y = 71; y < 256; y++)
						world.getBlockAt(x, y, z).setType(Material.AIR);
				}
			}
		}
		location.getBlock().setType(Material.DIAMOND_BLOCK);
		location.getBlock().getRelative(BlockFace.NORTH).setType(Material.GOLD_BLOCK);
		location.getBlock().getRelative(BlockFace.SOUTH).setType(Material.GOLD_BLOCK);
		location.getBlock().getRelative(BlockFace.EAST).setType(Material.GOLD_BLOCK);
		location.getBlock().getRelative(BlockFace.WEST).setType(Material.GOLD_BLOCK);
		
		placeEnderChest(location.clone().add(0, 1, +7).getBlock(), BlockFace.NORTH);
		placeEnderChest(location.clone().add(0, 1, -7).getBlock(), BlockFace.SOUTH);
		placeEnderChest(location.clone().add(-7, 1, 0).getBlock(), BlockFace.EAST);
		placeEnderChest(location.clone().add(+7, 1, 0).getBlock(), BlockFace.WEST);
	}
	
	/**
	 * Places an ender chest at the given block.
	 * Used in spawn area creation.
	 * 
	 * @param block
	 * @param blockFace
	 * 
	 * @see SeasonalSurvivalManager#createSpawnCircle(World, Location)
	 */
	private static void placeEnderChest(Block block, BlockFace blockFace) {
		block.setType(Material.ENDER_CHEST);
		BlockData blockData = block.getBlockData();
        ((Directional) blockData).setFacing(blockFace);
        block.setBlockData(blockData);
	}
	
	/**
	 * @return The current season. (YEAR-QUARTER)
	 */
	private static String getSurvivalSeason() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) / 3 + 1);
	}
	
	/**
	 * @return The path of the Season.yml in the Survival world folder.
	 */
	private static File getSurvivalSeasonFile() {
		return new File(core.getDataFolder().getAbsolutePath().replace("plugins/WauzStarter", "Survival/Season.yml"));
	}
	
	/**
	 * Deletes a world from the server.
	 * 
	 * @param file The world folder.
	 * @return If the world folder was deleted.
	 */
	private static boolean deleteWorld(File file) {
		if(file.exists()) {
		    File files[] = file.listFiles();
		    for(int i = 0; i < files.length; i++) {
		        if(files[i].isDirectory()) {
		            deleteWorld(files[i]);
		        } else {
		            files[i].delete();
		        }
		    }
		}
		return file.delete();
	}
	
}
