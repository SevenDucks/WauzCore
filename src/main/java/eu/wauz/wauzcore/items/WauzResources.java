package eu.wauz.wauzcore.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;

/**
 * A class to interact with gatherable resources.
 * 
 * @author Wauzmons
 */
public class WauzResources {
	
	/**
	 * Tries to mine a resource block.
	 * 
	 * @param player The player who tries to mine the block.
	 * @param block The block that is being mined.
	 */
	public static void tryToMine(Player player, Block block) {
		Location location = block.getLocation();
		BlockData bedrockBlockData = Material.BEDROCK.createBlockData();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {

			@Override
			public void run() {
				player.sendBlockChange(location, bedrockBlockData);
			}
			
		}, 1);
		
		ItemStack dropItemStack = new ItemStack(block.getType());
		player.getWorld().playSound(location, Sound.BLOCK_STONE_BREAK, 1, 1);
		player.getWorld().dropItemNaturally(location, dropItemStack);
	}

}
