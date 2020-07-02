package eu.wauz.wauzcore.players.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to register, find and select classes.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 */
public class WauzPlayerClassPool {
	
	/**
	 * A map of all registered classes.
	 */
	private static Map<String, WauzPlayerClass> playerClassMap = new HashMap<>();
	
	/**
	 * Gets a class for the given name from the map.
	 * 
	 * @param className The name of the class.
	 * 
	 * @return The class or null, if not found.
	 */
	public static WauzPlayerClass getClass(String className) {
		return playerClassMap.get(className);
	}
	
	/**
	 * @return A list of all classes.
	 */
	public static List<WauzPlayerClass> getAllClasses() {
		return new ArrayList<>(playerClassMap.values());
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
	}

}
