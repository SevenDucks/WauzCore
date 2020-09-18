package eu.wauz.wauzpets;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of this module, used to implement a pet system.
 * Responsible for spawning and managing pets.
 * 
 * @author Wauzmons
 */
public class WauzPets extends JavaPlugin {

	/**
	 * The instance of this class, that is created by the Minecraft server.
	 */
	private static WauzPets instance;

	/**
	 * Gets called when the server is started.
	 */
	@Override
	public void onEnable() {
		instance = this;
		
		/**
		 * Print the version
		 */
		getLogger().info("O~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-O");
		getLogger().info(" _    _                                           ");
		getLogger().info("| |  | | WauzPets Standalone Module from WauzCore");
		getLogger().info("| |  | | __ _ _   _ _____ __ ___   ___  _ __  ___ ");
		getLogger().info("| |/\\| |/ _` | | | |_  / '_ ` _ \\ / _ \\| '_ \\/ __|");
		getLogger().info("\\  /\\  / (_| | |_| |/ /| | | | | | (_) | | | \\__ \\");
		getLogger().info(" \\/  \\/ \\__,_|\\__,_/___|_| |_| |_|\\___/|_| |_|___/");
		getLogger().info("");
		getLogger().info("O-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~O");
	}

	/**
	 * @return The instance of this class, that is created by the Minecraft server.
	 */
	public static WauzPets getInstance() {
		return instance;
	}

}
