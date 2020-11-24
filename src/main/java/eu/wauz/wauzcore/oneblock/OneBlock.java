package eu.wauz.wauzcore.oneblock;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * The one infinite block from the one-block gamemode.
 * 
 * @author Wauzmons
 */
public class OneBlock {
	
	/**
	 * Checks if the given block is the one infinite block or a block in its spawn zone.
	 * 
	 * @param block The block to check.
	 * 
	 * @return If it is the one block.
	 */
	public static boolean isOneBlock(Block block) {
		if(!block.getWorld().getName().equals("SurvivalOneBlock")) {
			return false;
		}
		int x = Math.abs(block.getX() % 250);
		int y = block.getY();
		int z = Math.abs(block.getZ() % 250);
		if(x == 0 && y == 70 && z == 0) {
			return true;
		}
		else {
			return (x <= 1 || x == 249) && (y >= 71 && y < 74) && (z <= 1 || z == 249);
		}
	}
	
	/**
	 * Checks if the block is a valid one block and lets the player break it to progress further.
	 * 
	 * @param player The player who is breaking th block.
	 * @param blockToBreak The one block.
	 * 
	 * @see OneBlockProgression#progress(Block)
	 */
	public static void breakOneBlock(Player player, Block blockToBreak) {
		if(blockToBreak.getX() % 250 != 0 || blockToBreak.getY() != 70 || blockToBreak.getZ() % 250 != 0 ) {
			return;
		}
		// TODO
	}

}
