package eu.wauz.wauzcore.items.weapons;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

/**
 * A collection of methods for using the hook weapon.
 * 
 * @author Wauzmons
 */
public class CustomWeaponHook {

	/**
	 * Handles a hook launch and cancels it, when no target was found within 15 blocks.
	 * Pulls the player to the targeted block and makes the hook loose 12 durability, if they are sneaking.
	 * Otherwhise tries to pulls the nearest entity, in line of sight, to the player.
	 * 
	 * @param event The projectile event.
	 * 
	 * @see CustomWeaponHook#pull(Block, ProjectileLaunchEvent)
	 * @see DurabilityCalculator#damageItem(Player, org.bukkit.inventory.ItemStack, int, boolean)
	 */
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

		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1, 0.75f);
		player.teleport(player.getLocation().add(0, 0.5, 0));
		final Vector vector = SkillUtils.getVectorForPoints(player.getLocation(), target);
		event.getEntity().setVelocity(vector);
		DurabilityCalculator.damageItem(player, player.getEquipment().getItemInMainHand(), 12, false);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
            	player.setVelocity(vector);
            	event.getEntity().remove();
            }
		}, 10);
	}
	
	/**
	 * Searches the block for valid attack targets.
	 * If a target was found, the target will be pulled to the player.
	 * 
	 * @param block The block to search for targets.
	 * @param event The projectile event.
	 * 
	 * @return If a target was found.
	 * 
	 * @see SkillUtils#getVectorForPoints(Location, Location)
	 * @see DurabilityCalculator#damageItem(Player, org.bukkit.inventory.ItemStack, boolean)
	 */
	private static boolean pull(Block block, final ProjectileLaunchEvent event) {
		final Player player = (Player) event.getEntity().getShooter();
		Collection<Entity> nearbyEntites = block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1);
		
		if(nearbyEntites.size() > 0) {
			for(final Entity entity : nearbyEntites) {
				if(SkillUtils.isValidAttackTarget(entity)) {
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1, 0.75f);
					entity.teleport(entity.getLocation().add(0, 0.5, 0));
					event.getEntity().setVelocity(SkillUtils.getVectorForPoints(player.getLocation(), entity.getLocation()));
					DurabilityCalculator.damageItem(player, player.getEquipment().getItemInMainHand(), false);
					
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
