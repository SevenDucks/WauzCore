package eu.wauz.wauzstarter;

import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Main Class of this Module, used to setup The Environment for WauzCore.
 * Responsible for loading Worlds and scheduling Restarts.
 * 
 * @author Wauzmons
 */
public class WauzStarter extends JavaPlugin {

	/**
	 * The instance of this Class, that is created by the Minecraft Server.
	 */
	private static WauzStarter instance;

	/**
	 * Gets called when the Server is started.
	 * 1. Loads Worlds and creates a new Survival World each Season.
	 * 2. Initializes the Restart Scheduler.
	 * 
	 * @see SeasonalSurvivalManager
	 * @see WauzRestartScheduler
	 */
	@Override
	public void onEnable() {
		instance = this;
		
		/**
		 * Print the Version
		 */
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
		SeasonalSurvivalManager.generateSurvivalWorld();
		getLogger().info("Created Worlds!");
		
		WauzRestartScheduler.init();
		getLogger().info("Scheduled Restart!");
	}

	/**
	 * @return The instance of this Class, that is created by the Minecraft Server.
	 */
	public static WauzStarter getInstance() {
		return instance;
	}

}
