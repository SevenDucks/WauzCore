package eu.wauz.wauzstarter;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * An util class to spawn structures in seasonal Survival worlds.
 * Called by the seasonal survival manager.
 * 
 * @author Wauzmons
 *
 * @see SeasonalSurvivalManager
 */
public class SeasonalSurvivalUtils {
	
	/**
	 * Creates a circular spawn area in the given world.
	 * 
	 * @param world The world to create the spawn circle in.
	 * @param location The location to create the spawn circle at.
	 */
	public static void createSpawnCircle(World world, Location location) {
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
					for(int y = 71; y < 256; y++) {
						world.getBlockAt(x, y, z).setType(Material.AIR);
					}
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
	 * @param block The block that should be an ender chest.
	 * @param blockFace The direction the chest should be facing.
	 * 
	 * @see SeasonalSurvivalUtils#createSpawnCircle(World, Location)
	 */
	public static void placeEnderChest(Block block, BlockFace blockFace) {
		block.setType(Material.ENDER_CHEST);
		BlockData blockData = block.getBlockData();
        ((Directional) blockData).setFacing(blockFace);
        block.setBlockData(blockData);
	}

	/**
	 * Deletes a world from the server.
	 * 
	 * @param file The world folder.
	 * @return If the world folder was deleted.
	 */
	public static boolean deleteWorld(File file) {
		if(file.exists()) {
		    File files[] = file.listFiles();
		    for(int i = 0; i < files.length; i++) {
		        if(files[i].isDirectory()) {
		            deleteWorld(files[i]);
		        }
		        else {
		            files[i].delete();
		        }
		    }
		}
		return file.delete();
	}
	
}
