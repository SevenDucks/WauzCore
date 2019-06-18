package eu.wauz.wauzcore.items;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillMechanics;
import net.md_5.bungee.api.ChatColor;

public class DungeonItemGrapplingHook {

// Pull Player to Location
	
	public static void use(final ProjectileLaunchEvent event) {
		final Player player = (Player) event.getEntity().getShooter();
		Location target = null;

		for(Block block : player.getLineOfSight(null, 15)) {
			if(pull(block, event))
				return;

			if(block.getType().equals(Material.SPONGE)) {
				target = block.getLocation();
				break;
			}
		}

		if(target == null) {
			event.setCancelled(true);
			return;
		}

		player.teleport(player.getLocation().add(0, 0.5, 0));
		final Vector vector = WauzPlayerSkillMechanics.getVectorForPoints(player.getLocation(), target);
		event.getEntity().setVelocity(vector);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
            	player.setVelocity(vector);
            	event.getEntity().remove();
            }
		}, 10);
	}
	
// Pull Entity to Player
	
	public static boolean pull(Block block, final ProjectileLaunchEvent event) {
		final Player player = (Player) event.getEntity().getShooter();
		Collection<Entity> nearbyEntites = block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1);
		
		if(nearbyEntites.size() > 0) {
			for(final Entity entity : nearbyEntites) {
				if(!entity.getType().equals(EntityType.ARMOR_STAND) &&
						entity.getCustomName() != null &&
						entity.getCustomName().contains("" + ChatColor.AQUA)) {
					
					entity.teleport(entity.getLocation().add(0, 0.5, 0));
					event.getEntity().setVelocity(WauzPlayerSkillMechanics.getVectorForPoints(player.getLocation(), entity.getLocation()));
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			            public void run() {
			            	entity.setVelocity(WauzPlayerSkillMechanics.getVectorForPoints(entity.getLocation(), player.getLocation()));
			            	event.getEntity().remove();
			            }
					}, 10);	
					return true;
				}
			}
		}
		return false;
	}

}
