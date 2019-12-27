package eu.wauz.wauzcore.data;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import net.md_5.bungee.api.ChatColor;

/**
 * Configurator to fetch or modify data from the Instance.yml files.
 * 
 * @author Wauzmons
 */
public class InstanceConfigurator extends GlobalConfigurationUtils {

// General Parameters
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The maximum players of the instance.
	 */
	public static int getMaximumPlayers(String instanceName) {
		int maxPlayers = instanceConfigGetInt(instanceName, "maxplayers");
		return maxPlayers > 0 ? maxPlayers : 5;
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The maximum deaths per player of the instance.
	 */
	public static int getMaximumDeaths(String instanceName) {
		int maxDeaths = instanceConfigGetInt(instanceName, "maxdeaths");
		return maxDeaths > 0 ? maxDeaths : 3;
	}
	
// Command Strings
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The commands to execute before entering the instance.
	 */
	public static List<String> getBeforeEnterCommands(String instanceName) {
		return instanceConfigGetStringList(instanceName, "before");
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The commands to execute agter entering the instance.
	 */
	public static List<String> getAfterEnterCommands(String instanceName) {
		return instanceConfigGetStringList(instanceName, "after");
	}
	
// Type Specific
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The type of the instance.
	 */
	public static String getInstanceType(String instanceName) {
		String instanceType = instanceConfigGetString(instanceName, "type");
		return StringUtils.isNotBlank(instanceType) ? instanceType : "Unknown";
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The list of key names of the instance.
	 */
	public static List<String> getKeyNameList(String instanceName) {
		return instanceConfigGetStringList(instanceName, "keys");
	}
	
	/**
	 * The status name of an ubobtained key.
	 */
	public static final String KEY_STATUS_UNOBTAINED = ChatColor.RED + "Unobtained";
	
	/**
	 * The status name of an obtained key.
	 */
	public static final String KEY_STATUS_OBTAINED = ChatColor.YELLOW + "Obtained";
	
	/**
	 * The status name of an used key.
	 */
	public static final String KEY_STATUS_USED = ChatColor.GREEN + "Used";
	
// World Specific
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The name of the instance world.
	 */
	public static String getInstanceWorldName(World world) {
		return instanceWorldConfigGetString(world, "name");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param worldName The new name of the instance world.
	 */
	public static void setInstanceWorldName(World world, String worldName) {
		instanceWorldConfigSet(world, "name", worldName);
	}
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The type of the instance world.
	 */
	public static String getInstanceWorldType(World world) {
		return instanceWorldConfigGetString(world, "type");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param worldType The new type of the instance world.
	 */
	public static void setInstanceWorldType(World world, String worldType) {
		instanceWorldConfigSet(world, "type", worldType);
	}
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The maximum players of the instance world.
	 */
	public static int getInstanceWorldMaximumPlayers(World world) {
		return instanceWorldConfigGetInt(world, "maxplayers");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param maxPlayers The new maximum players of the instance world.
	 */
	public static void setInstanceWorldMaximumPlayers(World world, int maxPlayers) {
		instanceWorldConfigSet(world, "maxplayers", maxPlayers);
	}
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The maximum deaths per player of the instance world.
	 */
	public static int getInstanceWorldMaximumDeaths(World world) {
		return instanceWorldConfigGetInt(world, "maxdeaths");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param maxDeaths The new maximum deaths per player of the instance world.
	 */
	public static void setInstanceWorldMaximumDeaths(World world, int maxDeaths) {
		instanceWorldConfigSet(world, "maxdeaths", maxDeaths);
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param player The player that may have died.
	 * 
	 * @return The amount of times a player died in the instance world.
	 */
	public static int getInstanceWorldPlayerDeathCount(World world, Player player) {
		return instanceWorldConfigGetInt(world, "deaths." + player.getUniqueId().toString());
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param player The player that may have died.
	 * @param deathCount The new amount of times a player died in the instance world.
	 */
	public static void setInstanceWorldPlayerDeathCount(World world, Player player, int deathCount) {
		instanceWorldConfigSet(world, "deaths." + player.getUniqueId().toString(), deathCount);
	}
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The list of key names of the instance world.
	 */
	public static Set<String> getInstanceWorldKeyIds(World world) {
		return instanceWorldConfigGetKeys(world, "keys");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param keyId The name of the key.
	 * 
	 * @return The status of the key.
	 */
	public static String getInstanceKeyStatus(World world, String keyId) {
		return instanceWorldConfigGetString(world, "keys." + keyId);
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param keyId The name of the key.
	 * @param keyStatus The new status of the key.
	 */
	public static void setInstanceWorldKeyStatus(World world, String keyId, String keyStatus) {
		instanceWorldConfigSet(world, "keys." + keyId, keyStatus);
	}

}
