package eu.wauz.wauzcore.oneblock;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerOneBlockConfigurator;
import eu.wauz.wauzcore.items.WauzRewards;

/**
 * The one infinite block from the one-block gamemode.
 * 
 * @author Wauzmons
 */
public class OneBlock {
	
	/**
	 * Checks if the given block is the one infinite block or a block on top of it.
	 * 
	 * @param block The block to check.
	 * 
	 * @return If it is the one block.
	 */
	public static boolean isOneBlock(Block block) {
		if(!block.getWorld().getName().equals("SurvivalOneBlock")) {
			return false;
		}
		return block.getX() % 250 == 0 && block.getY() >= 70 && block.getY() < 75 && block.getZ() % 250 == 0;
	}
	
	/**
	 * Lets a player break the given one block and progress to the next one.
	 * Also handles spawning of mobs and chests.
	 * 
	 * @param player The player who is breaking th block.
	 * @param blockToBreak The one block.
	 */
	public static void breakOneBlock(Player player, Block blockToBreak) {
		if(blockToBreak.getY() != 70) {
			return;
		}
		Collection<ItemStack> drops = blockToBreak.getDrops(player.getEquipment().getItemInMainHand(), player);
		Location dropLocation = blockToBreak.getRelative(BlockFace.UP).getLocation();
		for(ItemStack dropItemStack : drops) {
			player.getWorld().dropItemNaturally(dropLocation, dropItemStack);
		}
		
		int phaseNo = PlayerOneBlockConfigurator.getPhase(player);
		int levelNo = PlayerOneBlockConfigurator.getLevel(player);
		int blockNo = PlayerOneBlockConfigurator.getBlock(player);
		if(phaseNo > OnePhase.count()) {
			phaseNo = 1;
			PlayerOneBlockConfigurator.setPhase(player, phaseNo);
		}
		OnePhase phase = OnePhase.get(phaseNo);
		if(levelNo > phase.levelCount()) {
			levelNo = 1;
			PlayerOneBlockConfigurator.setLevel(player, levelNo);
		}
		OnePhaseLevel level = phase.getLevel(levelNo);
		if(blockNo > level.getBlockAmount()) {
			blockNo = 1;
		}
		else {
			blockNo++;
		}
		
		if(blockNo == level.getBlockAmount()) {
			OneChestType chestType = OneChestType.getRandomChestType();
			OneChest chest = phase.getChests().get(chestType);
			chest.spawnRandomFilledChest(blockToBreak);
			PlayerOneBlockConfigurator.setBlock(player, blockNo);
		}
		else {
			if(blockNo > level.getBlockAmount()) {
				blockNo = 1;
				levelNo++;
				if(levelNo > phase.levelCount()) {
					levelNo = 1;
					phaseNo++;
					if(phaseNo > OnePhase.count()) {
						phaseNo = 1;
					}
					PlayerOneBlockConfigurator.setPhase(player, phaseNo);
					int highestPhase = PlayerOneBlockConfigurator.getHighestPhase(player);
					if(phaseNo > highestPhase) {
						PlayerOneBlockConfigurator.setHighestPhase(player, phaseNo);
					}
				}
				PlayerOneBlockConfigurator.setLevel(player, levelNo);
				phase = OnePhase.get(phaseNo);
				level = phase.getLevel(levelNo);
				player.sendMessage(ChatColor.GREEN + "You advanced to [" + phase.getPhaseName() + " " + level.getLevelName() + "]");
			}
			phase.tryToSpawnMob(dropLocation);
			level.placeRandomBlock(blockToBreak);
			PlayerOneBlockConfigurator.setBlock(player, blockNo);
		}
		int totalBlocks = PlayerOneBlockConfigurator.getTotalBlocks(player) + 1;
		PlayerOneBlockConfigurator.setTotalBlocks(player, totalBlocks);
		if(totalBlocks % 750 == 0) {
			WauzRewards.earnOneBlockToken(player);
		}
	}

}
