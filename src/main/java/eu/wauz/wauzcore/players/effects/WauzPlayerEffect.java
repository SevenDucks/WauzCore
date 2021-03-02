package eu.wauz.wauzcore.players.effects;

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
	 * @param duration The remaining duration of the effect in seconds.
	 */
	public WauzPlayerEffect(WauzPlayerEffectType type, int duration) {
		this.type = type;
		this.duration = duration;
	}
	
	/**
	 * @return The type of the effect.
	 */
	public WauzPlayerEffectType getType() {
		return type;
	}

	/**
	 * @return The remaining duration of the effect in seconds.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration The new remaining duration of the effect in seconds.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return The strength of the effect.
	 */
	public int getPower() {
		return power;
	}

	/**
	 * @param power The new strength of the effect.
	 */
	public void setPower(int power) {
		this.power = power;
	}

}
