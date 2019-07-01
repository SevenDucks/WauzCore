package eu.wauz.wauzcore.system;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzTeleporter {
	
// Spawn and Hub
	
	public static boolean hubTeleportManual(Player player) {
		player.closeInventory();
		
		// Handles TamingMenu.unsummon()
		CharacterManager.logoutCharacter(player);
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
		return true;
	}
	
	public static boolean spawnTeleportManual(Player player) {
		player.closeInventory();
		if(!WauzPlayerDataPool.isCharacterSelected(player)) {
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
			return true;
		}
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return true;
		}
		if(WauzMode.isMMORPG(player)) {
			PetOverviewMenu.unsummon(player);
		}
		player.teleport(PlayerConfigurator.getCharacterSpawn(player));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
		return true;
	}
	
// Instances - Manual
	
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
		String name = event.getItem().getItemMeta().getDisplayName().replaceAll("" + ChatColor.RESET, "");
		event.getItem().setAmount(event.getItem().getAmount() - 1);
		
		// Handles TamingMenu.unsummon() and Effect.PORTAL_TRAVEL
		if(type.contains("Survival"))
			InstanceManager.enter(player, type);
		else if(name.endsWith("Shrine"))
			return;
		else
			InstanceManager.enter(player, name);	
	}
		
	public static void exitInstanceTeleportManual(Player player) {
		Location destination = PlayerConfigurator.getCharacterLocation(player);		
		player.closeInventory();
		
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return;
		}
		PetOverviewMenu.unsummon(player);
		player.teleport(destination);
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
// Instances Development
	
	public static boolean enterInstanceTeleportBetaDev(Player player, String instanceName) {
		File file = new File(Bukkit.getWorld("Wauzland").getWorldFolder().getPath().toString()
				.replaceAll("Wauzland", instanceName));
		
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return false;
		}
		if(!file.exists()) {
			player.sendMessage(ChatColor.RED + "The world " + file.getPath() + " does not exist!");
			return false;
		}
		CharacterManager.saveCharacter(player);
		
		PetOverviewMenu.unsummon(player);
		return player.teleport(new Location(Bukkit.getServer().createWorld(new WorldCreator(instanceName)), 0.5, 5, 0.5));
	}
	
	public static boolean enterInstanceTeleportDev(Player player, String instanceName) {
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return false;
		}
		// Handles TamingMenu.unsummon()
		return InstanceManager.enter(player, instanceName);
	}
	
	public static boolean exitInstanceTeleportDev(Player player) {
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return false;
		}
		PetOverviewMenu.unsummon(player);
		return player.teleport(PlayerConfigurator.getCharacterLocation(player));
	}
	
// Player Groups
	
	public static void playerTeleportManual(Player player, Player target) {
		String targetWorldName = target.getWorld().getName();
		String playerWorldName = PlayerConfigurator.getCharacterWorldString(player);
		
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
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
			World world = target.getWorld();
			int maxPlayers = InstanceConfigurator.getInstanceWorldMaximumPlayers(world);
			if(maxPlayers > 0 && world.getPlayers().size() >= maxPlayers) {
				player.sendMessage(ChatColor.RED + "This instance is already full!");
				player.closeInventory();
				return;
			}
			int maxDeaths = InstanceConfigurator.getInstanceWorldMaximumDeaths(world);
			if(maxDeaths > 0 && InstanceConfigurator.getInstanceWorldPlayerDeathCount(world, player) >= maxDeaths) {
				player.sendMessage(ChatColor.RED + "You already died too much in this instance!");
				player.closeInventory();
				return;
			}
		}
		if(!playerWorldName.contains("Instance")) {
			PlayerConfigurator.setCharacterLocation(player, player.getLocation());
		}
		PetOverviewMenu.unsummon(player);
		player.teleport(target.getLocation());
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}
	
// Hearthstone
	
	public static boolean hearthstoneTeleport(Player player) {
		player.closeInventory();
		if(!WauzPlayerDataPool.isCharacterSelected(player)) {
			player.sendMessage(ChatColor.RED + "You can't do that in this world!");
			return true;
		}
		if(player.isInsideVehicle()) {
			player.sendMessage(ChatColor.RED + "You can't warp while mounted!");
			return true;
		}
		if(StringUtils.isBlank(PlayerConfigurator.getCharacterHearthstoneRegion(player))) {
			player.sendMessage(ChatColor.RED
					+ "You have no home!" + (WauzMode.isMMORPG(player)
					? " Talk to an Inkeeper to change that!"
					: " Use /sethome to change that!"));
			return true;
		}
		if(WauzMode.isMMORPG(player)) {
			PetOverviewMenu.unsummon(player);
		}
		player.teleport(PlayerConfigurator.getCharacterHearthstone(player));
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
		return true;
	}
	
// Event
	
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
		PetOverviewMenu.unsummon(player);
		player.teleport(location);
		player.getWorld().playEffect(player.getLocation(), Effect.PORTAL_TRAVEL, 0);
	}

}
