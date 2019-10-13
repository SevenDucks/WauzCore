package eu.wauz.wauzcore.players;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.players.ui.WauzPlayerBossBar;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerRegistrator {

	private static WauzCore core = WauzCore.getInstance();

	public static void login(final Player player) throws Exception {
		File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		playerDirectory.mkdir();
		File playerDataFile = new File(playerDirectory, "global.yml");
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		
		playerDataConfig.set("name", player.getName());
		playerDataConfig.set("lastplayed", System.currentTimeMillis());
		
		if(!playerDataFile.exists()) {
			playerDataConfig.set("rank", "Normal");
			if(player.hasPermission("wauz.system"))
				playerDataConfig.set("rank", "Admin");
			
			playerDataConfig.set("guild", "none");
			
			playerDataConfig.set("tokens", 0);
			playerDataConfig.set("tokenlimit.survival.date", WauzDateUtils.getDateLong());
			playerDataConfig.set("tokenlimit.survival.amount", 0);
			playerDataConfig.set("tokenlimit.mmorpg.date", WauzDateUtils.getDateLong());
			playerDataConfig.set("tokenlimit.mmprgp.amount", 0);
			playerDataConfig.set("score.survival", 0);
		}
		playerDataConfig.save(playerDataFile);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
        		player.setHealth(20);
        		player.setFoodLevel(20);
        		player.setSaturation(20);
        		player.getInventory().clear();
        		player.setExp(0);
        		player.setLevel(0);
        		
        		player.setBedSpawnLocation(WauzCore.getHubLocation(), true);
        		player.teleport(WauzCore.getHubLocation());
        		
        		player.sendMessage("Welcome to Wauzland! v" + core.getDescription().getVersion());
        		WauzPlayerDataPool.regPlayer(player);
        		WauzPlayerScoreboard.scheduleScoreboard(player);
        		CharacterManager.equipHubItems(player);
            }
		}, 10);
	}
	
	public static void logout(Player player) {
		WauzPlayerBossBar.clearBar(player);
		PlayerConfigurator.setLastPlayed(player);
		CharacterManager.logoutCharacter(player);
		WauzPlayerDataPool.unregPlayer(player);
	}
	
	public static void respawn(final Player player) {
		boolean allowRespawn = false;
		World world = player.getWorld();
		int maxDeaths = InstanceConfigurator.getInstanceWorldMaximumDeaths(world);
		if(maxDeaths > 0) {
			int deathCount = InstanceConfigurator.getInstanceWorldPlayerDeathCount(world, player) + 1;
			InstanceConfigurator.setInstanceWorldPlayerDeathCount(world, player, deathCount);
			if(deathCount <= maxDeaths) {
				player.sendMessage(ChatColor.RED + "Youd respawned " + deathCount + " / " + maxDeaths + " times in this instance!");
				allowRespawn = true;
			}
		}
		
		final boolean respawnInCurrentWorld = allowRespawn;
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(core, new Runnable() { public void run() {
			if(respawnInCurrentWorld) {
				Location spawnLocation = player.getBedSpawnLocation();
				player.setBedSpawnLocation(new Location(world, 0.5, 5, 0.5), true);
				WauzNmsClient.nmsRepsawn(player);
				player.setBedSpawnLocation(spawnLocation, true);
			}
			else {
				WauzNmsClient.nmsRepsawn(player);
			}
        	player.sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "YOU DIED", "", 10, 70, 20);
        }});
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		playerData.setResistanceHeat((short) 0);
		playerData.setResistanceCold((short) 0);
		playerData.setResistancePvsP((short) 0);
		WauzPlayerActionBar.update(player);
	}

}
