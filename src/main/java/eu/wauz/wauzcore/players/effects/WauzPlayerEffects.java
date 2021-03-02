package eu.wauz.wauzcore.players.effects;

import java.util.ArrayList;
import java.util.List;

/**
 * All the active status effects on a player.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerEffect
 */
public class WauzPlayerEffects {
	
	/**
	 * The list of currently active status effects.
	 */
	private final List<WauzPlayerEffect> effects = new ArrayList<>();
	
	/**
	 * Removes all currently active status effects.
	 */
	public void clearEffects() {
		effects.clear();
	}

}
