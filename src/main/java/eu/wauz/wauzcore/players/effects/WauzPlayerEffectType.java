package eu.wauz.wauzcore.players.effects;

import org.bukkit.ChatColor;

/**
 * The type of a temporary status effect on a player.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerEffect
 */
public enum WauzPlayerEffectType {
	
	/**
	 * Effect that pervents engaging in PvP.
	 */
	PVP_PROTECTION("PvP Protection", ChatColor.YELLOW),
	
	/**
	 * Effect that prevents damage from high temperatures
	 */
	HEAT_RESISTANCE("Heat Resistance", ChatColor.DARK_RED),
	
	/**
	 * Effect that prevents damage from low temperatures.
	 */
	COLD_RESISTANCE("Cold Resistance", ChatColor.DARK_AQUA),
	
	/**
	 * Effect that boosts attack damage by x percent.
	 */
	ATTACK_BOOST("% Attack Boost", ChatColor.GOLD),
	
	/**
	 * Effect that boosts defense by x percent.
	 */
	DEFENSE_BOOST("% Defense Boost", ChatColor.BLUE),
	
	/**
	 * Effect that boosts gained exp by x percent.
	 */
	EXP_BOOST("% Exp Boost", ChatColor.LIGHT_PURPLE),
	
	/**
	 * Effect that boosts evasion chance by x percent.
	 */
	EVASION_CHANCE("% Evasion Chance", ChatColor.AQUA);
	
	/**
	 * The name of the effect type.
	 */
	private String name;
	
	/**
	 * The color of the effect type.
	 */
	private ChatColor color;
	
	/**
	 * Creates a new effect type with given name.
	 * 
	 * @param name The name of the effect type.
	 * @param color The color of the effect type.
	 */
	WauzPlayerEffectType(String name, ChatColor color) {
		this.name = name;
		this.color = color;
	}
	
	/**
	 * Returns the name of the effect type in a title friendly format.
	 * 
	 * @return The name of the effect type.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return The color of the effect type.
	 */
	public ChatColor getColor() {
		return color;
	}

}
