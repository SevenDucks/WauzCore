package eu.wauz.wauzcore.players.ui.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.professions.WauzResourceCache;

/**
 * An UI class to show name and health of the targeted resource to the player.
 * 
 * @author Wauzmons
 */
public class WauzPlayerResourceBossBar extends WauzPlayerBossBar {
	
	/**
	 * The resource, this bar belongs to.
	 */
	private WauzResourceCache resource;
	
	/**
	 * Creates a boss bar for the given resource.
	 * Also schedules a task to check if the resource and assigned players are still valid. 
	 * 
	 * @param resource The resource, this bar belongs to.
	 * 
	 * @see WauzPlayerBossBar#doPlayerChecks()
	 */
	public WauzPlayerResourceBossBar(WauzResourceCache resource) {
		this.resource = resource;
		this.uuid = resource.getUuid();
		
		int health = (int) Math.ceil(getHealth());
		int maxHealth = (int) Math.ceil(getMaxHealth());
		bossBar = Bukkit.createBossBar(getTitle(health, maxHealth), BarColor.BLUE, BarStyle.SEGMENTED_6);
		bossBars.put(uuid, this);
		doPlayerChecks();
	}
	
	/**
	 * @param resource A resource with a boss bar.
	 * 
	 * @return The boss bar of this resource, if existing.
	 */
	public static WauzPlayerBossBar getBossBar(WauzResourceCache resource) {
		return bossBars.get(resource.getUuid());
	}
	
	/**
	 * Schedules a task to check if the object and assigned players are still valid.
	 * If not, they get removed, else the task is scheduled again for the next second.
	 * 
	 * @see WauzPlayerBossBar#destroy()
	 */
	@Override
	protected void doPlayerChecks() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        @Override
			public void run() {
	        	try {
        			for(Player player : bossBar.getPlayers()) {
        				if(!inDistance(player, resource.getResourceSpawn().getLocation())) {
        					destroy();
        					return;
        				}
        			}
        			doPlayerChecks();
	        	}
	        	catch (Exception e) {
	        		e.printStackTrace();
	        		destroy();
	        	}
	        }
	        
		}, 20);
	}
	
	/**
	 * @return The name of the object, this bar belongs to.
	 */
	@Override
	protected String getName() {
		return resource.getResourceSpawn().getResource().getNodeName();
	}
	
	/**
	 * @return The health of the object, this bar belongs to.
	 */
	@Override
	protected double getHealth() {
		return resource.getHealth();
	}
	
	/**
	 * @return The maximum health of the object, this bar belongs to.
	 */
	@Override
	protected double getMaxHealth() {
		return resource.getResourceSpawn().getResource().getNodeHealth();
	}

}
