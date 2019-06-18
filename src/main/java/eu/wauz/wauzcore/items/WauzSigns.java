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
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillMechanics;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzMode;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.md_5.bungee.api.ChatColor;

public class WauzSigns {
	
	public static final String EXIT_DOOR_TEXT = ChatColor.BLACK + "[" + ChatColor.DARK_BLUE + "Leave" + ChatColor.BLACK + "]";
	public static final String EXIT_DOOR_LEAVE_TEXT = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Exit Instance";
	
	private static final String LOCKED_DOOR_TEXT = ChatColor.BLACK + "[" + ChatColor.DARK_RED + "Locked Door" + ChatColor.BLACK + "]";
	private static final String LOCKED_DOOR_KEY_TEXT = ChatColor.BLACK + "Key:" + ChatColor.DARK_AQUA + ChatColor.BOLD + " ";
	
	private static final String TRAVEL_TEXT = ChatColor.BLACK + "[" + ChatColor.DARK_PURPLE + "Fast Travel" + ChatColor.BLACK + "]";
	private static final String TRAVEL_LOCATION_TEXT = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "";
	
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
	
	public static void interact(Player player, Block block) {
		Sign sign = (Sign) block.getState();
		String signType = sign.getLine(1);
		
		if(StringUtils.isBlank(signType))
			return;
		else if(signType.contains("Leave")&& player.getWorld().getName().contains("Instance"))
			WauzTeleporter.exitInstanceTeleportManual(player);
		else if(!WauzMode.isMMORPG(player))
			return;
		else if(signType.contains("Locked Door") && player.getWorld().getName().contains("Instance"))
			tryToOpenDoor(player, sign);
		else if(signType.contains("Fast Travel"))
			tryToTravel(player, sign);
	}
	
	private static void tryToOpenDoor(Player player, Sign sign) {
		WauzDebugger.log(player, "Try to Open Door");
		String keyId = StringUtils.substringAfterLast(sign.getLine(2), " ");
		WauzDebugger.log(player, "Key ID: " + keyId);
		
		boolean hasAccess = false;
		
		if(keyId.equals("None"))
			hasAccess = true;
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
		
		BlockFace blockFace = ((org.bukkit.material.Sign) sign.getBlock().getState().getData()).getAttachedFace();
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
	
	private static void atTravelDestination(Entity entity, Location targetLocation) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
	        public void run() {
	        	try {
	        		Location entityLocation = entity.getLocation();
	        		
	    			if(atTravelCoordinate(entityLocation.getY(), targetLocation.getY(), 1) || !hasDragonPlayerMounted(entity.getPassengers())) {
	    				for(Entity passenger : entity.getPassengers()) {
	    					if(passenger instanceof Player)
	    						passenger.leaveVehicle();
	    					else
	    						passenger.remove();
	    				}
	    				entity.remove();
	    			}
	    			else {
	    				WauzPlayerSkillMechanics.spawnParticleHelix(entity.getLocation(), new WauzPlayerSkillParticle(Particle.PORTAL), 1.5, 5);
	    			}

	        		atTravelDestination(entity, targetLocation);
	        	}
	        	catch (NullPointerException e) {
	        		
	        	}
	        }
		}, 10);
	}

	private static boolean atTravelCoordinate(double coord1, double coord2, double maxDiff) {
		return Math.abs(coord1 - coord2) < maxDiff;
	}
	
	public static boolean hasDragonPlayerMounted(List<Entity> passengers) {
		for(Entity passenger : passengers)
			if(passenger instanceof Player)
				return true;
		
		return false;
	}

}
