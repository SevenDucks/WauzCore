package eu.wauz.wauzcore.players.ui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * An UI class to show the player name and health of the targeted object.
 * 
 * @author Wauzmons
 */
public abstract class WauzPlayerBossBar {
	
	/**
	 * A map of all boss bars, indexed by entity uuid.
	 */
	protected static Map<String, WauzPlayerBossBar> bossBars = new HashMap<>();
	
	/**
	 * A map of boss bars, indexed by the player they are shown to.
	 */
	protected static Map<Player, WauzPlayerBossBar> bossBarPlayerLinks = new HashMap<>();
	
	/**
	 * Removes the shown boss bar for the given player.
	 * 
	 * @param player The player who should no longer see a boss bar.
	 */
	public static void clearBar(Player player) {
		WauzPlayerBossBar playerBossBar = bossBarPlayerLinks.get(player);
		if(playerBossBar != null) {
			playerBossBar.removePlayer(player);
			bossBarPlayerLinks.put(player, null);
		}
	}
	
	/**
	 * The id of the object, this bar belongs to.
	 */
	protected String uuid;
	
	/**
	 * Modifiers shown in the bar.
	 */
	protected String modifiers;
	
	/**
	 * Max health shown in the bar.
	 */
	protected double maxHealth;
	
	/**
	 * The Minecraft boss bar.
	 */
	protected BossBar bossBar;
	
	/**
	 * Adds a player that should see this bar.
	 * If the entity will die from the damage, the bar will not be shown.
	 * 
	 * @param player The player that should see this bar.
	 * @param damage The damage the player dealt to see the bar.
	 */
	public void addPlayer(Player player, double damage) {
		if(bossBar.getProgress() <= 0) {
			return;
		}
		
		if((int) Math.ceil(getHealth()) - damage <= 0) {
			return;
		}
		
		WauzPlayerBossBar playerBossBar = bossBarPlayerLinks.get(player);
		if(playerBossBar == null || !playerBossBar.equals(this)) {
			if(playerBossBar != null) {
				playerBossBar.removePlayer(player);
			}
			bossBar.addPlayer(player);
			bossBarPlayerLinks.put(player, this);
		}
	}
	
	/**
	 * Removes a player that should no longer see this bar.
	 * 
	 * @param player The player that should no longer see this bar.
	 */
	public void removePlayer(Player player) {
		bossBar.removePlayer(player);
	}
	
	/**
	 * Updates the boss bar, because the entity got damaged.
	 * If the entity will die from the damage, the bar be destroyed.
	 * 
	 * @param damage The damage that was dealt to the entity.
	 */
	public void updateBossBar(double damage) {
		int health = (int) Math.ceil((getHealth() - damage));
		if(health > maxHealth) {
			health = (int) maxHealth;
		}
		if(health > 0) {
			for(Player player : bossBar.getPlayers()) {
				WauzDebugger.log(player, "BossBar: "
						+ health + " (" + getHealth() + " - "
						+ damage + ") / " + maxHealth);
			}
			bossBar.setTitle(getTitle(health));
			bossBar.setProgress(health / maxHealth);
		}
		else {
			destroy();
		}
	}
	
	/**
	 * Generates a new title for the boss bar.
	 * 
	 * @param health How much health is left.
	 * 
	 * @return The new title.
	 */
	public String getTitle(int health) {
		String currentHealth = ChatColor.RED + Formatters.INT.format(health);
		String maximumHealth = Formatters.INT.format(maxHealth) + " " + UnicodeUtils.ICON_HEART;
		String healthString = ChatColor.GRAY + "[ " + currentHealth + " / " + maximumHealth + ChatColor.GRAY + " ]";
		return modifiers + getName() + " " + healthString;
	}
	
	/**
	 * Schedules a task to check if the object and assigned players are still valid.
	 * If not, they get removed, else the task is scheduled again for the next second.
	 * 
	 * @see WauzPlayerBossBar#destroy()
	 */
	protected abstract void doPlayerChecks();
	
	/**
	 * @return The name of the object, this bar belongs to.
	 */
	protected abstract String getName();
	
	/**
	 * @return The health of the object, this bar belongs to.
	 */
	protected abstract double getHealth();
	
	/**
	 * Checks if the given player is close enough to see the boss bar.
	 * 
	 * @param player The player to check for.
	 * @param location The current location of the object, this bar belongs to.
	 * 
	 * @return If the player is in distance.
	 */
	protected boolean inDistance(Player player, Location location) {
		if(location == null || player == null || !player.isValid()) {
			return false;
		}
		World playerWorld = player.getWorld();
		World locationWorld = location.getWorld();
		if(playerWorld == null || locationWorld == null || !playerWorld.equals(locationWorld)) {
			return false;
		}
		return player.getLocation().distance(location) <= 32;
	}
	
	/**
	 * Removes this boss bar from all maps and clears out all players.
	 */
	protected void destroy() {
		for(Player player : bossBar.getPlayers()) {
			bossBar.removePlayer(player);
			bossBarPlayerLinks.remove(player);
		}
		bossBars.remove(uuid);
	}
	
}
