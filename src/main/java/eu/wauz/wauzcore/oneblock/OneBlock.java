package eu.wauz.wauzcore.oneblock;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The one infinite block from the one-block gamemode.
 * 
 * @author Wauzmons
 */
public class OneBlock {
	
	/**
	 * Checks if the given block is the one infinite block.
	 * 
	 * @param block The block to check.
	 * 
	 * @return If it is the one block.
	 */
	public static boolean isOneBlock(Block block) {
		if(!block.getWorld().getName().equals("SurvivalOneBlock")) {
			return false;
		}
		return block.getX() % 250 == 0 && block.getY() == 70 && block.getZ() % 250 == 0;
	}
	
	/**
	 * Lets a player break the given one block and progress to the next one.
	 * 
	 * @param player The player who is breaking th block.
	 * @param block The one block.
	 */
	public static void breakOneBlock(Player player, Block block) {
		ItemStack dropItemStack = new ItemStack(block.getType());
		player.getWorld().dropItemNaturally(block.getRelative(BlockFace.UP).getLocation(), dropItemStack);
	}

}
