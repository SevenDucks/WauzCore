package eu.wauz.wauzstarter;

import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of this module, used to setup the environment for WauzCore.
 * Responsible for loading worlds and scheduling restarts.
 * 
 * @author Wauzmons
 */
public class WauzStarter extends JavaPlugin {

	/**
	 * The instance of this class, that is created by the Minecraft server.
	 */
	private static WauzStarter instance;

	/**
	 * Gets called when the server is started.
	 * 1. Loads worlds and creates a new Survival / OneBlock world each season.
	 * 2. Initializes the restart scheduler.
	 * 
	 * @see SeasonalSurvivalManager
	 * @see WauzRestartScheduler
	 */
	@Override
	public void onEnable() {
		instance = this;
		getLogger().info("O~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-O");
		getLogger().info(" _    _                                           ");
		getLogger().info("| |  | | WauzStarter v" + getDescription().getVersion());
		getLogger().info("| |  | | __ _ _   _ _____ __ ___   ___  _ __  ___ ");
		getLogger().info("| |/\\| |/ _` | | | |_  / '_ ` _ \\ / _ \\| '_ \\/ __|");
		getLogger().info("\\  /\\  / (_| | |_| |/ /| | | | | | (_) | | | \\__ \\");
		getLogger().info(" \\/  \\/ \\__,_|\\__,_/___|_| |_| |_|\\___/|_| |_|___/");
		getLogger().info("");
		getLogger().info("O-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~O");
		
		getServer().createWorld(new WorldCreator("Wauzland"));
		getServer().createWorld(new WorldCreator("Dalyreos"));
		new SeasonalSurvivalManager(new WorldCreator("Survival"), true).generateSurvivalWorld();
		new SeasonalSurvivalManager(new EmptyWorldCreator("SurvivalOneBlock"), false).generateSurvivalWorld();
		getLogger().info("Created Worlds!");
		
		WauzRestartScheduler.init();
		getLogger().info("Scheduled Restart!");
	}

	/**
	 * @return The instance of this class, that is created by the Minecraft server.
	 */
	public static WauzStarter getInstance() {
		return instance;
	}

}
