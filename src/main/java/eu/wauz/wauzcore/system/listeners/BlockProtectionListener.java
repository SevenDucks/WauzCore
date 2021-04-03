package eu.wauz.wauzcore.system.listeners;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;

import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A listener to catch events, related to damaging or modifying blocks.
 * 
 * @author Wauzmons
 */
public class BlockProtectionListener implements Listener {
	
	/**
	 * Prevents leaf decay in MMORPG worlds.
	 * 
	 * @param event The decay event.
	 * 
	 * @see WauzMode#isMMORPG(org.bukkit.World)
	 */
	@EventHandler
	public void onLeafDecay(LeavesDecayEvent event) {
		if(WauzMode.isMMORPG(event.getBlock().getWorld()) || WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The fade event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeFade(BlockFadeEvent event) {
		if(WauzMode.isMMORPG(event.getBlock().getWorld()) || WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The form event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeForm(BlockFormEvent event) {
		if(WauzMode.isMMORPG(event.getBlock().getWorld()) || WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The structure event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onStructureGrow(StructureGrowEvent event) {
		for(BlockState blockState : event.getBlocks()) {
			if(WauzRegion.disallowBlockChange(blockState.getBlock())) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The explode event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		for(Block block : new ArrayList<>(event.blockList())) {
			if(WauzRegion.disallowBlockChange(block)) {
				event.blockList().remove(block);
			}
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The explode event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeExplode(BlockExplodeEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The fertilize event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeFertilize(BlockFertilizeEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The grow event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeGrow(BlockGrowEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The ignite event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangeIgnite(BlockIgniteEvent event) {
		if(WauzRegion.disallowBlockChange(event.getBlock())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The piston extend event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangePistonExtend(BlockPistonExtendEvent event) {
		BlockFace face = event.getDirection();
		if(WauzRegion.disallowBlockChange(event.getBlock()) || WauzRegion.disallowBlockChange(event.getBlock().getRelative(face))) {
			event.setCancelled(true);
			return;
		}
		for(Block block : new ArrayList<>(event.getBlocks())) {
			if(WauzRegion.disallowBlockChange(block) || WauzRegion.disallowBlockChange(block.getRelative(face))) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	/**
	 * Prevents changes to protected regions.
	 * 
	 * @param event The piston retract event.
	 * 
	 * @see WauzRegion#disallowBlockChange(Block)
	 */
	@EventHandler
	public void onBlockChangePistonRetract(BlockPistonRetractEvent event) {
		BlockFace face = event.getDirection();
		if(WauzRegion.disallowBlockChange(event.getBlock()) || WauzRegion.disallowBlockChange(event.getBlock().getRelative(face))) {
			event.setCancelled(true);
			return;
		}
		for(Block block : new ArrayList<>(event.getBlocks())) {
			if(WauzRegion.disallowBlockChange(block) || WauzRegion.disallowBlockChange(block.getRelative(face))) {
				event.setCancelled(true);
				return;
			}
		}
	}

}
