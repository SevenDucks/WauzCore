package eu.wauz.wauzcore.professions;

import java.util.UUID;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.ui.bossbar.WauzPlayerBossBar;
import eu.wauz.wauzcore.players.ui.bossbar.WauzPlayerResourceBossBar;

/**
 * A player specific cache of a gatherable resource spawn.
 * 
 * @author Wauzmons
 *
 * @see WauzResourceSpawn
 */
public class WauzResourceCache {
	
	/**
	 * The uuid of the resource cache.
	 */
	private String uuid = UUID.randomUUID().toString();
	
	/**
	 * The cooldown timestamp, indicating when the resource can be collected again.
	 */
	private long cooldown = 0;
	
	/**
	 * The current health of the resource.
	 */
	private int health = 0;
	
	/**
	 * The resource spawn, the cache is referring to.
	 */
	private WauzResourceSpawn resourceSpawn;
	
	/**
	 * Creates a new player specific cache for the given resource spawn.
	 * 
	 * @param resourceSpawn The resource spawn, the cache is referring to.
	 */
	public WauzResourceCache(WauzResourceSpawn resourceSpawn) {
		this.resourceSpawn = resourceSpawn;
	}
	
	/**
	 * Reduces the cached health of the resource spawn.
	 * 
	 * @param player The player that damaged the resource.
	 * @param damage The damage done to the resource.
	 * 
	 * @return If the damage was enough to destroy the resource.
	 */
	public boolean reduceHealth(Player player, double damage) {
		WauzPlayerBossBar playerBossBar = WauzPlayerResourceBossBar.getBossBar(this);
		if(playerBossBar == null) {
			health = resourceSpawn.getResource().getNodeHealth();
			playerBossBar = new WauzPlayerResourceBossBar(this);
		}
		health -= damage;
		playerBossBar.addPlayer(player, 0);
		playerBossBar.updateBossBar();
		return health <= 0;
	}

	/**
	 * @return The uuid of the resource cache.
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @return The cooldown timestamp, indicating when the resource can be collected again.
	 */
	public long getCooldown() {
		return cooldown;
	}

	/**
	 * @param cooldown The new cooldown timestamp, indicating when the resource can be collected again.
	 */
	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * @return The current health of the resource.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health The new current health of the resource.
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return The resource spawn, the cache is referring to.
	 */
	public WauzResourceSpawn getResourceSpawn() {
		return resourceSpawn;
	}

	/**
	 * @param resourceSpawn The resource spawn, the cache is referring to.
	 */
	public void setResourceSpawn(WauzResourceSpawn resourceSpawn) {
		this.resourceSpawn = resourceSpawn;
	}

}
