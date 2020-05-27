package eu.wauz.wauzcore.oneblock;

/**
 * A level of a phase of the one-block gamemode.
 * 
 * @author Wauzmons
 */
public class OnePhaseLevel {
	
	/**
	 * The phase that the level is part of.
	 */
	private OnePhase phase;
	
	/**
	 * The key of the phase's level.
	 */
	private String levelKey;
	
	/**
	 * The display name of the level.
	 */
	private String levelName;
	
	/**
	 * How many blocks need to be mined, to proceed to the next level.
	 */
	private int blockAmount;
	
	/**
	 * Constructs a level of a phase, based on the one-block file in the /WauzCore folder.
	 * 
	 * @param phase The phase that the level is part of.
	 * @param levelKey The key of the phase's level.
	 */
	public OnePhaseLevel(OnePhase phase, String levelKey) {
		this.phase = phase;
		this.levelKey = levelKey;
	}

	/**
	 * @return The phase that the level is part of.
	 */
	public OnePhase getPhase() {
		return phase;
	}

	/**
	 * @return The key of the phase's level.
	 */
	public String getLevelKey() {
		return levelKey;
	}

	/**
	 * @return The display name of the level.
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * @return How many blocks need to be mined, to proceed to the next level.
	 */
	public int getBlockAmount() {
		return blockAmount;
	}
	
}
