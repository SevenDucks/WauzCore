package eu.wauz.wauzcore.players;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.oneblock.OnePlotManager;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.players.ui.bossbar.WauzPlayerBossBar;
import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import eu.wauz.wauzcore.worlds.instances.WauzActiveInstance;
import eu.wauz.wauzcore.worlds.instances.WauzActiveInstancePool;

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
	 * @throws Exception Error on login.
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
			playerDataConfig.set("rank", "Normal");
			playerDataConfig.set("guild", "none");
			playerDataConfig.set("tokens", 0);
			long dateLong = WauzDateUtils.getDateLong();
			playerDataConfig.set("tokenlimit.mails.date", dateLong);
			playerDataConfig.set("tokenlimit.mails.amount", 0);
			playerDataConfig.set("score.survival", 0);
			playerDataConfig.set("friends", new ArrayList<>());
		}
		playerDataConfig.save(playerDataFile);
		
		WauzRank rank = WauzRank.getRank(player);
		PermissionAttachment attachment = player.addAttachment(core);
		player.setOp(rank.isGrantOp());
		attachment.setPermission(rank.getRankPermission().toString(), true);
		attachment.setPermission(WauzPermission.FAWE_WORLDEDIT_1.toString(), false);
		attachment.setPermission(WauzPermission.FAWE_WORLDEDIT_2.toString(), false);
		attachment.setPermission(WauzPermission.FAWE_WORLDEDIT_3.toString(), false);
		attachment.setPermission(WauzPermission.FAWE_WORLDEDIT_4.toString(), false);
		attachment.setPermission(WauzPermission.FAWE_VOXELSNIPER.toString(), false);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            
			@Override
			public void run() {
        		player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        		player.setFoodLevel(20);
        		player.setSaturation(20);
        		player.getInventory().clear();
        		player.setExp(0);
        		player.setLevel(0);
        		player.setGameMode(GameMode.ADVENTURE);
        		
        		player.setBedSpawnLocation(WauzCore.getHubLocation(), true);
        		player.teleport(WauzCore.getHubLocation());
        		
        		player.sendMessage("Welcome! Running WauzCore v" + core.getDescription().getVersion());
        		WauzPlayerDataPool.regPlayer(player);
        		WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
        		CharacterManager.equipHubItems(player);
            }
            
		});
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
		final WauzActiveInstance instance = WauzActiveInstancePool.getInstance(world);
		if(instance != null && instance.getMaxDeaths() > 0) {
			instance.addPlayerDeath(player);
			int maxDeaths = instance.getMaxDeaths();
			int playerDeaths = instance.getPlayerDeaths(player);
			if(playerDeaths <= maxDeaths) {
				player.sendMessage(ChatColor.RED + "You've respawned " + playerDeaths + " / " + maxDeaths + " times in this instance!");
				allowRespawn = true;
			}
		}
		
		final boolean respawnInCurrentWorld = allowRespawn;
		final boolean characterSelected = WauzPlayerDataPool.isCharacterSelected(player);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
			
			@Override
			public void run() {
				if(respawnInCurrentWorld) {
					Location spawnLocation = player.getBedSpawnLocation();
					player.setBedSpawnLocation(instance.getSpawnLocation(), true);
					player.spigot().respawn();
					player.setBedSpawnLocation(spawnLocation, true);
				}
				else if(characterSelected && WauzMode.isMMORPG(player) && !StringUtils.isBlank(PlayerConfigurator.getCharacterHearthstoneRegion(player))) {
					Location spawnLocation = player.getBedSpawnLocation();
					player.setBedSpawnLocation(PlayerConfigurator.getCharacterHearthstone(player), true);
					player.spigot().respawn();
					player.setBedSpawnLocation(spawnLocation, true);
				}
				else {
					if(characterSelected) {
						player.setBedSpawnLocation(PlayerConfigurator.getCharacterSpawn(player), true);
					}
					player.spigot().respawn();
				}
				
				if(WauzMode.inOneBlock(player)) {
					OnePlotManager.setUpBorder(player);
				}
			}
			
		});
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		playerData.getStats().getEffects().clearEffects();
		WauzPlayerActionBar.update(player);
	}
	
}
