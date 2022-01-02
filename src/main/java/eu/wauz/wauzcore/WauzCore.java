package eu.wauz.wauzcore;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import eu.wauz.wauzcore.commands.WauzCommandExecutor;
import eu.wauz.wauzcore.data.ConfigurationLoader;
import eu.wauz.wauzcore.data.DiscordConfigurator;
import eu.wauz.wauzcore.data.ServerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerRegistrator;
import eu.wauz.wauzcore.system.WauzRepeatingTasks;
import eu.wauz.wauzcore.system.WauzRestartScheduler;
import eu.wauz.wauzcore.system.annotations.AnnotationLoader;
import eu.wauz.wauzcore.system.api.FtpServerManager;
import eu.wauz.wauzcore.system.api.WauzDiscordBot;
import eu.wauz.wauzcore.system.api.WebServerManager;
import eu.wauz.wauzcore.system.listeners.ArmorEquipEventListener;
import eu.wauz.wauzcore.system.listeners.BlockProtectionListener;
import eu.wauz.wauzcore.system.listeners.CitizenListener;
import eu.wauz.wauzcore.system.listeners.InventoryListener;
import eu.wauz.wauzcore.system.listeners.MythicMobsListener;
import eu.wauz.wauzcore.system.listeners.PlayerAmbientListener;
import eu.wauz.wauzcore.system.listeners.PlayerCombatListener;
import eu.wauz.wauzcore.system.listeners.PlayerInteractionListener;
import eu.wauz.wauzcore.system.listeners.PreJoinListener;
import eu.wauz.wauzcore.system.listeners.ProjectileMovementListener;
import eu.wauz.wauzcore.system.listeners.WauzDiscordListener;
import eu.wauz.wauzcore.system.logging.LogFilterManager;
import eu.wauz.wauzcore.system.util.BungeeUtils;
import eu.wauz.wauzcore.worlds.WorldLoader;
import eu.wauz.wauzcore.worlds.instances.InstanceManager;

/**
 * The main class of the plugin and holder of system information.
 * Initializes all static data, listerners, API and repeating tasks.
 * When the server stops it will clean it up again.
 * 
 * @author Wauzmons
 */
public class WauzCore extends JavaPlugin {
	
	/**
	 * The maximum level a player can reach in MMORPG mode.
	 */
	public static final int MAX_PLAYER_LEVEL = 60;
	
	/**
	 * The maximum level a player can reach in Survival mode.
	 */
	public static final int MAX_PLAYER_LEVEL_SURVIVAL = 30;
	
	/**
	 * The maximum crafting skill a player can reach in MMORPG mode.
	 */
	public static final int MAX_CRAFTING_SKILL = 40;
	
	/**
	 * The instance of this class, that is created by the Minecraft server.
	 */
	private static WauzCore instance;
	
	/**
	 * If the core is started.
	 */
	private static boolean isStarted;
	
	/**
	 * The Discord bot running on the server.
	 */
	private static WauzDiscordBot discordBot;
	
	/**
	 * The WebServerManager used for the web based API.
	 */
	private static WebServerManager webServerManager;
	
	/**
	 * The FtpServerManager used for tranfering data.
	 */
	private static FtpServerManager ftpServerManager;
	
	/**
	 * Gets called when the server is started.
	 */
	@Override
	public void onEnable() {
		instance = this;
		isStarted = false;
		LogFilterManager.enableGeneralFilter();
		
		getLogger().info("O~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-O");
		getLogger().info(" _    _                                           ");
		getLogger().info("| |  | | WauzCore v" + getDescription().getVersion());
		getLogger().info("| |  | | __ _ _   _ _____ __ ___   ___  _ __  ___ ");
		getLogger().info("| |/\\| |/ _` | | | |_  / '_ ` _ \\ / _ \\| '_ \\/ __|");
		getLogger().info("\\  /\\  / (_| | |_| |/ /| | | | | | (_) | | | \\__ \\");
		getLogger().info(" \\/  \\/ \\__,_|\\__,_/___|_| |_| |_|\\___/|_| |_|___/");
		getLogger().info("");
		getLogger().info("O-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~O");
		
		getServer().getPluginManager().registerEvents(new PreJoinListener(), this);
		getServer().getScheduler().scheduleSyncDelayedTask(this, instance::setupModules, 100);
	}
	
	/**
	 * Sets up all the modules of the engine.
	 */
	private void setupModules() {
		WorldLoader.init();
		getLogger().info("Finished Loading World Saves!");
		
		ConfigurationLoader.init();
		getLogger().info("Finished Loading Data from Files!");
		
		AnnotationLoader.init();
		getLogger().info("Finished Loading Data from Predefined Classes!");
		
		PluginManager pluginManager = getServer().getPluginManager();
		getServer().getMessenger().registerOutgoingPluginChannel(this, BungeeUtils.BUNGEE_CHANNEL);
		pluginManager.registerEvents(new ArmorEquipEventListener(), this);
		pluginManager.registerEvents(new BlockProtectionListener(), this);
		pluginManager.registerEvents(new CitizenListener(), this);
		pluginManager.registerEvents(new InventoryListener(), this);
		pluginManager.registerEvents(new MythicMobsListener(), this);
		pluginManager.registerEvents(new PlayerAmbientListener(), this);
		pluginManager.registerEvents(new PlayerCombatListener(), this);
		pluginManager.registerEvents(new PlayerInteractionListener(), this);
		pluginManager.registerEvents(new ProjectileMovementListener(), this);
		getLogger().info("Registered EventListeners!");
		
		discordBot = new WauzDiscordBot();
		LogFilterManager.enableDiscordFilter();
		pluginManager.registerEvents(new WauzDiscordListener(), this);
		getLogger().info("Enabled Discord Integration!");
		
		int apiPort = ServerConfigurator.getServerApiPort();
		webServerManager = new WebServerManager(apiPort);
		getLogger().info("Started WebServerManager on port " + apiPort + "!");
		
		int ftpPort = ServerConfigurator.getServerFtpPort();
		ftpServerManager = new FtpServerManager(ftpPort);
		getLogger().info("Started FtpServerManager on port " + ftpPort + "!");
		
		WauzRepeatingTasks.schedule(this);
		getLogger().info("Scheduled Repeating Tasks!");
		
		WauzRestartScheduler.schedule(this);
		getLogger().info("Scheduled Next Restart!");
		
		if(DiscordConfigurator.showStartStopNotification()) {
			discordBot.sendEmbedFromMinecraft(null, ":white_check_mark: " + WauzCore.getServerKey()
					+ " has been started!", null, Color.GREEN, false);
		}
		isStarted = true;
	}

	/**
	 * Gets called when the server is stopped.
	 */
	@Override
	public void onDisable() {
		isStarted = false;
		if(DiscordConfigurator.showStartStopNotification()) {
			discordBot.sendEmbedFromMinecraft(null, ":octagonal_sign: " + WauzCore.getServerKey()
					+ " has been stopped!", null, Color.RED, false);
		}
		LogFilterManager.disableDiscordFilter();
		discordBot.stop();
		getLogger().info("Disabled Discord Integration!");
		
		webServerManager.stop();
		getLogger().info("Stopped WebServerManager!");
		
		ftpServerManager.stop();
		getLogger().info("Stopped FtpServerManager!");
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			WauzPlayerRegistrator.logout(player);
		}
		getLogger().info("Logged Out Players!");
		
		for(World world : Bukkit.getWorlds()) {
			InstanceManager.closeInstance(world);
		}
		getLogger().info("Closed Active Instances!");
		
		LogFilterManager.disableGeneralFilter();
	}

	/**
	 * Listens for incoming commands from the plugin.xml.
	 * Redirects the needed informtation to the command executor.
	 * 
	 * @see WauzCommandExecutor
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return WauzCommandExecutor.execute(sender, cmd, args);
	}

	/**
	 * @return The instance of this class, that is created by the Minecraft server.
	 */
	public static WauzCore getInstance() {
		return instance;
	}
	
	/**
	 * @return If the core is started.
	 */
	public static boolean isStarted() {
		return isStarted;
	}

	/**
	 * @return The Discord bot running on this server.
	 */
	public static WauzDiscordBot getDiscordBot() {
		return discordBot;
	}

	/**
	 * @return The location of the hub, where players start their adventures.
	 */
	public static Location getHubLocation() {
		return new Location(Bukkit.getWorld("HubNexus"), 0.5, 95, 0.5);
	}
	
	/**
	 * Finds all players with cached player data.
	 * 
	 * @return A list of all registered active players.
	 * 
	 * @see WauzPlayerDataPool
	 */
	public static List<Player> getRegisteredActivePlayers() {
		List<Player> players = new ArrayList<Player>();
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(WauzPlayerDataPool.getPlayer(player) != null) {
				players.add(player);
			}
		}
		return players;
	}
	
	/**
	 * Finds an online player by their name.
	 * 
	 * @param name The name of the player.
	 * 
	 * @return The player if they are online, else null.
	 */
	public static Player getOnlinePlayer(String name) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Finds an offline player by their name.
	 * 
	 * @param name The name of the player.
	 * @return The player if they exist, else null.
	 */
	public static OfflinePlayer getOfflinePlayer(String name) {
		for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
			if(player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * @return The key of the server, defined in the .yml configuration.
	 */
	public static String getServerKey() {
		return ServerConfigurator.getServerKey();
	}
	
}
