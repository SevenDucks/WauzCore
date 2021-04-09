package eu.wauz.wauzcore.skills.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import eu.wauz.wauzcore.system.util.Chance;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * A collection of default dye colors.
 * 
 * @author Wauzmons
 */
public class Colors {
	
	/**
	 * A map of all named component colors, indexed by chat colors.
	 */
	private static final Map<ChatColor, NamedTextColor> COMPONENT_COLOR_MAP = new HashMap<>();
	
	/**
	 * A map of all default dye colors.
	 */
	private static final Map<String, Color> COLOR_MAP = new HashMap<>();
	
	/**
	 * A list of all default dye colors.
	 */
	private static final List<Color> COLORS = new ArrayList<>();
	
	/**
	 * Initializes the color maps and list.
	 */
	static {
		COMPONENT_COLOR_MAP.put(ChatColor.AQUA, NamedTextColor.AQUA);
		COMPONENT_COLOR_MAP.put(ChatColor.BLACK, NamedTextColor.BLACK);
		COMPONENT_COLOR_MAP.put(ChatColor.BLUE, NamedTextColor.BLUE);
		COMPONENT_COLOR_MAP.put(ChatColor.DARK_AQUA, NamedTextColor.DARK_AQUA);
		COMPONENT_COLOR_MAP.put(ChatColor.DARK_BLUE, NamedTextColor.DARK_BLUE);
		COMPONENT_COLOR_MAP.put(ChatColor.DARK_GRAY, NamedTextColor.DARK_GRAY);
		COMPONENT_COLOR_MAP.put(ChatColor.DARK_GREEN, NamedTextColor.DARK_GREEN);
		COMPONENT_COLOR_MAP.put(ChatColor.DARK_PURPLE, NamedTextColor.DARK_PURPLE);
		COMPONENT_COLOR_MAP.put(ChatColor.DARK_RED, NamedTextColor.DARK_RED);
		COMPONENT_COLOR_MAP.put(ChatColor.GOLD, NamedTextColor.GOLD);
		COMPONENT_COLOR_MAP.put(ChatColor.GRAY, NamedTextColor.GRAY);
		COMPONENT_COLOR_MAP.put(ChatColor.GREEN, NamedTextColor.GREEN);
		COMPONENT_COLOR_MAP.put(ChatColor.LIGHT_PURPLE, NamedTextColor.LIGHT_PURPLE);
		COMPONENT_COLOR_MAP.put(ChatColor.RED, NamedTextColor.RED);
		COMPONENT_COLOR_MAP.put(ChatColor.WHITE, NamedTextColor.WHITE);
		COMPONENT_COLOR_MAP.put(ChatColor.YELLOW, NamedTextColor.YELLOW);
		
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
	 * Gets the named component color for the given chat color.
	 * 
	 * @param color The associated chat color.
	 * 
	 * @return The component color.
	 */
	public static NamedTextColor getByChatColor(ChatColor color) {
		return COMPONENT_COLOR_MAP.get(color);
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
