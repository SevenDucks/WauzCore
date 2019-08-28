package eu.wauz.wauzstarter;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

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
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class SeasonalSurvivalManager {
	
	private static WauzStarter core = WauzStarter.getInstance();

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
	
	private static void placeEnderChest(Block block, BlockFace blockFace) {
		block.setType(Material.ENDER_CHEST);
		BlockData blockData = block.getBlockData();
        ((Directional) blockData).setFacing(blockFace);
        block.setBlockData(blockData);
	}
	
	private static String getSurvivalSeason() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) / 3 + 1);
	}
	
	private static File getSurvivalSeasonFile() {
		return new File(core.getDataFolder().getAbsolutePath().replace("plugins/WauzStarter", "Survival/Season.yml"));
	}
	
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
