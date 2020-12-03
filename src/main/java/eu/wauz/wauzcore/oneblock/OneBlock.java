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
		int x = Math.abs(block.getX() % OnePlotManager.PLOT_SIZE);
		int y = block.getY();
		int z = Math.abs(block.getZ() % OnePlotManager.PLOT_SIZE);
		if(x == 0 && y == 70 && z == 0) {
			return true;
		}
		else {
			int b = OnePlotManager.PLOT_SIZE - 1;
			return (x <= 1 || x == b) && (y >= 71 && y < 74) && (z <= 1 || z == b);
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
		int plotSize = OnePlotManager.PLOT_SIZE;
		if(blockToBreak.getX() % plotSize != 0 || blockToBreak.getY() != 70 || blockToBreak.getZ() % plotSize != 0 ) {
			return;
		}
		OneBlockProgression.getPlayerOneBlock(player).progress(blockToBreak);
	}

}
