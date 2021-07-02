package eu.wauz.wauzcore.building;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A generator to create spawn areas.
 * 
 * @author Wauzmons
 */
public class WorldSpawnGenerator {
	
	/**
	 * Creates a circular spawn area in the given world.
	 * Ender chests are placed on the circle edges.
	 * 
	 * @param world The world to create the spawn circle in.
	 * @param location The location to create the spawn circle at.
	 */
	public static void createMainSpawnCircle(World world, Location location) {
		new ShapeCircle(location, 10, false).create(Material.CHISELED_QUARTZ_BLOCK);
		new ShapeCircle(location, 10, true).create(Material.GOLD_BLOCK);
		for(int y = 1; y < world.getMaxHeight() - location.getBlockY(); y++) {
			new ShapeCircle(location.clone().add(0, y, 0), 10, false).create(Material.AIR);
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
	 * Creates a circular spawn area in the given world.
	 * Exit signs are placed on the circle edges.
	 * 
	 * @param world The world to create the spawn circle in.
	 * @param location The location to create the spawn circle at.
	 */
	public static void createInstanceSpawnCircle(World world, Location location) {
		new ShapeCircle(location, 7, false).create(Material.OBSIDIAN);
		new ShapeCircle(location, 7, true).create(Material.GLOWSTONE);
		for(int y = 1; y <= 3; y++) {
			new ShapeCircle(location.clone().add(0, y, 0), 7, false).create(Material.AIR);
		}
		location.getBlock().setType(Material.BEDROCK);
		location.getBlock().getRelative(BlockFace.NORTH).setType(Material.GLOWSTONE);
		location.getBlock().getRelative(BlockFace.SOUTH).setType(Material.GLOWSTONE);
		location.getBlock().getRelative(BlockFace.EAST).setType(Material.GLOWSTONE);
		location.getBlock().getRelative(BlockFace.WEST).setType(Material.GLOWSTONE);
		placeExitSign(location.clone().add(0, 1, +5).getBlock(), BlockFace.NORTH);
		placeExitSign(location.clone().add(0, 1, -5).getBlock(), BlockFace.SOUTH);
		placeExitSign(location.clone().add(-5, 1, 0).getBlock(), BlockFace.EAST);
		placeExitSign(location.clone().add(+5, 1, 0).getBlock(), BlockFace.WEST);
	}
	
	/**
	 * Places an ender chest on the given block.
	 * 
	 * @param block The block where the chest should be placed.
	 * @param blockFace The direction the chest should face.
	 * 
	 * @see WorldSpawnGenerator#createSpawnCircle(World, Location)
	 */
	private static void placeEnderChest(Block block, BlockFace blockFace) {
		block.setType(Material.ENDER_CHEST);
		BlockData blockData = block.getBlockData();
        ((Directional) blockData).setFacing(blockFace);
        block.setBlockData(blockData);
	}
	
	/**
	 * Places an exit sign on the given block.
	 * 
	 * @param block The block where the sign should be placed.
	 * @param blockFace The direction the sign should face.
	 */
	private static void placeExitSign(Block block, BlockFace blockFace) {
		block.setType(Material.OAK_SIGN);
		Sign sign = (Sign) block.getState();
		org.bukkit.block.data.type.Sign signData = (org.bukkit.block.data.type.Sign) sign.getBlockData();
		signData.setRotation(blockFace);
		sign.setBlockData(signData);
		Components.line(sign, 1, WauzSigns.EXIT_DOOR_TEXT);
		Components.line(sign, 2, WauzSigns.EXIT_DOOR_LEAVE_TEXT);
		sign.update();
	}
	
}
