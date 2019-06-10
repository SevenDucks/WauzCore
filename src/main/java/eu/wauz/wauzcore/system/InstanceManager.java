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

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import net.md_5.bungee.api.ChatColor;

public class InstanceManager {
	
	private static WauzCore core = WauzCore.getInstance();

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
		
		if(instanceType.equals("Keys"))
			for(String keyId : InstanceConfigurator.getKeyNameList(instanceName))
				InstanceConfigurator.setInstanceWorldKeyStatus(instance, keyId, InstanceConfigurator.KEY_STATUS_UNOBTAINED);
		
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
	
	public static void execute(Player player, World instance, List<String> commands) {
		for(String cmd : commands) {
			cmd = cmd.replaceAll("world", instance.getName());
			cmd = cmd.replaceAll("player", player.getName());
			core.getServer().dispatchCommand(core.getServer().getConsoleSender(), cmd);
		}
	}
	
// Create Guild Instance
	
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
		}
		
		player.teleport(new Location(instance, 0.5, 5, 0.5));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
// Create Survival Instance
	
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
	
	private static void placeExitSign(Block block, BlockFace blockFace) {
		block.setType(Material.SIGN);
		Sign sign = (Sign) block.getState();
		org.bukkit.material.Sign signData = new org.bukkit.material.Sign(Material.SIGN);
		signData.setFacingDirection(blockFace);
		sign.setData(signData);
		sign.setLine(1, WauzSigns.EXIT_DOOR_TEXT);
		sign.setLine(2, WauzSigns.EXIT_DOOR_LEAVE_TEXT);
		sign.update();
	}
	
// Instance Lifecycle
	
	private static void openInstance(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    openInstance(srcFile, destFile);
	                }
	            } else {
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
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	public static void closeInstance(World world) {
	    if(world.getWorldFolder().toString().contains("Instance")) {
			Bukkit.getServer().unloadWorld(world, true);
			removeInstance(world.getWorldFolder());
	    }
	}
	
	public static void removeInactiveInstances() {
		File rootDirectory = new File(Bukkit.getWorld("Wauzland").getWorldFolder().getPath().toString().replace("Wauzland", ""));
		for(File file : rootDirectory.listFiles()) {
			if(file.getName().startsWith("WzInstance"))
				removeInstance(file);
		}
	}
	
	private static boolean removeInstance(File file) {
	    if(file.exists()) {
	        File files[] = file.listFiles();
	        for(int i = 0; i < files.length; i++) {
	            if(files[i].isDirectory()) {
	                removeInstance(files[i]);
	            } else {
	                files[i].delete();
	            }
	        }
	    }
	    return(file.delete());
	}
	
}
