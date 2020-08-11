package eu.wauz.wauzcore.mobs;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.calc.ExperienceCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.instances.WauzInstanceKeyStatus;

/**
 * This is the place, where exp and key drops are generated
 * from a mob's metadata, that they received from the spawner.
 * 
 * @author Wauzmons
 * 
 * @see MenacingMobsSpawner
 */
public class MenacingMobsLoot {
	
	/**
	 * Drops exp for the killer, according to the entity's metadata.
	 * 
	 * @param entity The entity that died.
	 * @param killer The killer of the entity.
	 */
	public static void dropExp(Entity entity, Entity killer) {
		if(killer == null || !(killer instanceof Player)) {
			return;
		}
		
		int tier = MobMetadataUtils.getExpDropTier(entity);
		double amount = MobMetadataUtils.getExpDropAmount(entity);
		if(tier > 0 && amount > 0) {
			ExperienceCalculator.grantExperience((Player) killer, tier, amount, entity.getLocation());
		}
	}
	
	/**
	 * Drops a key for its current world, according to the entity's metadata.
	 * 
	 * @param entity The entity that died.
	 */
	public static void dropKey(Entity entity) {
		String keyId = MobMetadataUtils.getKeyDrop(entity);
		if(StringUtils.isNotBlank(keyId)) {
			WauzActiveInstance instance = WauzActiveInstancePool.getInstance(entity.getWorld());
			instance.setKeyStatus(keyId, WauzInstanceKeyStatus.OBTAINED);
			
			for(Player player : instance.getWorld().getPlayers()) {
				WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
				player.sendMessage(ChatColor.GREEN + "You obtained the Key \"" + ChatColor.DARK_AQUA + keyId + ChatColor.GREEN + "\"!");
			}
		}
	}

}
