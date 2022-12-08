package eu.wauz.wauzcore.system.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import eu.wauz.wauzcore.mobs.MobEventMapper;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobDespawnEvent;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;

/**
 * A listener to catch events, related to the mythic mobs api.
 * 
 * @author Wauzmons
 */
public class MythicMobsListener implements Listener {
	
	/**
	 * Lets the mapper decide how to handle the spawn of a (mythic) mob.
	 * 
	 * @param event The spawn event.
	 * 
	 * @see MobEventMapper#spawn(MythicMobSpawnEvent)
	 */
	 @EventHandler
	 public void onMythicSpawn(MythicMobSpawnEvent event) {
		 MobEventMapper.spawn(event);
	 }
	 
	 /**
	 * Lets the mapper decide how to handle the despawn of a (mythic) mob.
	 * 
	 * @param event The despawn event.
	 * 
	 * @see MobEventMapper#despawn(MythicMobDespawnEvent)
	 */
	 @EventHandler
	 public void onMythicDepawn(MythicMobDespawnEvent event) {
		 MobEventMapper.despawn(event);
	 }

	/**
	 * Lets the mapper decide how to handle the death of a (mythic) mob.
	 * 
	 * @param event The death event.
	 * 
	 * @see MobEventMapper#death(MythicMobDeathEvent)
	 */
	@EventHandler
	public void onMythicDeath(MythicMobDeathEvent event) {
		MobEventMapper.death(event);
	}

}
