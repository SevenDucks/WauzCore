package eu.wauz.wauzcore.system;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.calc.SpeedCalculator;

/**
 * Used for debug logging and administrative command mechanics.
 * 
 * @author Wauzmons
 */
public class WauzDebugger {
	
	/**
	 * Sends a debug message to the player, if they have debug mode enabled.
	 * 
	 * @param player The player who should receive the message.
	 * @param message The content of the message.
	 */
	public static void log(Player player, String message) {
		if(player.hasPermission(WauzPermission.DEBUG.toString())) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + "[Debug] " + message);
		}
	}
	
	/**
	 * Sends a debug message to the server console.
	 * 
	 * @param message The content of the message.
	 */
	public static void log(String message) {
		WauzCore.getInstance().getLogger().info("[Debug] " + message);
	}
	
	/**
	 * Sends a catched exception message to the server console.
	 * 
	 * @param clazz The class in which the exception occured.
	 * @param e The catched exception.
	 */
	public static void catchException(Class<?> clazz, Exception e) {
		String trace = e.getStackTrace().length > 0 ? " " + e.getStackTrace()[0] : "";
		String exceptionString = clazz.getName() + " " + e.getClass().getName() + ": (" + e.getMessage() + ")" + trace;
		WauzCore.getInstance().getLogger().warning("[Catcher] Exception in " + exceptionString);
	}
	
	/**
	 * Toggles the general debug mode for a player.
	 * Grants access to the ingame debug log.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleDebugMode(Player player) {
		String permission = WauzPermission.DEBUG.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the magic debug mode for a player.
	 * Grants infinite mana and 1s skill cooldowns.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleMagicDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_MAGIC.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Magic debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the crafting debug mode for a player.
	 * Grants virtually unlimited materials and crafting levels.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleCraftingDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_CRAFTING.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Crafting debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the building debug mode for a player.
	 * Grants building rights in every region.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleBuildingDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_BUILDING.toString();
		boolean hasPermission = !player.hasPermission(permission);
		PermissionAttachment attachment = player.addAttachment(WauzCore.getInstance());
		attachment.setPermission(permission, hasPermission);
		attachment.setPermission(WauzPermission.FAWE_WORLDEDIT_1.toString(), hasPermission);
		attachment.setPermission(WauzPermission.FAWE_WORLDEDIT_2.toString(), hasPermission);
		attachment.setPermission(WauzPermission.FAWE_WORLDEDIT_3.toString(), hasPermission);
		attachment.setPermission(WauzPermission.FAWE_WORLDEDIT_4.toString(), hasPermission);
		attachment.setPermission(WauzPermission.FAWE_VOXELSNIPER.toString(), hasPermission);
		log(player, "Building debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the flying debug mode for a player.
	 * Grants flying rights + increased speed in all modes.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleFlyingDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_FLYING.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Flying debug mode toggled!");
		SpeedCalculator.resetFlySpeed(player);
		return true;
	}
	
	/**
	 * Toggles the attack debug mode for a player.
	 * Grants drastical increase of damage output.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleAttackDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_ATTACK.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Attack debug mode toggled!");
		return true;
	}
	
	/**
	 * Toggles the defense debug mode for a player.
	 * Grants reduction to zero of taken damage.
	 * 
	 * @param player The player that gets their permissions changed.
	 * 
	 * @return If it was successful. (Default true)
	 */
	public static boolean toggleDefenseDebugMode(Player player) {
		String permission = WauzPermission.DEBUG_DEFENSE.toString();
		player.addAttachment(WauzCore.getInstance(), permission, !player.hasPermission(permission));
		log(player, "Defense debug mode toggled!");
		return true;
	}
	
}
