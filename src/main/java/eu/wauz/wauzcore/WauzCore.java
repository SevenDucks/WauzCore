package eu.wauz.wauzcore;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.data.RegionConfigurator;
import eu.wauz.wauzcore.events.ArmorEquipEventListener;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerRegistrator;
import eu.wauz.wauzcore.players.calc.ClimateCalculator;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.players.ui.WauzPlayerNotifier;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.InstanceManager;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.api.WebServerManager;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

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
	public static final int MAX_PLAYER_LEVEL = 30;
	
	/**
	 * The maximum level a player can reach in Survival mode.
	 */
	public static final int MAX_PLAYER_LEVEL_SURVIVAL = 30;
	
	/**
	 * The maximum crafting skill a player can reach in MMORPG mode.
	 */
	public static final int MAX_CRAFTING_SKILL = 40;
	
	/**
	 * The public IP address of the Minecraft server.
	 */
	public static final String IP = Bukkit.getServer().getIp();
	
	/**
	 * The public IP address and port of the Minecraft server.
	 */
	public static final String IP_AND_PORT = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();

	/**
	 * The instance of this class, that is created by the Minecraft server.
	 */
	private static WauzCore instance;
	
	/**
	 * The WebServerManager used for the web based API.
	 */
	private static WebServerManager webServerManager;
	
	/**
	 * Gets called when the server is started.
	 * 1. Initializes the loader to load all the static data.
	 * 2. Registers the event listeners.
	 * 3. Sets up the web based API.
	 * 4. And finally starts all repeating tasks.
	 * 
	 * @see WauzLoader
	 * @see WauzListener
	 * @see WebServerManager
	 */
	@Override
	public void onEnable() {
		instance = this;
		
		/**
		 * Print the version
		 */
		getLogger().info("O~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-O");
		getLogger().info(" _    _                                           ");
		getLogger().info("| |  | | WauzCore v" + getDescription().getVersion());
		getLogger().info("| |  | | __ _ _   _ _____ __ ___   ___  _ __  ___ ");
		getLogger().info("| |/\\| |/ _` | | | |_  / '_ ` _ \\ / _ \\| '_ \\/ __|");
		getLogger().info("\\  /\\  / (_| | |_| |/ /| | | | | | (_) | | | \\__ \\");
		getLogger().info(" \\/  \\/ \\__,_|\\__,_/___|_| |_| |_|\\___/|_| |_|___/");
		getLogger().info("");
		getLogger().info("O-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~O");
		
		WauzLoader.init();
		getLogger().info("Loaded Data from Files!");
		
		getServer().getPluginManager().registerEvents(new WauzListener(), this);
		getServer().getPluginManager().registerEvents(new ArmorEquipEventListener(), this);
		getLogger().info("Registered EventListeners!");
		
		webServerManager = new WebServerManager(7069);
		getLogger().info("Started WebServerManager!");
		
		/**
		 * Every second
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : getRegisteredActivePlayers()) {
					WauzPlayerActionBar.update(player);
				}
			}
		}, 200, 20);
		
		/**
		 * Every 3 seconds
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : getRegisteredActivePlayers()) {
					WauzPlayerScoreboard.scheduleScoreboard(player);
					WauzRegion.regionCheck(player);
				}
			}
		}, 200, 60);
		
		/**
		 * Every 5 seconds
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : getRegisteredActivePlayers()) {
					if(WauzMode.isMMORPG(player)) {
						ClimateCalculator.updateTemperature(player);
						ManaCalculator.regenerateMana(player);
					}
					else if(WauzMode.isSurvival(player)) {
						DamageCalculator.decreasePvPProtection(player);
					}
				}
			}
		}, 200, 100);
		
		/**
		 * Every 5 minutes
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(World world : Bukkit.getWorlds()) {
					if(world.getPlayers().size() == 0) InstanceManager.closeInstance(world);
				}
				for(Player player : getRegisteredActivePlayers()) {
					CharacterManager.saveCharacter(player);
				}
			}
		}, 200, 6000);
		
		/**
		 * Every 15 minutes
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					WauzPlayerNotifier.execute(player);
				}
			}
		}, 200, 18000);
		
		getLogger().info("Scheduled Repeating Tasks!");
	}

	/**
	 * Gets called when the server is stopped.
	 * 1. Closes the web based API.
	 * 2. Logs out all players.
	 * 3. And closes all active instances.
	 * 
	 * @see WauzPlayerRegistrator#logout(Player)
	 * @see InstanceManager#closeInstance(World)
	 */
	@Override
	public void onDisable() {
		webServerManager.stop();
		getLogger().info("Stopped WebServerManager!");
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			WauzPlayerRegistrator.logout(player);
		}
		getLogger().info("Logged Out Players!");
		
		for(World world : Bukkit.getWorlds()) {
			InstanceManager.closeInstance(world);
		}
		getLogger().info("Closed Active Instances!");
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
	 * @return The WebServerManager used for the web based API.
	 */
	public static WebServerManager getWebServerManager() {
		return webServerManager;
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
	public List<Player> getRegisteredActivePlayers() {
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
		return RegionConfigurator.getServerRegionKey();
	}
	
	/**
	 * Prints information about the system to the requestor.
	 * 
	 * @param sender The person who requested the analytics.
	 * @return If the action was successful.
	 */
	public static boolean printSystemAnalytics(CommandSender sender) {
		try {
			OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
			String sys = os.getName() + " " + os.getArch();
			String cpu = os.getAvailableProcessors() + " Processors, " + os.getSystemLoadAverage() + " Load Avg";
			String ram = getByteUnit(Runtime.getRuntime().freeMemory()) + " / " + getByteUnit(Runtime.getRuntime().maxMemory());
			long spaceTotal = instance.getDataFolder().getTotalSpace();
			long spaceInUse = spaceTotal - instance.getDataFolder().getFreeSpace();
			String ssd = getByteUnit(spaceInUse) + " / " + getByteUnit(spaceTotal);
			sender.sendMessage(ChatColor.DARK_RED + "[System: " + sys + " " + cpu + "]");
			sender.sendMessage(ChatColor.DARK_RED + "[RAM: " + ram + "] [SSD: " + ssd + "]");
			return true;
		}
		catch (Exception e) {
			WauzDebugger.catchException(instance.getClass(), e);
			return false;
		}
	}
	
	/**
	 * Formats byte units for displaying in the system analytics.
	 * 
	 * @param bytes The raw amount of bytes.
	 * @return A formatted string with byte unit.
	 * 
	 * @see WauzCore#printSystemAnalytics(CommandSender)
	 */
	private static String getByteUnit(long bytes) {
		if (bytes < 1024)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(1024));
		return String.format("%.1f %sB", bytes / Math.pow(1024, exp), ("KMGTPE").charAt(exp - 1));
	}

}
