package eu.wauz.wauzcore.system;

import java.io.File;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.instances.InstanceManager;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * Used to handle different types of teleportation of players.
 * 
 * @author Wauzmons
 */
public class WauzTeleporter {
	
	/**
	 * Teleports the player to the hub.
	 * The player can manually call this method.
	 * 
	 * @param player The player to teleport.
	 */
	public static void hubTeleportManual(Player player) {
		player.closeInventory();
		CharacterManager.logoutCharacter(player);
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
	/**
	 * Teleports the player to the spawn.
	 * The player can manually call this method.
	 * Not usable when no character is selected.
	 * Not usable when mounted.
	 * 
	 * @param player The player to teleport.
	 */
	public static void spawnTeleportManual(Player player) {
		player.closeInventory();
		if(!WauzPlayerDataPool.isCharacterSelected(player)) {
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
			return;
		}
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return;
		}
		WauzActivePet.tryToUnsummon(player, true);
		player.teleport(PlayerConfigurator.getCharacterSpawn(player));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
	/**
	 * Teleports the player to an instance.
	 * The player can manually call this method.
	 * Only callable by using instance map items.
	 * Not usable when mounted.
	 * Not usable inside instances.
	 * 
	 * @param event The player event.
	 */
	public static void enterInstanceTeleportManual(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(!ItemUtils.isInstanceMap(event.getItem())) {
			return;
		}
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return;
		}
		if(player.getWorld().getWorldFolder().toString().contains("Instance")) {
			player.sendMessage(ChatColor.RED + "You can't enter Maps inside an instance!");
			return;
		}	
		String type = ItemUtils.getInstanceMapType(event.getItem());
		String name = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());
		event.getItem().setAmount(event.getItem().getAmount() - 1);
		
		if(type.contains("Survival")) {
			InstanceManager.enter(player, type);
		}
		else {
			InstanceManager.enter(player, name);	
		}
	}
		
	/**
	 * Teleports the player to out of an instance.
	 * The player can manually call this method.
	 * Not usable when mounted.
	 * 
	 * @param player The player to teleport.
	 */
	public static void exitInstanceTeleportManual(Player player) {
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return;
		}
		if(WauzMode.isArcade(player)) {
			CharacterManager.logoutCharacter(player);
			return;
		}
		if(!WauzPlayerDataPool.isCharacterSelected(player) || StringUtils.equals(player.getWorld().getName(), PlayerConfigurator.getCharacterWorldString(player))) {
			player.sendMessage(ChatColor.RED + "You can't leave when not inside an instance!");
			return;
		}
		WauzActivePet.tryToUnsummon(player, true);
		player.teleport(PlayerConfigurator.getCharacterLocation(player));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
	/**
	 * Teleports the player to a development instance (actually any world).
	 * Only callable with system permissions.
	 * Always teleports to 0 5 0 coordinates.
	 * Not usable when mounted.
	 * 
	 * @param player The player to teleport.
	 * @param instanceName The world name to teleport to.
	 * 
	 * @return True if successful.
	 */
	public static boolean enterInstanceTeleportSystemDev(Player player, String instanceName) {
		File file = new File(Bukkit.getWorld("MMORPG").getWorldFolder().getPath().toString()
				.replaceAll("MMORPG", instanceName));
		
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return false;
		}
		if(!file.exists()) {
			player.sendMessage(ChatColor.RED + "The world " + file.getPath() + " does not exist!");
			return false;
		}
		CharacterManager.logoutCharacter(player);
		return player.teleport(new Location(Bukkit.getServer().createWorld(new WorldCreator(instanceName)), 0.5, 256, 0.5));
	}
	
	/**
	 * Teleports the player to a new normal instance.
	 * Only callable with system permissions.
	 * Not usable when mounted.
	 * 
	 * @param player The player to teleport.
	 * @param instanceName The world name to teleport to.
	 * 
	 * @return True if successful.
	 */
	public static boolean enterInstanceTeleportSystem(Player player, String instanceName) {
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return false;
		}
		return InstanceManager.enter(player, instanceName);
	}
	
	/**
	 * Teleports the player to another player.
	 * The player can manually call this method.
	 * Not usable when mounted.
	 * Not usable when player is in another gamemode.
	 * Not usable when player is in another (non instance) world.
	 * Not usable when player is in another guildhall.
	 * Not usable when the instance is full.
	 * Not usable when died too much in the instance.
	 * 
	 * @param player The player to teleport.
	 * @param target The target player.
	 */
	public static void playerTeleportManual(Player player, Player target) {
		String targetWorldName = target.getWorld().getName();
		String playerWorldName = PlayerConfigurator.getCharacterWorldString(player);
		
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			player.closeInventory();
			return;
		}
		if(!Objects.equals(WauzMode.getMode(player), WauzMode.getMode(target))) {
			player.sendMessage(ChatColor.RED + "You can only teleport to players in the same gamemode!");
			player.closeInventory();
			return;
		}
		if(!targetWorldName.contains("Instance") && !targetWorldName.equals(playerWorldName)) {
			player.sendMessage(ChatColor.RED + "You can only teleport to players in instances or the same world!");
			player.closeInventory();
			return;
		}
		WauzPlayerGuild targetGuild = PlayerConfigurator.getGuild(target);
		if(targetGuild != null && targetWorldName.equals("WzInstance_MMORPG_" + targetGuild.getGuildUuidString())) {
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
			if(playerGuild == null || playerGuild.getGuildUuidString().equals(targetGuild.getGuildUuidString())) {
				player.sendMessage(ChatColor.RED + "This instance is only for Guild-Members!");
				player.closeInventory();
				return;
			}
				
		}
		if(targetWorldName.contains("Instance")) {
			WauzActiveInstance instance = WauzActiveInstancePool.getInstance(target);
			int maxPlayers = instance.getMaxPlayers();
			if(maxPlayers > 0 && instance.getWorld().getPlayers().size() >= maxPlayers) {
				player.sendMessage(ChatColor.RED + "This instance is already full!");
				player.closeInventory();
				return;
			}
			int maxDeaths = instance.getMaxDeaths();
			if(maxDeaths > 0 && instance.getPlayerDeaths(player) >= maxDeaths) {
				player.sendMessage(ChatColor.RED + "You already died too often in this instance!");
				player.closeInventory();
				return;
			}
		}
		CharacterManager.saveCharacter(player);
		WauzActivePet.tryToUnsummon(player, true);
		player.teleport(target.getLocation());
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
	/**
	 * Teleports the player per command.
	 * The player can manually call this method.
	 * Not usable when mounted.
	 * Not usable when in hub.
	 * Not usable when location is in another gamemode.
	 * Not usable when location is in another (non instance) world.
	 * 
	 * @param event The teleport event.
	 */
	public static void commandTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		
		if(player.isInsideVehicle()) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return;
		}
		World sourceWorld = event.getFrom().getWorld();
		World targetWorld = event.getTo().getWorld();
		String sourceWorldName = sourceWorld.getName();
		String targetWorldName = targetWorld.getName();
		if(WauzMode.inHub(sourceWorld)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
			return;
		}
		if(!Objects.equals(WauzMode.getMode(sourceWorldName), WauzMode.getMode(targetWorldName))) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can only teleport to locations in the same gamemode!");
			return;
		}
		if(!targetWorldName.contains("Instance") && !targetWorldName.equals(sourceWorldName)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can only teleport to locations in instances or the same world!");
			return;
		}
	}
	
	/**
	 * Teleports the player to their home.
	 * The player can manually call this method.
	 * Not usable when no character is selected.
	 * Not usable when mounted.
	 * Not usable when player has no home.
	 * 
	 * @param player The player to teleport.
	 */
	public static void hearthstoneTeleport(Player player) {
		player.closeInventory();
		if(!WauzPlayerDataPool.isCharacterSelected(player) || WauzMode.inOneBlock(player)) {
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
			return;
		}
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return;
		}
		if(StringUtils.isBlank(PlayerConfigurator.getCharacterHearthstoneRegion(player))) {
			player.sendMessage(ChatColor.RED
					+ "You have no home!" + (WauzMode.isMMORPG(player)
					? " Talk to an Inkeeper to change that!"
					: " Use /sethome to change that!"));
			return;
		}
		WauzActivePet.tryToUnsummon(player, true);
		player.teleport(PlayerConfigurator.getCharacterHearthstone(player));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
	/**
	 * Teleports the player to a waypoint.
	 * The player can manually call this method.
	 * Not usable when player is in a not MMORPG world.
	 * Not usable when mounted.
	 * Not usable with invalid waypoint key.
	 * 
	 * @param player The player to teleport.
	 * @param waypointKey The key of the waypoint.
	 */
	public static void waypointTeleport(Player player, String waypointKey) {
		if(!WauzMode.isMMORPG(player) || WauzMode.inHub(player)) {
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
			return;
		}
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return;
		}
		WauzWaypoint waypoint = WauzWaypoint.getWaypoint(waypointKey);
		if(waypoint == null) {
			player.sendMessage(ChatColor.RED + "This waypoint is unknown!");
			return;
		}
		WauzActivePet.tryToUnsummon(player, true);
		CharacterManager.saveCharacter(player);
		player.teleport(waypoint.getWaypointLocation());
		player.sendMessage(ChatColor.GREEN + "You were warped to '" + waypoint.getWaypointDisplayName() + "'!");
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
	/**
	 * Teleports the player to an event.
	 * The player can manually call this method.
	 * Not usable when event already ended.
	 * Not usable when player is in another world.
	 * Not usable when mounted.
	 * 
	 * @param player The player to teleport.
	 * @param location The event location.
	 */
	public static void eventTeleport(Player player, Location location) {
		player.closeInventory();
		
		if(location == null) {
			player.sendMessage(ChatColor.RED + "This teleport link is already expired!");
			return;
		}
		if(!location.getWorld().equals(player.getWorld())) {
			player.sendMessage(ChatColor.RED + "You are in the wrong world for this teleport!");
			return;
		}
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return;
		}
		WauzActivePet.tryToUnsummon(player, true);
		player.teleport(location);
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}

}
