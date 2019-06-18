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
import eu.wauz.wauzcore.system.WauzCommandExecutor;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.api.ShiroDiscordBot;
import eu.wauz.wauzcore.system.api.WebServerManager;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzCore extends JavaPlugin {
	
	public static final int MAX_PLAYER_LEVEL = 30;
	
	public static final int MAX_PLAYER_LEVEL_SURVIVAL = 30;
	
	public static final int MAX_CRAFTING_SKILL = 40;
	
	public static final String IP = Bukkit.getServer().getIp();

	private static WauzCore instance;
	
	private static WebServerManager webServerManager;
	
	private static ShiroDiscordBot shiroDiscordBot;
	
	@Override
	public void onEnable() {
		instance = this;
		
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
		
		shiroDiscordBot = new ShiroDiscordBot();
		getLogger().info("Shiro's body is ready!");
		
		// Every Second
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					WauzPlayerActionBar.update(player);
				}
			}
		}, 200, 20);
		
		// Every 3 Seconds
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					WauzPlayerScoreboard.scheduleScoreboard(player);
					WauzRegion.regionCheck(player);
				}
			}
		}, 200, 60);
		
		// Every 5 Seconds
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
		
		// Every 5 Minutes
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
		
		// Every 15 Minutes
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

	@Override
	public void onDisable() {
		webServerManager.stop();
		getLogger().info("Stopped WebServerManager!");
		
		shiroDiscordBot.stop();
		getLogger().info("Shiro's taking a nap!");
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			WauzPlayerRegistrator.logout(player);
		}
		getLogger().info("Logged Out Players!");
		
		for(World world : Bukkit.getWorlds()) {
			InstanceManager.closeInstance(world);
		}
		getLogger().info("Closed Active Instances!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return WauzCommandExecutor.execute(sender, cmd, args);
	}

	public static WauzCore getInstance() {
		return instance;
	}

	public static WebServerManager getWebServerManager() {
		return webServerManager;
	}
	
	public static ShiroDiscordBot getShiroDiscordBot() {
		return shiroDiscordBot;
	}
	
	public static Location getHubLocation() {
		return new Location(Bukkit.getWorld("HubNexus"), 0.5, 95, 0.5);
	}
	
	public static Player getOnlinePlayer(String name) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equals(name))
				return player;
		}
		return null;
	}
	
	public static OfflinePlayer getOfflinePlayer(String name) {
		for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
			if(player.getName().equals(name))
				return player;
		}
		return null;
	}
	
	public static String getServerKey() {
		return RegionConfigurator.getServerRegionKey();
	}
	
	public static boolean printSystemAnalytics(CommandSender sender) {
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
	
	private static String getByteUnit(long bytes) {
		if (bytes < 1024)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(1024));
		return String.format("%.1f %sB", bytes / Math.pow(1024, exp), ("KMGTPE").charAt(exp - 1));
	}

}
