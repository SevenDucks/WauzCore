package eu.wauz.wauzcore.players.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;

/**
 * This class is used to register, find and select classes.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 */
public class WauzPlayerClassPool {
	
	/**
	 * An ordered list of all classes.
	 */
	private static List<WauzPlayerClass> playerClassList = new ArrayList<>();
	
	/**
	 * A map of all registered classes, indexed by name.
	 */
	private static Map<String, WauzPlayerClass> playerClassMap = new HashMap<>();
	
	/**
	 * @param className The name of a class.
	 * 
	 * @return The class or null, if not found.
	 */
	public static WauzPlayerClass getClass(String className) {
		return playerClassMap.get(className);
	}
	
	/**
	 * @param player The player to get the class of.
	 * 
	 * @return The class of the player.
	 */
	public static WauzPlayerClass getClass(Player player) {
		return getClass(PlayerConfigurator.getCharacterClass(player));
	}
	
	/**
	 * @return A list of all classes.
	 */
	public static List<WauzPlayerClass> getAllClasses() {
		return new ArrayList<>(playerClassList);
	}
	
	/**
	 * @return A list of all class names.
	 */
	public static List<String> getAllClassNames() {
		return new ArrayList<>(playerClassMap.keySet());
	}
	
	/**
	 * @return The count of different classes.
	 */
	public static int getClassCount() {
		return playerClassMap.size();
	}
	
	/**
	 * Registers a class.
	 * 
	 * @param playerClass The class to register.
	 */
	public static void registerClass(WauzPlayerClass playerClass) {
		playerClassMap.put(playerClass.getClassName(), playerClass);
		playerClassList.add(playerClass);
		playerClassList.sort((class1, class2) -> class2.getClassName().compareTo(class1.getClassName()));
	}

}
