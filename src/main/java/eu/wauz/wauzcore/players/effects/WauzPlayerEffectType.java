package eu.wauz.wauzcore.players.effects;

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
	PVP_PROTECTION("PvP Protection"),
	
	/**
	 * Effect that prevents damage from high temperatures
	 */
	HEAT_RESISTANCE("Heat Resistance"),
	
	/**
	 * Effect that prevents damage from low temperatures.
	 */
	COLD_RESISTANCE("Cold Resistance"),
	
	/**
	 * Effect that regenerates x hitpoints per second.
	 */
	REGENERATION("HP/s Regeneration"),
	
	/**
	 * Effect that boosts attack damage by x percent.
	 */
	ATTACK_BOOST("% Attack Boost"),
	
	/**
	 * Effect that boosts defense by x percent.
	 */
	DEFENSE_BOOST("% Defense Boost"),
	
	/**
	 * Effect that boosts gained exp by x percent.
	 */
	EXP_BOOS("% Experience Boost"),
	
	/**
	 * Effect that boosts evasion chance by x percent.
	 */
	EVASION_CHANCE("% Evasion Chance");
	
	/**
	 * The name of the effect type.
	 */
	private String name;
	
	/**
	 * Creates a new effect type with given name.
	 * 
	 * @param name The name of the effect type.
	 */
	WauzPlayerEffectType(String name) {
		this.name = name;
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

}
