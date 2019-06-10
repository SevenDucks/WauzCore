package eu.wauz.wauzcore.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import eu.wauz.wauzcore.WauzCore;

public class DungeonItemThunderRod {
	
	public static void use(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Location target = null;
		
		for(Block block : player.getLineOfSight(null, 15)) {
			if(block.getType().equals(Material.SPONGE)) {
				target = block.getLocation();
				break;
			}
		}
		
		if(target == null) {
			event.setCancelled(true);
			return;
		}
		
		player.getWorld().strikeLightning(target);	
		final Block block = player.getWorld().getBlockAt(target);
		block.setType(Material.REDSTONE_BLOCK);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
            	block.setType(Material.SPONGE);
            }
		}, 100);
	}

}
