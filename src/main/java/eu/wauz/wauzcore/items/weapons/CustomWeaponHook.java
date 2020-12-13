package eu.wauz.wauzcore.items.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.system.annotations.Item;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A collection of methods for using the hook weapon.
 * 
 * @author Wauzmons
 */
@Item
public class CustomWeaponHook implements CustomWeapon {
	
	/**
	 * Handles the interaction with the item.
	 * Does nothing. Only triggered from projectile events.
	 * 
	 * @param event The interaction event.
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		return;
	}
	
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
		if(WauzMode.isInstance(player.getWorld().getName())) {
			player.sendMessage(ChatColor.RED + "This skill does not work in instances!");
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
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.FISHING_ROD);
	}
	
	/**
	 * Determines if the custom weapon can have a skillgem slot.
	 * 
	 * @return If the custom weapon can have a skillgem slot.
	 */
	@Override
	public boolean canHaveSkillSlot() {
		return false;
	}

	/**
	 * Gets the lores to show on an instance of the custom weapon.
	 * 
	 * @param hasSkillSlot If the weapon has a skillgem slot.
	 */
	@Override
	public List<String> getCustomWeaponLores(boolean hasSkillSlot) {
		List<String> lores = new ArrayList<>();
		lores.add("");
		lores.add(ChatColor.GRAY + "Use while Sneaking to pull you to a Block");
		lores.add(ChatColor.GRAY + "Right Click to grab Enemies");
		return lores;
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
