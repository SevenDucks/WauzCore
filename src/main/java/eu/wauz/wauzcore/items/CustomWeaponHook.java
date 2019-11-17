package eu.wauz.wauzcore.items;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

public class CustomWeaponHook {

	public static void use(final ProjectileLaunchEvent event) {
		final Player player = (Player) event.getEntity().getShooter();
		Location target = null;

		for(Block block : player.getLineOfSight(null, 15)) {
			if(!player.isSneaking() && pull(block, event)) {
				return;
			}

			if(player.isSneaking() && !block.getType().equals(Material.AIR)) {
				target = block.getLocation();
				break;
			}
		}

		if(target == null) {
			event.setCancelled(true);
			return;
		}

		player.teleport(player.getLocation().add(0, 0.5, 0));
		final Vector vector = SkillUtils.getVectorForPoints(player.getLocation(), target);
		event.getEntity().setVelocity(vector);
		DurabilityCalculator.takeDamage(player, player.getEquipment().getItemInMainHand(), 12, false);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
            	player.setVelocity(vector);
            	event.getEntity().remove();
            }
		}, 10);
	}
	
	private static boolean pull(Block block, final ProjectileLaunchEvent event) {
		final Player player = (Player) event.getEntity().getShooter();
		Collection<Entity> nearbyEntites = block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1);
		
		if(nearbyEntites.size() > 0) {
			for(final Entity entity : nearbyEntites) {
				if(SkillUtils.isValidAttackTarget(entity)) {
					entity.teleport(entity.getLocation().add(0, 0.5, 0));
					event.getEntity().setVelocity(SkillUtils.getVectorForPoints(player.getLocation(), entity.getLocation()));
					DurabilityCalculator.takeDamage(player, player.getEquipment().getItemInMainHand(), false);
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			            public void run() {
			            	entity.setVelocity(SkillUtils.getVectorForPoints(entity.getLocation(), player.getLocation()));
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
