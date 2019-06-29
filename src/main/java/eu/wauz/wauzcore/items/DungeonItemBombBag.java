package eu.wauz.wauzcore.items;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;
import net.md_5.bungee.api.ChatColor;

public class DungeonItemBombBag {

	public static void use(final ProjectileHitEvent event) {
		if(event.getEntity().getShooter() != null && !(event.getEntity().getShooter() instanceof Player))
			return;
		
		Entity entity = event.getEntity();
		entity.getWorld().createExplosion(entity.getLocation(), 0);
		
		throwback(entity.getLocation());

		if(event.getHitBlock() != null && event.getHitBlock().getType() == Material.SPONGE) {
			event.getHitBlock().setType(Material.AIR);

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
	            public void run() {
	            	event.getHitBlock().setType(Material.SPONGE);
	            }
			}, 100);
		}
	}
	
	public static void throwback(Location centerLocation) {
		Collection<Entity> nearbyEntites = centerLocation.getWorld().getNearbyEntities(centerLocation, 5, 5, 5);
		
		if(nearbyEntites.size() > 0) {
			for(final Entity entity : nearbyEntites) {
				if(!entity.getType().equals(EntityType.ARMOR_STAND) &&
						entity.getCustomName() != null &&
						entity.getCustomName().contains("" + ChatColor.AQUA)) {
					
					SkillUtils.throwBackEntity(entity, centerLocation, 1.2);
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			            public void run() {
			            	try {
			            		entity.getWorld().playEffect(entity.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			            	}
			            	catch (NullPointerException e) {
			            		WauzDebugger.catchException(getClass(), e);
			            	}
			            }
					}, 7);
				}
			}
		}
	}

	public static void returnBomb(final ProjectileLaunchEvent event) {
		if(event.getEntity().getShooter() != null && !(event.getEntity().getShooter() instanceof Player))
			return;
		
		final Player player = (Player) event.getEntity().getShooter();

		if(!Cooldown.playerBombThrow(player)) {
			event.setCancelled(true);
			return;
		}
		
		final ItemStack bombItemStack = new ItemStack(Material.SNOWBALL, 1);
		ItemMeta bombItemMeta = bombItemStack.getItemMeta();
		bombItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Bag of Bombs");
		bombItemStack.setItemMeta(bombItemMeta);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
        		player.getInventory().setItem(7, bombItemStack);
            }
		}, 3);
	}

}
