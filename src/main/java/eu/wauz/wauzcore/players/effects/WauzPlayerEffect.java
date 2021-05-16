package eu.wauz.wauzcore.players.effects;

import org.bukkit.ChatColor;

/**
 * A temporary status effect on a player.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerEffects
 */
public class WauzPlayerEffect {
	
	/**
	 * The type of the effect.
	 */
	private final WauzPlayerEffectType type;
	
	/**
	 * The source of the effect.
	 */
	private final WauzPlayerEffectSource source;
	
	/**
	 * The initial duration of the effect in seconds.
	 */
	private int initialDuration;
	
	/**
	 * The remaining duration of the effect in seconds.
	 */
	private int duration;
	
	/**
	 * The strength of the effect.
	 */
	private int power;
	
	/**
	 * Creates a new temporary status effect on a player.
	 * 
	 * @param type The type of the effect.
	 * @param source The source of the effect.
	 * @param duration The remaining duration of the effect in seconds.
	 * @param power The strength of the effect.
	 */
	public WauzPlayerEffect(WauzPlayerEffectType type, WauzPlayerEffectSource source, int duration, int power) {
		this.type = type;
		this.source = source;
		this.initialDuration = duration;
		this.duration = duration;
		this.power = power;
	}
	
	/**
	 * Reduces the effect duration by 1 second.
	 * 
	 * @return If the effect expired.
	 */
	public boolean reduceDuration() {
		duration--;
		return duration <= 0;
	}
	
	/**
	 * @return The type of the effect.
	 */
	public WauzPlayerEffectType getType() {
		return type;
	}

	/**
	 * @return The source of the effect.
	 */
	public WauzPlayerEffectSource getSource() {
		return source;
	}

	/**
	 * @return The initial duration of the effect in seconds.
	 */
	final int getInitialDuration() {
		return initialDuration;
	}

	/**
	 * @return The remaining duration of the effect in seconds.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @return The strength of the effect.
	 */
	public int getPower() {
		return power;
	}
	
	/**
	 * @return The strength of the effect as decimal number.
	 */
	public double getPowerDecimal() {
		return (double) power / (double) 100.0;
	}
	
	/**
	 * Returns the name of the effect in a scoreboard friendly format.
	 * 
	 * @return The name of the effect.
	 */
	@Override
	public String toString() {
		String prefix = "\n" + type.getColor() + "[" + ChatColor.WHITE;
		String suffix = type.getColor() + "] " + ChatColor.WHITE + duration + "s";
		String sourceName = ChatColor.GRAY + " from " + source.toString();
		String name = (power == 0 ? "" : "" + power) + type.toString();
		return prefix + name + sourceName + suffix;
	}

}
