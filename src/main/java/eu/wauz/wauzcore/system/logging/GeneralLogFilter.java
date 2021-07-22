package eu.wauz.wauzcore.system.logging;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.WauzCore;

/**
 * A log filter to exclude unnecessary messages.
 * 
 * @author Wauzmons
 */
public class GeneralLogFilter extends AbstractLogFilter {
	
	/**
	 * Loggers to deny, when they send low level messages.
	 */
	private List<String> ignoredLoggers = Arrays.asList(
			"net.dv8tion.jda",
			"org.reflections",
			"org.apache.ftpserver");
	
	/**
	 * Message starts to deny, that are sent during world loads.
	 */
	private List<String> worldLoadMessageStarts = Arrays.asList(
			"Experience Merge Radius:",
			"Cactus Growth Modifier:",
			"Cane Growth Modifier:",
			"Melon Growth Modifier:",
			"Mushroom Growth Modifier:",
			"Pumpkin Growth Modifier:",
			"Sapling Growth Modifier:",
			"Beetroot Growth Modifier:",
			"Carrot Growth Modifier:",
			"Potato Growth Modifier:",
			"Wheat Growth Modifier:",
			"NetherWart Growth Modifier:",
			"Vine Growth Modifier:",
			"Cocoa Growth Modifier:",
			"Bamboo Growth Modifier:",
			"SweetBerry Growth Modifier:",
			"Kelp Growth Modifier:",
			"Entity Activation Range:",
			"Hopper Transfer:",
			"Custom Map Seeds:",
			"Max TNT Explosions:",
			"Tile Max Tick Time:",
			"Entity Tracking Range:",
			"Allow Zombie Pigmen to spawn from portal blocks:",
			"Item Merge Radius:",
			"Item Despawn Rate:",
			"View Distance:",
			"Arrow Despawn Rate:",
			"Zombie Aggressive Towards Villager:",
			"Nerfing mobs spawned from spawners:",
			"Mob Spawn Range:",
			"Unable to find spawn biome",
			"Loaded 0 spawn chunks for world",
			"Preparing start region for dimension",
			"Preparing spawn area:",
			"Time elapsed:",
			"Named entity ",
			"Loaded class io.lumine.xikage.mythicmobs.MythicMobs",
			"NPCLib Attempting to inject into netty",
			"NPCLib Enabled for Minecraft");

	/**
	 * Applies the filter to the given log event.
	 * 
	 * @param logEvent The log event.
	 * 
	 * @return The filter result.
	 */
	@Override
	public Result apply(LogEvent logEvent) {
		if(logEvent.getLevel().isLessSpecificThan(Level.INFO)) {
			for(String loggerName : ignoredLoggers) {
				if(logEvent.getLoggerName().startsWith(loggerName)) {
					return Result.DENY;
				}
			}
		}
		
		String message = logEvent.getMessage().getFormattedMessage();
		for(String ignored : worldLoadMessageStarts) {
			if(StringUtils.startsWith(message, ignored)) {
				return Result.DENY;
			}
		}
		if(StringUtils.startsWith(message, "-------- World Settings For [")) {
			String worldName = StringUtils.substringBetween(message, "[", "]");
			WauzCore.getInstance().getLogger().info(worldName + ": World loaded!");
			return Result.DENY;
		}
		return Result.NEUTRAL;
	}

}
