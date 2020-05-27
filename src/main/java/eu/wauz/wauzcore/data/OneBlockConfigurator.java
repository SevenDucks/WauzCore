package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.EntityType;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;
import eu.wauz.wauzcore.oneblock.OneChestType;

/**
 * Configurator to fetch or modify data from the OneBlock.yml.
 * 
 * @author Wauzmons
 */
public class OneBlockConfigurator extends GlobalConfigurationUtils {
	
// Keys
	
	/**
	 * @return The keys of all one-block phases.
	 */
	public static List<String> getAllPhaseKeys() {
		return new ArrayList<>(mainConfigGetKeys("OneBlock", "phases"));
	}
	
	/**
	 * @param phaseKey The key of the phase.
	 * 
	 * @return The keys of all levels of the phase.
	 */
	public static List<String> getPhaseLevelKeys(String phaseKey) {
		return new ArrayList<>(mainConfigGetKeys("OneBlock", "phases. " + phaseKey + ".levels"));
	}
	
// Names
	
	/**
	 * @param phaseKey The key of the phase.
	 * 
	 * @return The display name of the phase.
	 */
	public static String getPhaseName(String phaseKey) {
		return mainConfigGetString("OneBlock", "phases. " + phaseKey + ".name");
	}
	
	/**
	 * @param phaseKey The key of the phase.
	 * @param levelKey The key of the phase's level.
	 * 
	 * @return The display name of the level.
	 */
	public static String getPhaseLevelName(String phaseKey, String levelKey) {
		return mainConfigGetString("OneBlock", "phases. " + phaseKey + ".levels." + levelKey + ".name");
	}
	
// Mobs
	
	/**
	 * @param phaseKey The key of the phase.
	 * 
	 * @return The chance that a mob spawns, when a block is destroyed.
	 */
	public static double getMobSpawnChance(String phaseKey) {
		return mainConfigGetDouble("OneBlock", "phases. " + phaseKey + ".mobs.spawnchance");
	}
	
	/**
	 * @param phaseKey The key of the phase.
	 * 
	 * @return The chance of it being hostile, when a mob spawns.
	 */
	public static double getMobHostileChance(String phaseKey) {
		return mainConfigGetDouble("OneBlock", "phases. " + phaseKey + ".mobs.hostilechance");
	}
	
	/**
	 * @param phaseKey The key of the phase.
	 * @param hostile If hostile mobs should be listed, instead of passive ones.
	 * 
	 * @return The list of mobs, spawning in the phase.
	 */
	public static List<EntityType> getMobs(String phaseKey, boolean hostile) {
		String mobCategory = hostile ? "hostile" : "passive";
		List<String> mobNames = mainConfigGetStringList("OneBlock", "phases. " + phaseKey + ".mobs." + mobCategory);
		return mobNames.stream()
				.map(mob -> EntityType.valueOf(mob))
				.collect(Collectors.toList());
	}
	
// Chests
	
	/**
	 * @param phaseKey The key of the phase.
	 * @param chestType The type of the chest.
	 * 
	 * @return How many item stacks are contained in the chest.
	 */
	public static int getChestStackCount(String phaseKey, OneChestType chestType) {
		return mainConfigGetInt("OneBlock", "phases. " + phaseKey + ".chests." + chestType.toString() + ".stackcount");
	}
	
	/**
	 * @param phaseKey The key of the phase.
	 * @param chestType The type of the chest.
	 * 
	 * @return A list of strings, representing the possible items in the chest.
	 */
	public static List<String> getChestContentStrings(String phaseKey, OneChestType chestType) {
		return mainConfigGetStringList("OneBlock", "phases. " + phaseKey + ".chests." + chestType.toString() + ".content");
	}
	
// Blocks
	
	/**
	 * @param The key of the phase.
	 * @param levelKey The key of the phase's level.
	 * 
	 * @return How many blocks need to be mined, to proceed to the next level.
	 */
	public static int getPhaseLevelBlockAmount(String phaseKey, String levelKey) {
		return mainConfigGetInt("OneBlock", "phases. " + phaseKey + ".levels." + levelKey + ".blockamount");
	}
	
	/**
	 * @param The key of the phase.
	 * @param levelKey The key of the phase's level.
	 * 
	 * @return A list of strings, representing the possible blocks spawning in the level.
	 */
	public static List<String> getPhaseLevelBlocks(String phaseKey, String levelKey) {
		return mainConfigGetStringList("OneBlock", "phases. " + phaseKey + ".levels." + levelKey + ".blocks");
	}

}
