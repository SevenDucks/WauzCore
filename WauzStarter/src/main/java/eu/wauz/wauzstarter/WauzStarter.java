package eu.wauz.wauzstarter;

import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public class WauzStarter extends JavaPlugin {

	private static WauzStarter instance;

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
		SeasonalSurvivalManager.generateSurvivalWorld();
		getLogger().info("Created Worlds!");
		
		WauzRestartScheduler.init();
		getLogger().info("Scheduled Restart!");
	}

	public static WauzStarter getInstance() {
		return instance;
	}

}
