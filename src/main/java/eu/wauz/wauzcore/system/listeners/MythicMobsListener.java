package eu.wauz.wauzcore.system.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import eu.wauz.wauzcore.data.players.PlayerBestiaryConfigurator;
import eu.wauz.wauzcore.mobs.MobEventMapper;
import eu.wauz.wauzcore.mobs.MobMetadataUtils;
import eu.wauz.wauzcore.mobs.bestiary.ObservationRank;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDespawnEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

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
	
	/**
	 * Doubles the loot dropped by a mob, whem the killer has an S-tier bestiary entry of it.
	 * 
	 * @param event The loot drop event.
	 */
	public void onMythicDrop(MythicMobLootDropEvent event) {
		if(!(event.getKiller() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getKiller();
		ActiveMob mob = event.getMob();
		Entity entity = mob.getEntity().getBukkitEntity();
		
		if(MobMetadataUtils.hasBestiaryEntry(entity)) {
			String entry = MobMetadataUtils.getBestiaryEntry(entity);
			boolean isBoss = MobMetadataUtils.isRaidBoss(entity);
			int killCount = PlayerBestiaryConfigurator.getBestiaryKills(player, entry);
			int neededKills = isBoss ? ObservationRank.S.getBossKills() : ObservationRank.S.getNormalKills();
			if(killCount >= neededKills) {
				DropMetadata dropMetadata = new DropMetadata(mob, BukkitAdapter.adapt(event.getKiller()));
				event.getDrops().add(dropMetadata, event.getDrops());
			}
		}
	}

}
