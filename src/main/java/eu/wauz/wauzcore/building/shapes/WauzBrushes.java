package eu.wauz.wauzcore.building.shapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of all available brushes.
 * 
 * @author Wauzmons
 */
public class WauzBrushes {
	
	/**
	 * A map of all brushes, indexed by name.
	 */
	private static Map<String, WauzBrush> brushMap = new HashMap<>();
	
	/**
	 * Gets the brush with the given name.
	 * 
	 * @param brushName The name of the brush.
	 * 
	 * @return The brush or null, if not found.
	 */
	public static WauzBrush getBrush(String brushName) {
		return brushMap.get(brushName);
	}
	
	/**
	 * Gets a list of all brush names.
	 * 
	 * @return The list of all brush names.
	 */
	public static List<String> getAllBrushNames() {
		return new ArrayList<>(brushMap.keySet());
	}
	
	/**
	 * Registers the given brush.
	 * 
	 * @param brush The brush to register.
	 */
	public static void registerBrush(WauzBrush brush) {
		brushMap.put(brush.getName(), brush);
	}

}
