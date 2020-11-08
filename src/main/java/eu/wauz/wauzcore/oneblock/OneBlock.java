package eu.wauz.wauzcore.oneblock;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerOneBlockConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.WauzRewards;

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
	 * Lets a player break the given one block and progress to the next one.
	 * Also handles spawning of mobs and chests.
	 * 
	 * @param player The player who is breaking th block.
	 * @param blockToBreak The one block.
	 */
	public static void breakOneBlock(Player player, Block blockToBreak) {
		if(blockToBreak.getX() % 250 != 0 || blockToBreak.getY() != 70 || blockToBreak.getZ() % 250 != 0 ) {
			return;
		}
		ItemStack equipmentItemStack = player.getEquipment().getItemInMainHand();
		Collection<ItemStack> drops = blockToBreak.getDrops(equipmentItemStack, player);
		Location dropLocation = blockToBreak.getLocation().clone().add(0.5, 1, 0.5);
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
		DurabilityCalculator.increaseDamageOnOneBlock(equipmentItemStack, player);
		if(totalBlocks % 750 == 0) {
			WauzRewards.earnOneBlockToken(player);
		}
	}

}
