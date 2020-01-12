package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.RegionConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.md_5.bungee.api.ChatColor;

/**
 * A class for handling the usage of event signs.
 * 
 * @author Wauzmons
 */
public class WauzSigns {
	
	/**
	 * The first row text of an exit door sign.
	 */
	public static final String EXIT_DOOR_TEXT = ChatColor.BLACK + "[" + ChatColor.DARK_BLUE + "Leave" + ChatColor.BLACK + "]";
	
	/**
	 * The second row text of an exit door sign.
	 */
	public static final String EXIT_DOOR_LEAVE_TEXT = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Exit Instance";
	
	/**
	 * The first row text of a locked door sign.
	 */
	private static final String LOCKED_DOOR_TEXT = ChatColor.BLACK + "[" + ChatColor.DARK_RED + "Locked Door" + ChatColor.BLACK + "]";
	
	/**
	 * The second row text of a locked door sign.
	 */
	private static final String LOCKED_DOOR_KEY_TEXT = ChatColor.BLACK + "Key:" + ChatColor.DARK_AQUA + ChatColor.BOLD + " ";
	
	/**
	 * The first row text of a travel sign.
	 */
	private static final String TRAVEL_TEXT = ChatColor.BLACK + "[" + ChatColor.DARK_PURPLE + "Fast Travel" + ChatColor.BLACK + "]";
	
	/**
	 * The second row text of a travel sign.
	 */
	private static final String TRAVEL_LOCATION_TEXT = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "";
	
	/**
	 * Tries to format a sign, to be a special event sign, if the valid keywords were found.
	 * List of valid formats, were --- stands for empty rows:</br></br>
	 * 
	 * --------------------</br>
	 * [Leave]</br>
	 * --------------------</br>
	 * --------------------</br></br>
	 * 
	 * --------------------</br>
	 * [Locked Door]</br>
	 * Key: KeyName</br>
	 * --------------------</br></br>
	 * 
	 * --------------------</br>
	 * [Fast Travel]</br>
	 * StationName</br>
	 * --------------------
	 * 
	 * @param event The sign event.
	 */
	public static void create(SignChangeEvent event) {	
		String[] lines = event.getLines();
		
		if(StringUtils.isBlank(lines[1])) {
			return;
		}
		else if(lines[1].equals("[Leave]")) {
			lines[1] = EXIT_DOOR_TEXT;
			lines[2] = EXIT_DOOR_LEAVE_TEXT;
		}
		else if(lines[1].equals("[Locked Door]") && lines[2].contains("Key: ")) {
			lines[1] = LOCKED_DOOR_TEXT;
			lines[2] = lines[2].replace("Key: ", LOCKED_DOOR_KEY_TEXT);
		}
		else if(lines[1].equals("[Fast Travel]") && StringUtils.isNotBlank(lines[2])) {
			lines[1] = TRAVEL_TEXT;
			lines[2] = TRAVEL_LOCATION_TEXT + lines[2];
		}
	}
	
	/**
	 * Checks the sign content for keywords, to may trigger an event for the player.
	 * 
	 * @param player The player who interacted with the sign.
	 * @param block The sign block.
	 * 
	 * @see WauzTeleporter#exitInstanceTeleportManual(Player)
	 * @see WauzSigns#tryToOpenDoor(Player, Sign)
	 * @see WauzSigns#tryToTravel(Player, Sign)
	 */
	public static void interact(Player player, Block block) {
		Sign sign = (Sign) block.getState();
		String signType = sign.getLine(1);
		
		if(StringUtils.isBlank(signType) || !signType.contains(UnicodeUtils.ICON_PARAGRAPH)) {
			return;
		}
		else if(signType.contains("Leave") && player.getWorld().getName().contains("Instance")) {
			WauzTeleporter.exitInstanceTeleportManual(player);
		}
		else if(!WauzMode.isMMORPG(player)) {
			return;
		}
		else if(signType.contains("Locked Door") && player.getWorld().getName().contains("Instance")) {
			tryToOpenDoor(player, sign);
		}
		else if(signType.contains("Fast Travel")) {
			tryToTravel(player, sign);
		}
	}
	
	/**
	 * Tries to open a door, based on the key name on a sign.
	 * 
	 * @param player The player who is opening the door.
	 * @param sign The sign that is placed on the door.
	 * 
	 * @see InstanceConfigurator#getInstanceKeyStatus(org.bukkit.World, String)
	 * @see InstanceConfigurator#setInstanceWorldKeyStatus(org.bukkit.World, String, String)
	 */
	private static void tryToOpenDoor(Player player, Sign sign) {
		WauzDebugger.log(player, "Try to Open Door");
		String keyId = StringUtils.substringAfterLast(sign.getLine(2), " ");
		WauzDebugger.log(player, "Key ID: " + keyId);
		
		boolean hasAccess = false;
		
		if(keyId.equals("None")) {
			hasAccess = true;
		}
		else {
			String keyStatus = InstanceConfigurator.getInstanceKeyStatus(player.getWorld(), keyId);
			hasAccess =
				keyStatus.equals(InstanceConfigurator.KEY_STATUS_OBTAINED) ||
				keyStatus.equals(InstanceConfigurator.KEY_STATUS_USED);
		}
		
		if(!hasAccess) {
			player.sendMessage(ChatColor.RED + "You don't have the right key to open this!");
			return;
		}
		
		BlockFace blockFace = ((org.bukkit.block.data.type.WallSign) sign.getBlock().getBlockData()).getFacing().getOppositeFace();
		boolean isHorizontal = blockFace.equals(BlockFace.SOUTH) || blockFace.equals(BlockFace.NORTH);
		WauzDebugger.log(player, "Door Sign Face: " + blockFace.toString());
		
		List<Block> blocksToRemove = new ArrayList<>();
		Block centerBlock = sign.getBlock().getRelative(blockFace);
		blocksToRemove.add(centerBlock);
		blocksToRemove.add(centerBlock.getRelative(BlockFace.UP));
		blocksToRemove.add(centerBlock.getRelative(BlockFace.DOWN));
		
		for(Block block : blocksToRemove) {
			if(isHorizontal) {
				block.getRelative(BlockFace.WEST).setType(Material.AIR);
				block.getRelative(BlockFace.EAST).setType(Material.AIR);
			}
			else {
				block.getRelative(BlockFace.SOUTH).setType(Material.AIR);
				block.getRelative(BlockFace.NORTH).setType(Material.AIR);
			}
			block.setType(Material.AIR);
		}
		
		InstanceConfigurator.setInstanceWorldKeyStatus(player.getWorld(), keyId, InstanceConfigurator.KEY_STATUS_USED);
		for(Player member : player.getWorld().getPlayers()) {
			WauzPlayerScoreboard.scheduleScoreboard(member);
			member.sendMessage(ChatColor.GREEN + "The door \"" + ChatColor.DARK_AQUA + keyId + ChatColor.GREEN + "\" was unlocked!");
		}
	}
	
	/**
	 * Tries to travel to a station, based on the station name on a sign.
	 * 
	 * @param player The player who wants to travel.
	 * @param sign The sign at the current travel station.
	 * 
	 * @see RegionConfigurator#getStationCoordinateString(String)
	 * @see WauzSigns#startTravelling(Player, String, String, String)
	 */
	private static void tryToTravel(Player player, Sign sign) {
		WauzDebugger.log(player, "Try to Travel");
		String stationId = StringUtils.substringAfterLast(sign.getLine(2), TRAVEL_LOCATION_TEXT);
		WauzDebugger.log(player, "Station ID: " + stationId);
		
		String coordinateString = RegionConfigurator.getStationCoordinateString(stationId);
		if(StringUtils.isBlank(coordinateString)) {
			player.sendMessage(ChatColor.RED + "This station is not available at the moment!");
		}
		else {
			player.sendMessage(ChatColor.GREEN + "Travelling to: " + ChatColor.DARK_AQUA + stationId + ChatColor.GREEN + " (" + coordinateString + ")");
			String[] coordinateStrings = coordinateString.split(" ");
			startTravelling(player, coordinateStrings[0], coordinateStrings[1], coordinateStrings[2]);
		}
	}
	
	/**
	 * Strats travelling to the destined location, on the MythicMob called "TravelPhantom".
	 * Checks regularely afterwards, if the target is reached.
	 * 
	 * @param player The player that is travelling.
	 * @param xString The x coordinate of the destined location.
	 * @param yString The y coordinate of the destined location.
	 * @param zString The z coordinate of the destined location.
	 * 
	 * @return If the travel was successffully started.
	 * 
	 * @see BukkitAPIHelper#spawnMythicMob(String, Location)
	 * @see WauzSigns#atTravelDestination(Entity, Location)
	 */
	public static boolean startTravelling(Player player, String xString, String yString, String zString) {
		try {
			double x = Double.parseDouble(xString);
			double y = Double.parseDouble(yString);
			double z = Double.parseDouble(zString);
			Location targetLocation = new Location(player.getWorld(), x, y, z);
			Location offsetLocation = targetLocation.clone().add(0, 75, 0);
			BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
			
			player.teleport(offsetLocation);
			Entity dragon = mythicMobs.spawnMythicMob("TravelPhantom", offsetLocation);
			WauzNmsClient.nmsEntityPersistence(dragon, false);
			dragon.addPassenger(player);
			
			atTravelDestination(dragon, targetLocation);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks if the entity reached the travel destination.
	 * If the entity reached the destination or don't has any players on it, it unmounts all entities and despawns.
	 * If not it spawns some particles and rechecks in 0,5 seconds.
	 * 
	 * @param entity The entity that is moving towards the destination.
	 * @param targetLocation The destined location.
	 * 
	 * @see WauzSigns#atTravelCoordinate(double, double, double)
	 * @see WauzSigns#hasPlayerMounted(Entity)
	 * @see ParticleSpawner#spawnParticleHelix(Location, SkillParticle, double, double)
	 */
	private static void atTravelDestination(Entity entity, Location targetLocation) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
	        public void run() {
	        	try {
	        		Location entityLocation = entity.getLocation();
	        		
	    			if(atTravelCoordinate(entityLocation.getY(), targetLocation.getY(), 1) || !hasPlayerMounted(entity)) {
	    				for(Entity passenger : entity.getPassengers()) {
	    					if(passenger instanceof Player) {
	    						passenger.leaveVehicle();
	    					}
	    					else {
	    						passenger.remove();
	    					}
	    				}
	    				entity.remove();
	    			}
	    			else {
	    				ParticleSpawner.spawnParticleHelix(entity.getLocation(), new SkillParticle(Particle.PORTAL), 1.5, 5);
	    			}

	        		atTravelDestination(entity, targetLocation);
	        	}
	        	catch (NullPointerException e) {
	        		WauzDebugger.catchException(getClass(), e);
	        	}
	        }
		}, 10);
	}

	/**
	 * Checks if the given y coordinate has been reached.
	 * 
	 * @param coord1 The current y coordinate.
	 * @param coord2 The target y coordinate.
	 * @param maxDiff The maximum difference between coordinates.
	 * 
	 * @return If the coordinates has been reached.
	 */
	private static boolean atTravelCoordinate(double coord1, double coord2, double maxDiff) {
		return Math.abs(coord1 - coord2) < maxDiff;
	}
	
	/**
	 * Checks if a player is in the given list of passengers.
	 * 
	 * @param entity The vehicle, that holds the passengers.
	 * 
	 * @return If a player is mounted.
	 */
	public static boolean hasPlayerMounted(Entity entity) {
		for(Entity passenger : entity.getPassengers()) {
			if(passenger instanceof Player) {
				return true;
			}
		}
		return false;
	}

}
