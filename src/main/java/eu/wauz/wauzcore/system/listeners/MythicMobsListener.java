package eu.wauz.wauzcore.system.listeners;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import eu.wauz.wauzcore.mobs.MenacingMobsSpawner;
import eu.wauz.wauzcore.mobs.MobEventMapper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;

/**
 * A listener to catch events, related to the mythic mobs api.
 * 
 * @author Wauzmons
 */
public class MythicMobsListener implements Listener {
	
	/**
	 * Handles the spawn of a (mythic) mob.
	 * This includes the initialization of modifiers, loot and boss bars.
	 * 
	 * @param event
	 * 
	 * @see MenacingMobsSpawner
	 */
	 @EventHandler
	 public void onMythicSpawn(MythicMobSpawnEvent event) {
		 MenacingMobsSpawner.addMenacingMob(event.getEntity(), event.getMobType());
	 }

	 /**
	  * Lets the mapper decide how to handle the death of a (mythic) mob.
	  * This includes exp rewards, pet deaths and modifier effects.
	  * 
	  * @param event
	  * 
	  * @see MobEventMapper#death(MythicMobDeathEvent)
	  */
	@EventHandler
	public void onMythicDeath(MythicMobDeathEvent event) {
		if(StringUtils.isNotBlank(event.getEntity().getCustomName())) {
			MobEventMapper.death(event);
		}
	}

}
