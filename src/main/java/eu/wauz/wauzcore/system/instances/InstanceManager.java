package eu.wauz.wauzcore.system.instances;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.building.ShapeCircle;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.WauzDebugger;
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
		
		WauzInstance instanceTemplate = WauzInstance.getInstance(instanceName);
		if(instanceTemplate == null) {
			player.sendMessage(ChatColor.RED + "This Instance does not exist!");
			return false;
		}
		
		CharacterManager.saveCharacter(player);
		WauzActivePet.tryToUnsummon(player, true);
		
		String instanceUuid = "WzInstance_MMORPG_" + UUID.randomUUID();
		String path = Bukkit.getWorld("MMORPG").getWorldFolder().getPath().toString().replace("MMORPG", instanceUuid);
		File targetFolder = new File(path);
		targetFolder.mkdir();
		
		File sourceFolder = new File(core.getDataFolder(), "Worlds/" + instanceTemplate.getInstanceWorldTemplateName());
		openInstance(sourceFolder, targetFolder);
		final World instanceWorld = core.getServer().createWorld(new WorldCreator(instanceUuid));
		
		WauzActiveInstance activeInstance = new WauzActiveInstance(instanceWorld, instanceTemplate);
		WauzActiveInstancePool.registerInstance(activeInstance);
		
		player.teleport(new Location(instanceWorld, 0.5, 5, 0.5));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
		return true;
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
		
		CharacterManager.saveCharacter(player);
		WauzActivePet.tryToUnsummon(player, true);
		
		String instanceUuid = "WzInstance_MMORPG_" + guild.getGuildUuidString();
		World instanceWorld = Bukkit.getWorld(instanceUuid);
		if(instanceWorld == null) {
			File sourceFolder = new File(core.getDataFolder(), "Worlds/Guildhall");
			String path = Bukkit.getWorld("MMORPG").getWorldFolder().getPath().toString().replace("MMORPG", instanceUuid);
			File targetFolder = new File(path);
			targetFolder.mkdir();
			openInstance(sourceFolder, targetFolder);
			instanceWorld = core.getServer().createWorld(new WorldCreator(instanceUuid));
			
			WauzActiveInstance activeInstance = new WauzActiveInstance(instanceWorld, guild.getGuildName() + " Guildhall");
			activeInstance.setDisplayTitle(guild.getGuildName());
			activeInstance.setDisplaySubtitle("Grand Guildhall");
			WauzActiveInstancePool.registerInstance(activeInstance);
		}
		
		player.teleport(new Location(instanceWorld, 0.5, 26, 0.5));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
// Create Guild Instance
	
	/**
	 * Lets the player enter the arcade lobby.
	 * If there is no lobby already open, a new instance is created.
	 * Handles the teleportation of the player afterwards.
	 * 
	 * @param player The player who opened the instance.
	 * 
	 * @see InstanceManager#openInstance(File, File)
	 */
	public static void enterArcade(Player player) {
		String instanceUuid = "WzInstance_Arcade";
		World instanceWorld = Bukkit.getWorld(instanceUuid);
		if(instanceWorld == null) {
			File sourceFolder = new File(core.getDataFolder(), "Worlds/Arcade");
			String path = Bukkit.getWorld("MMORPG").getWorldFolder().getPath().toString().replace("MMORPG", instanceUuid);
			File targetFolder = new File(path);
			targetFolder.mkdir();
			openInstance(sourceFolder, targetFolder);
			instanceWorld = core.getServer().createWorld(new WorldCreator(instanceUuid));
			
			WauzActiveInstance activeInstance = new WauzActiveInstance(instanceWorld, "DropGuys");
			activeInstance.setDisplayTitle("DropGuys");
			activeInstance.setDisplaySubtitle("Ultimate Knockdown");
			WauzActiveInstancePool.registerInstance(activeInstance);
		}
		
		ArcadeLobby.addPlayerToQueue(player);
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
		CharacterManager.saveCharacter(player);
		String instanceUuid = "WzInstance_Survival_" + UUID.randomUUID();
		WorldCreator worldCreator = new WorldCreator(instanceUuid);
		String worldType;
		if(instanceName.equals("Survival Nether")) {
			worldCreator = worldCreator.environment(Environment.NETHER);
			worldType = "The Nether";
		}
		else if(instanceName.equals("Survival End")) {
			worldCreator = worldCreator.environment(Environment.THE_END);
			worldType = "The End";
		}
		else {
			worldType = "The Overworld";
		}
		
		World instanceWorld = core.getServer().createWorld(worldCreator);
		Location spawnLocation = instanceWorld.getSpawnLocation().clone().add(0.5, 0, 0.5);
		createSpawnCircle(instanceWorld, spawnLocation.clone().add(0, -1, 0));
		
		WauzActiveInstance activeInstance = new WauzActiveInstance(instanceWorld, "Survival");
		activeInstance.setDisplayTitle(worldType);
		activeInstance.setDisplaySubtitle("Survival Pocket Realm");
		WauzActiveInstancePool.registerInstance(activeInstance);
		
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
		new ShapeCircle(location, 7, false).create(Material.OBSIDIAN);
		new ShapeCircle(location, 7, true).create(Material.GLOWSTONE);
		new ShapeCircle(location.clone().add(0, 1, 0), 7, false).create(Material.AIR);
		new ShapeCircle(location.clone().add(0, 1, 0), 7, false).create(Material.AIR);
		new ShapeCircle(location.clone().add(0, 1, 0), 7, false).create(Material.AIR);
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
	 * 
	 * @see WauzActiveInstancePool#unregisterInstance(String)
	 * @see WauzFileUtils#removeFilesRecursive(File)
	 */
	public static void closeInstance(World world) {
	    if(world.getName().contains("WzInstance")) {
	    	WauzActiveInstancePool.unregisterInstance(world.getName());
			Bukkit.getServer().unloadWorld(world, true);
			WauzFileUtils.removeFilesRecursive(world.getWorldFolder());
	    }
	}
	
	/**
	 * Removes all instance folders.
	 * WARNING: There is no check if the instance is loaded!
	 * 
	 * @see WauzActiveInstancePool#unregisterInstance(String)
	 * @see WauzFileUtils#removeFilesRecursive(File)
	 */
	public static void removeInactiveInstances() {
		File rootDirectory = new File(Bukkit.getWorld("MMORPG").getWorldFolder().getPath().toString().replace("MMORPG", ""));
		for(File file : rootDirectory.listFiles()) {
			if(file.getName().startsWith("WzInstance")) {
				WauzActiveInstancePool.unregisterInstance(file.getName());
				WauzFileUtils.removeFilesRecursive(file);
			}
		}
	}
	
}
