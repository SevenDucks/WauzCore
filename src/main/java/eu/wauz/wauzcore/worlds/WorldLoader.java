package eu.wauz.wauzcore.worlds;

import org.bukkit.WorldCreator;

import eu.wauz.wauzcore.WauzCore;

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
		loadNormalWorld(new WorldCreator("MMORPG"));
		loadSeasonalWorld(new WorldCreator("Survival"), true);
		loadSeasonalWorld(new EmptyWorldCreator("SurvivalOneBlock"), false);
	}
	
	/**
	 * Loads the given world normally.
	 * 
	 * @param worldCreator The creator for the world.
	 */
	private static void loadNormalWorld(WorldCreator worldCreator) {
		WauzCore.getInstance().getLogger().info(worldCreator.name() + ": World is persistent! Loading world...");
		worldCreator.createWorld();
	}
	
	/**
	 * Loads the given world as season.
	 * 
	 * @param worldCreator The creator for the world.
	 * @param createSpawn If a spawn circle should be created automatically.
	 */
	private static void loadSeasonalWorld(WorldCreator worldCreator, boolean createSpawn) {
		SurvivalSeason season = new SurvivalSeason(worldCreator, createSpawn);
		season.createWorld();
	}

}
