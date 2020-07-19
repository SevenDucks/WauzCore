package eu.wauz.wauzcore.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.util.WauzFileUtils;

/**
 * Used for managing multiplayer instances.
 * 
 * @author Wauzmons
 */
public class InstanceManager {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();

	/**
	 * Creates an instance for the given player.
	 * If the instance name contains "Survival", a Survival instance is opened, otherwise a MMORPG one.
	 * Specified by the instance folder in the /WauzCore/Worlds folder
	 * and the configuration in the /WauzCore/InstanceData folder.
	 * Also initializes stuff like instance keys and setup commands.
	 * Handles the teleportation of the player afterwards.
	 * 
	 * @param player The player who opened the instance.
	 * @param instanceName The name of the instance.
	 * 
	 * @return If the playere entered the instance successfully.
	 * 
	 * @see InstanceManager#openInstance(File, File)
	 */
	public static boolean enter(Player player, String instanceName) {
		if(StringUtils.contains(instanceName, "Survival")) {
			enterSurvival(player, instanceName);
			return true;
		}
		
// Opening Instance
		
		File sourceFolder = new File(core.getDataFolder(), "Worlds/" + instanceName);
		if(!sourceFolder.exists()) {
			player.sendMessage(ChatColor.RED + "This Instance does not exist!");
			return false;
		}
		
		PlayerConfigurator.setCharacterLocation(player, player.getLocation());
		PetOverviewMenu.unsummon(player);
		
		String instanceId = "WzInstance_MMORPG_" + UUID.randomUUID();
		String path = Bukkit.getWorld("Wauzland").getWorldFolder().getPath().toString().replace("Wauzland", instanceId);
		File targetFolder = new File(path);
		targetFolder.mkdir();
		
		openInstance(sourceFolder, targetFolder);
		final World instance = core.getServer().createWorld(new WorldCreator(instanceId));
		
// World Configuration
		
		InstanceConfigurator.setInstanceWorldName(instance, instanceName);
		String instanceType = InstanceConfigurator.getInstanceType(instanceName);
		InstanceConfigurator.setInstanceWorldType(instance, instanceType);
		InstanceConfigurator.setInstanceWorldMaximumPlayers(instance, InstanceConfigurator.getMaximumPlayers(instanceName));
		InstanceConfigurator.setInstanceWorldMaximumDeaths(instance, InstanceConfigurator.getMaximumDeaths(instanceName));
		
		if(instanceType.equals("Keys")) {
			for(String keyId : InstanceConfigurator.getKeyNameList(instanceName)) {
				InstanceConfigurator.setInstanceWorldKeyStatus(instance, keyId, InstanceConfigurator.KEY_STATUS_UNOBTAINED);
			}
		}
		
// Commands and Entering

		List<String> commands = InstanceConfigurator.getBeforeEnterCommands(instanceName);
		execute(player, instance, commands);
		
		player.teleport(new Location(instance, 0.5, 5, 0.5));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
			public void run() {
				List<String> commands = InstanceConfigurator.getAfterEnterCommands(instanceName);
				execute(player, instance, commands);	
			}
			
		}, 60);
		return true;
	}
	
	/**
	 * Executes setup commands for an instance.
	 * Replaces "world" and "player" placeholders.
	 * 
	 * @param player The player who opened the instance.
	 * @param instance The instance world.
	 * @param commands The list of commands to execute.
	 */
	public static void execute(Player player, World instance, List<String> commands) {
		for(String cmd : commands) {
			cmd = cmd.replaceAll("world", instance.getName());
			cmd = cmd.replaceAll("player", player.getName());
			core.getServer().dispatchCommand(core.getServer().getConsoleSender(), cmd);
		}
	}
	
// Create Guild Instance
	
	/**
	 * Lets the player enter their guildhall. Only works if the player is in a guild.
	 * If there is no guildhall already open, a new instance is created.
	 * Handles the teleportation of the player afterwards.
	 * 
	 * @param player The player who opened the instance.
	 * 
	 * @see InstanceManager#openInstance(File, File)
	 */
	public static void enterGuild(Player player) {
		WauzPlayerGuild guild = PlayerConfigurator.getGuild(player);
		if(guild == null) {
			player.sendMessage(ChatColor.RED + "You are not in a guild!");
			return;
		}
		
		PlayerConfigurator.setCharacterLocation(player, player.getLocation());
		PetOverviewMenu.unsummon(player);
		
		String instanceId = "WzInstance_MMORPG_" + guild.getGuildUuidString();
		World instance = Bukkit.getWorld(instanceId);
		if(instance == null) {
			File sourceFolder = new File(core.getDataFolder(), "Worlds/Guildhall");
			String path = Bukkit.getWorld("Wauzland").getWorldFolder().getPath().toString().replace("Wauzland", instanceId);
			File targetFolder = new File(path);
			targetFolder.mkdir();
			openInstance(sourceFolder, targetFolder);
			instance = core.getServer().createWorld(new WorldCreator(instanceId));
			InstanceConfigurator.setInstanceWorldName(instance, guild.getGuildName() + " Guildhall");
			InstanceConfigurator.setInstanceWorldType(instance, "Guild");
			InstanceConfigurator.setInstanceWorldMaximumPlayers(instance, 0);
			InstanceConfigurator.setInstanceWorldMaximumDeaths(instance, 0);
		}
		
		player.teleport(new Location(instance, 0.5, 26, 0.5));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
// Create Survival Instance
	
	/**
	 * Creates an instance for the given player.
	 * Specified by the instance name:
	 * "Survival Nether" opens a nether world.
	 * "Survival End" opens a nether world.
	 * Otherwise a normal world should be created.
	 * A circle with exit signs is created around the spawn.
	 * Handles the teleportation of the player afterwards.
	 * 
	 * @param player The player who opened the instance.
	 * @param instanceName The name of the instance.
	 * 
	 * @see InstanceManager#createSpawnCircle(World, Location)
	 */
	public static void enterSurvival(Player player, String instanceName) {
		String instanceId = "WzInstance_Survival_" + UUID.randomUUID();
		
		WorldCreator worldCreator = new WorldCreator(instanceId);
		
		if(instanceName.equals("Survival Nether")) {
			worldCreator = worldCreator.environment(Environment.NETHER);
		}
		else if(instanceName.equals("Survival End")) {
			worldCreator = worldCreator.environment(Environment.THE_END);
		}
		
		World instance = core.getServer().createWorld(worldCreator);
		Location spawnLocation = instance.getSpawnLocation().clone().add(0.5, 0, 0.5);
		createSpawnCircle(instance, spawnLocation.clone().add(0, -1, 0));
		
		InstanceConfigurator.setInstanceWorldName(instance, "Survival");
		InstanceConfigurator.setInstanceWorldType(instance, "Survival");
		InstanceConfigurator.setInstanceWorldMaximumPlayers(instance, 0);
		InstanceConfigurator.setInstanceWorldMaximumDeaths(instance, 0);
		
		WauzDebugger.log(player, "Spawn Location: "
				+ spawnLocation.getX() + " "
				+ spawnLocation.getY() + " "
				+ spawnLocation.getZ());
		
		player.teleport(spawnLocation);
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
		player.sendMessage(ChatColor.YELLOW
				+ "Welcome! Type /spawn to leave the instance, but be aware that you can't return, "
				+ "unless one of your group members stays in this world!");
	}
	
	/**
	 * Creates a circle out of obsidian and glowstone at the given location.
	 * Exit signs are placed on the circle edges.
	 * Used for survival instance spawns.
	 * 
	 * @param world The world on which the circle should be created.
	 * @param location The center location of the circle.
	 * 
	 * @see InstanceManager#placeExitSign(Block, BlockFace)
	 */
	private static void createSpawnCircle(World world, Location location) {
		Vector vector = new BlockVector(location.getX(), location.getY(), location.getZ());
		int radius = 7;
		for(int x = -radius; x <= radius; x++) {
			for(int z = -radius; z <= radius; z++) {
				
				Vector position = vector.clone().add(new Vector(x, 0, z));
				double distance = vector.distance(position);
				
				if(distance <= radius + 0.5) {
					boolean isCircleEdge = distance > radius - 0.5;
					Material material = isCircleEdge ? Material.GLOWSTONE : Material.OBSIDIAN;
					
					world.getBlockAt(x, location.getBlockY(), z).setType(material);
					world.getBlockAt(x, location.getBlockY() + 1, z).setType(Material.AIR);
					world.getBlockAt(x, location.getBlockY() + 2, z).setType(Material.AIR);
					world.getBlockAt(x, location.getBlockY() + 3, z).setType(Material.AIR);
				}
			}
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
	 * Places a exit sign on the given block.
	 * Used for instance exits.
	 * 
	 * @param block The block where the sign should be placed.
	 * @param blockFace The direction thr sign should face.
	 */
	private static void placeExitSign(Block block, BlockFace blockFace) {
		block.setType(Material.OAK_SIGN);
		Sign sign = (Sign) block.getState();
		org.bukkit.block.data.type.Sign signData = (org.bukkit.block.data.type.Sign) sign.getBlockData();
		signData.setRotation(blockFace);
		sign.setBlockData(signData);
		sign.setLine(1, WauzSigns.EXIT_DOOR_TEXT);
		sign.setLine(2, WauzSigns.EXIT_DOOR_LEAVE_TEXT);
		sign.update();
	}
	
// Instance Lifecycle
	
	/**
	 * Creates a new instance from a template.
	 * Is called recursively to copy files into the new world.
	 * 
	 * @param source The instance template.
	 * @param target The new instance folder.
	 */
	private static void openInstance(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists()) {
	                	target.mkdirs();
	                }
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    openInstance(srcFile, destFile);
	                }
	            }
	            else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    }
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	/**
	 * Unloads the world of an instance and deletes the folder.
	 * Only works if given world is an instance.
	 * 
	 * @param world The instance world.
	 */
	public static void closeInstance(World world) {
	    if(world.getWorldFolder().toString().contains("Instance")) {
			Bukkit.getServer().unloadWorld(world, true);
			WauzFileUtils.removeFilesRecursive(world.getWorldFolder());
	    }
	}
	
	/**
	 * Removes all instance folders.
	 * WARNING: There is no check if the instance is loaded!
	 */
	public static void removeInactiveInstances() {
		File rootDirectory = new File(Bukkit.getWorld("Wauzland").getWorldFolder().getPath().toString().replace("Wauzland", ""));
		for(File file : rootDirectory.listFiles()) {
			if(file.getName().startsWith("WzInstance")) {
				WauzFileUtils.removeFilesRecursive(file);
			}
		}
	}
	
}
