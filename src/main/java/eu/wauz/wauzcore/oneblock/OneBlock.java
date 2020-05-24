package eu.wauz.wauzcore.oneblock;

import org.bukkit.block.Block;

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

}
