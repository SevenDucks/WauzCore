package eu.wauz.wauzcore.skills.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;

import eu.wauz.wauzcore.system.util.Chance;

/**
 * A collection of default dye colors.
 * 
 * @author Wauzmons
 */
public class Colors {
	
	/**
	 * A map of all default dye colors.
	 */
	private static final Map<String, Color> COLOR_MAP = new HashMap<String, Color>();
	
	/**
	 * A list of all default dye colors.
	 */
	private static final List<Color> COLORS = new ArrayList<>();
	
	/**
	 * Initializes the color map and list.
	 */
	static {
		COLOR_MAP.put("AQUA", Color.AQUA);
		COLOR_MAP.put("BLACK", Color.BLACK);
		COLOR_MAP.put("BLUE", Color.BLUE);
		COLOR_MAP.put("FUCHSIA", Color.FUCHSIA);
		COLOR_MAP.put("GRAY", Color.GRAY);
		COLOR_MAP.put("GREEN", Color.GREEN);
		COLOR_MAP.put("LIME", Color.LIME);
		COLOR_MAP.put("MAROON", Color.MAROON);
		COLOR_MAP.put("NAVY", Color.NAVY);
		COLOR_MAP.put("OLIVE", Color.OLIVE);
		COLOR_MAP.put("ORANGE", Color.ORANGE);
		COLOR_MAP.put("PURPLE", Color.PURPLE);
		COLOR_MAP.put("RED", Color.RED);
		COLOR_MAP.put("SILVER", Color.SILVER);
		COLOR_MAP.put("TEAL", Color.TEAL);
		COLOR_MAP.put("WHITE", Color.WHITE);
		COLOR_MAP.put("YELLOW", Color.YELLOW);
		COLORS.addAll(COLOR_MAP.values());
	}
	
	/**
	 * Gets the color with the given name.
	 * 
	 * @param name The name of the color.
	 * 
	 * @return The color.
	 */
	public static Color getByName(String name) {
		return COLOR_MAP.get(name);
	}
	
	/**
	 * Gets a random color.
	 * 
	 * @return The color.
	 */
	public static Color getRandom() {
		return COLORS.get(Chance.randomInt(COLORS.size()));
	}

}
