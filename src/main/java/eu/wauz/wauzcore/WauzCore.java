package eu.wauz.wauzcore;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.wauz.wauzcore.data.RegionConfigurator;
import eu.wauz.wauzcore.events.ArmorEquipEventListener;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerRegistrator;
import eu.wauz.wauzcore.players.calc.ClimateCalculator;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.players.ui.WauzPlayerNotifier;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.InstanceManager;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.api.WebServerManager;
import eu.wauz.wauzcore.system.commands.WauzCommandExecutor;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * The Main Class of the Plugin and Holder of System Information.
 * Initializes all static Data, Listerners, API and repeating Tasks.
 * When the server stops it will clean it up again.
 * 
 * @author Wauzmons
 */
public class WauzCore extends JavaPlugin {
	
	/**
	 * The Maximum Level a Player can reach in MMORPG Mode.
	 */
	public static final int MAX_PLAYER_LEVEL = 30;
	
	/**
	 * The Maximum Level a Player can reach in Survival Mode.
	 */
	public static final int MAX_PLAYER_LEVEL_SURVIVAL = 30;
	
	/**
	 * The Maximum Crafting Skill a Player can reach in MMORPG Mode.
	 */
	public static final int MAX_CRAFTING_SKILL = 40;
	
	/**
	 * The public IP Address of the Minecraft Server.
	 */
	public static final String IP = Bukkit.getServer().getIp();

	/**
	 * The instance of this Class, that is created by the Minecraft Server.
	 */
	private static WauzCore instance;
	
	/**
	 * The Web Server Manager used for the Web Based API.
	 */
	private static WebServerManager webServerManager;
	
	/**
	 * Gets called when the Server is started.
	 * Initializes the Loader to load all the static Data.
	 * Registers the Event Listeners.
	 * Sets up the Web Based API.
	 * And finally starts all repeating Tasks.
	 * 
	 * @see WauzLoader
	 * @see WauzListener
	 * @see WebServerManager
	 */
	@Override
	public void onEnable() {
		instance = this;
		
		/**
		 * Print the Version
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
		 * Every Second
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					WauzPlayerActionBar.update(player);
				}
			}
		}, 200, 20);
		
		/**
		 * Every 3 Seconds
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					WauzPlayerScoreboard.scheduleScoreboard(player);
					WauzRegion.regionCheck(player);
				}
			}
		}, 200, 60);
		
		/**
		 * Every 5 Seconds
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					if(WauzMode.isMMORPG(player)) {
						ClimateCalculator.temperature(player);
						ManaCalculator.regenerateMana(player);
					}
					else if(WauzMode.isSurvival(player)) {
						DamageCalculator.decreasePvPProtection(player);
					}
				}
			}
		}, 200, 100);
		
		/**
		 * Every 5 Minutes
		 */
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(World world : Bukkit.getWorlds()) {
					if(world.getPlayers().size() == 0) InstanceManager.closeInstance(world);
				}
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					CharacterManager.saveCharacter(player);
				}
			}
		}, 200, 6000);
		
		/**
		 * Every 15 Minutes
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
	 * Gets called when the Server is stopped.
	 * Closes the Web Based API.
	 * Logs out all Players.
	 * And closes all active Instances.
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
	 * Listens for incoming Commands from the plugin.xml.
	 * Redirects the needed Informtation the Command Executor.
	 * 
	 * @see WauzCommandExecutor
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return WauzCommandExecutor.execute(sender, cmd, args);
	}

	/**
	 * @return The instance of this Class, that is created by the Minecraft Server.
	 */
	public static WauzCore getInstance() {
		return instance;
	}

	/**
	 * @return The Web Server Manager used for the Web Based API.
	 */
	public static WebServerManager getWebServerManager() {
		return webServerManager;
	}
	
	/**
	 * @return The Location of the Hub, where Players start their Adventures.
	 */
	public static Location getHubLocation() {
		return new Location(Bukkit.getWorld("HubNexus"), 0.5, 95, 0.5);
	}
	
	/**
	 * Finds an online Player by their Name.
	 * 
	 * @param name The Name of the Player.
	 * @return The Player if they are online, else null.
	 */
	public static Player getOnlinePlayer(String name) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equals(name))
				return player;
		}
		return null;
	}
	
	/**
	 * Finds an offline Player by their Name.
	 * 
	 * @param name The Name of the Player.
	 * @return The Player if they exist, else null.
	 */
	public static OfflinePlayer getOfflinePlayer(String name) {
		for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
			if(player.getName().equals(name))
				return player;
		}
		return null;
	}
	
	/**
	 * @return The Key of the Server defined in the .yml Configuration.
	 */
	public static String getServerKey() {
		return RegionConfigurator.getServerRegionKey();
	}
	
	/**
	 * Prints Information about the System to the requestor.
	 * 
	 * @param sender The Person who requested the Analytics.
	 * @return If the Action was successful.
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
	 * Formats Byte Units for displaying in the System Analytics.
	 * 
	 * @param bytes The raw amount of Bytes.
	 * @return A formatted String with Byte Unit.
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
