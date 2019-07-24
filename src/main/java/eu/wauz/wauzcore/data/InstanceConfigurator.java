package eu.wauz.wauzcore.data;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import net.md_5.bungee.api.ChatColor;

public class InstanceConfigurator extends GlobalConfigurationUtils {

// General Parameters
	
	public static int getMaximumPlayers(String instanceName) {
		int maxPlayers = instanceConfigGetInt(instanceName, "maxplayers");
		return maxPlayers > 0 ? maxPlayers : 5;
	}
	
	public static int getMaximumDeaths(String instanceName) {
		int maxDeaths = instanceConfigGetInt(instanceName, "maxdeaths");
		return maxDeaths > 0 ? maxDeaths : 3;
	}
	
// Command Strings
	
	public static List<String> getBeforeEnterCommands(String instanceName) {
		return instanceConfigGetStringList(instanceName, "before");
	}
	
	public static List<String> getAfterEnterCommands(String instanceName) {
		return instanceConfigGetStringList(instanceName, "after");
	}
	
// Type Specific
	
	public static String getInstanceType(String instanceName) {
		String instanceType = instanceConfigGetString(instanceName, "type");
		return StringUtils.isNotBlank(instanceType) ? instanceType : "Unknown";
	}
	
	public static List<String> getKeyNameList(String instanceName) {
		return instanceConfigGetStringList(instanceName, "keys");
	}
	
	public static final String KEY_STATUS_UNOBTAINED = ChatColor.RED + "Unobtained";
	
	public static final String KEY_STATUS_OBTAINED = ChatColor.YELLOW + "Obtained";
	
	public static final String KEY_STATUS_USED = ChatColor.GREEN + "Used";
	
// World Specific
	
	public static String getInstanceWorldName(World world) {
		return instanceWorldConfigGetString(world, "name");
	}
	
	public static void setInstanceWorldName(World world, String worldName) {
		instanceWorldConfigSet(world, "name", worldName);
	}
	
	public static String getInstanceWorldType(World world) {
		return instanceWorldConfigGetString(world, "type");
	}
	
	public static void setInstanceWorldType(World world, String worldType) {
		instanceWorldConfigSet(world, "type", worldType);
	}
	
	public static int getInstanceWorldMaximumPlayers(World world) {
		return instanceWorldConfigGetInt(world, "maxplayers");
	}
	
	public static void setInstanceWorldMaximumPlayers(World world, int maxPlayers) {
		instanceWorldConfigSet(world, "maxplayers", maxPlayers);
	}
	
	public static int getInstanceWorldMaximumDeaths(World world) {
		return instanceWorldConfigGetInt(world, "maxdeaths");
	}
	
	public static void setInstanceWorldMaximumDeaths(World world, int maxDeaths) {
		instanceWorldConfigSet(world, "maxdeaths", maxDeaths);
	}
	
	public static int getInstanceWorldPlayerDeathCount(World world, Player player) {
		return instanceWorldConfigGetInt(world, "deaths." + player.getUniqueId().toString());
	}
	
	public static void setInstanceWorldPlayerDeathCount(World world, Player player, int deathCount) {
		instanceWorldConfigSet(world, "deaths." + player.getUniqueId().toString(), deathCount);
	}
	
	public static Set<String> getInstanceWorldKeyIds(World world) {
		return instanceWorldConfigGetKeys(world, "keys");
	}
	
	public static String getInstanceKeyStatus(World world, String keyId) {
		return instanceWorldConfigGetString(world, "keys." + keyId);
	}
	
	public static void setInstanceWorldKeyStatus(World world, String keyId, String keyStatus) {
		instanceWorldConfigSet(world, "keys." + keyId, keyStatus);
	}

}