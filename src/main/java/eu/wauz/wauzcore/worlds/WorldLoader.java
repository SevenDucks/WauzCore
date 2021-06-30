package eu.wauz.wauzcore.worlds;

import org.bukkit.WorldCreator;

/**
 * Used to load the worlds for the different gamemodes.
 * 
 * @author Wauzmons
 */
public class WorldLoader {
	
	/**
	 * Loads or creates all worlds that will be needed by the server.
	 * Only called once per server run.
	 */
	public static void init() {
		new WorldCreator("MMORPG").createWorld();
		new SurvivalSeason(new WorldCreator("Survival"), true).createWorld();
		new SurvivalSeason(new EmptyWorldCreator("SurvivalOneBlock"), false).createWorld();
	}

}
