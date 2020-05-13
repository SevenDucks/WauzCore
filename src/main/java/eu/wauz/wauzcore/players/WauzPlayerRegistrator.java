package eu.wauz.wauzcore.players;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * The player registrator is used to initially create/remove/update player information.
 * 
 * @author Wauzmons
 */
public class WauzPlayerRegistrator {

	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();

	/**
	 * Handles the join of a player.
	 * Updates "name" and "lastplayed" config values + initializes other values for first time players.
	 * Resets values like health and saturation and teleports the player to the hub.
	 * 
	 * @param player The player that joined.
	 * 
	 * @throws Exception
	 * 
	 * @see WauzPlayerDataPool#regPlayer(Player)
	 * @see CharacterManager#equipHubItems(Player)
	 */
	public static void login(final Player player) throws Exception {
		File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		playerDirectory.mkdir();
		File playerDataFile = new File(playerDirectory, "global.yml");
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		
		playerDataConfig.set("name", player.getName());
		playerDataConfig.set("lastplayed", System.currentTimeMillis());
		
		if(!playerDataFile.exists()) {
			if(player.hasPermission(WauzPermission.SYSTEM.toString())) {
				playerDataConfig.set("rank", "Admin");
			}
			else {
				playerDataConfig.set("rank", "Normal");
			}
			
			playerDataConfig.set("guild", "none");
			
			playerDataConfig.set("tokens", 0);
			long dateLong = WauzDateUtils.getDateLong();
			playerDataConfig.set("tokenlimit.survival.date", dateLong);
			playerDataConfig.set("tokenlimit.survival.amount", 0);
			playerDataConfig.set("tokenlimit.mmorpg.date", dateLong);
			playerDataConfig.set("tokenlimit.mmprgp.amount", 0);
			playerDataConfig.set("tokenlimit.mails.date", dateLong);
			playerDataConfig.set("tokenlimit.mails.amount", 0);
			playerDataConfig.set("score.survival", 0);
			playerDataConfig.set("friends", new ArrayList<>());
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
        		
        		player.sendMessage("Welcome! Running WauzCore v" + core.getDescription().getVersion());
        		WauzPlayerDataPool.regPlayer(player);
        		WauzPlayerScoreboard.scheduleScoreboard(player);
        		CharacterManager.equipHubItems(player);
            }
		}, 10);
	}
	
	/**
	 * Handles the leave of a player.
	 * Deletes all cached values related to that player.
	 * 
	 * @param player The player that left.
	 * 
	 * @see WauzPlayerBossBar#clearBar(Player)
	 * @see PlayerConfigurator#setLastPlayed(org.bukkit.OfflinePlayer)
	 * @see CharacterManager#logoutCharacter(Player)
	 * @see WauzPlayerDataPool#unregPlayer(Player)
	 */
	public static void logout(Player player) {
		WauzPlayerBossBar.clearBar(player);
		PlayerConfigurator.setLastPlayed(player);
		CharacterManager.logoutCharacter(player);
		WauzPlayerDataPool.unregPlayer(player);
	}
	
	/**
	 * Handles the respawn of a player.
	 * Skips the death screen and shows a "YOU DIED" title instead, while resetting food status effects.
	 * The player may respawn in an instance, if max deaths are defined and they are below that value.
	 * 
	 * @param player The player that is respawning.
	 */
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
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
			public void run() {
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
			}
		});
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		playerData.setResistanceHeat((short) 0);
		playerData.setResistanceCold((short) 0);
		playerData.setResistancePvP((short) 0);
		WauzPlayerActionBar.update(player);
	}

}
