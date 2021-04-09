package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.instances.WauzInstanceKeyStatus;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

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
	 * List of valid formats, were --- stands for empty rows:<br/><br/>
	 * 
	 * --------------------<br/>
	 * [Leave]<br/>
	 * --------------------<br/>
	 * --------------------<br/><br/>
	 * 
	 * --------------------<br/>
	 * [Locked Door]<br/>
	 * Key: KeyName<br/>
	 * --------------------<br/><br/>
	 * 
	 * --------------------<br/>
	 * [Fast Travel]<br/>
	 * WaypointName<br/>
	 * --------------------
	 * 
	 * @param event The sign event.
	 */
	public static void create(SignChangeEvent event) {	
		String[] lines = Components.lines(event).toArray(new String[0]);
		
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
		
		Components.lines(event, Arrays.asList(lines));
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
		String signType = Components.line(sign, 1);
		
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
	 * @see WauzActiveInstance#getKeyStatus(String)
	 * @see WauzActiveInstance#setKeyStatus(String, WauzInstanceKeyStatus)
	 */
	private static void tryToOpenDoor(Player player, Sign sign) {
		WauzDebugger.log(player, "Try to Open Door");
		String keyId = StringUtils.substringAfterLast(Components.line(sign, 2), " ");
		WauzDebugger.log(player, "Key ID: " + keyId);
		
		boolean hasAccess = false;
		
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(player);
		if(keyId.equals("None")) {
			hasAccess = true;
		}
		else {
			WauzInstanceKeyStatus keyStatus = instance.getKeyStatus(keyId);
			hasAccess =
				keyStatus.equals(WauzInstanceKeyStatus.OBTAINED) ||
				keyStatus.equals(WauzInstanceKeyStatus.USED);
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
		
		instance.setKeyStatus(keyId, WauzInstanceKeyStatus.USED);
		for(Player member : instance.getWorld().getPlayers()) {
			WauzPlayerScoreboard.scheduleScoreboardRefresh(member);
			member.sendMessage(ChatColor.GREEN + "The door \"" + ChatColor.DARK_AQUA + keyId + ChatColor.GREEN + "\" was unlocked!");
		}
	}
	
	/**
	 * Tries to travel to a waypoint, based on the waypoint name on a sign.
	 * 
	 * @param player The player who wants to travel.
	 * @param sign The sign at the current travel station.
	 * 
	 * @see WauzTeleporter#waypointTeleport(Player, String)
	 */
	private static void tryToTravel(Player player, Sign sign) {
		WauzDebugger.log(player, "Try to Travel");
		String waypointKey = StringUtils.substringAfterLast(Components.line(sign, 2), TRAVEL_LOCATION_TEXT);
		WauzDebugger.log(player, "Waypoint ID: " + waypointKey);
		
		WauzTeleporter.waypointTeleport(player, waypointKey);
	}

}
