package eu.wauz.wauzcore.oneblock;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerOneBlockConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.WauzRewards;

/**
 * The progrss of a player in the one-block gamemode.
 * 
 * @author Wauzmons
 */
public class OneBlockProgression {
	
	/**
	 * A map of cached one-block progressions by player.
	 */
	private static HashMap<Player, OneBlockProgression> storage = new HashMap<>();
	
	/**
	 * The player that owns the one block.
	 */
	private Player player;
	
	/**
	 * The one-block phase number of the player.
	 */
	private int phaseNo;
	
	/**
	 * The one-block level number of the player.
	 */
	private int levelNo;
	
	/**
	 * The one-block current block number of the player.
	 */
	private int blockNo;
	
	/**
	 * The one-block phase of the player.
	 */
	private OnePhase phase;
	
	/**
	 * The one-block level of the player.
	 */
	private OnePhaseLevel level;
	
	/**
	 * The highest reached one-block phase of the player.
	 */
	private int highestPhase;
	
	/**
	 * The one-block total blocks destroyed of the player.
	 */
	private int totalBlocks;
	
	/**
	 * Fetches a cached one-block progressions.
	 * 
	 * @param player The player that owns the one block.
	 * 
	 * @return The requested one-block progressions.
	 */
	public static OneBlockProgression getPlayerOneBlock(Player player) {
		return storage.get(player);
	}

	/**
	 * Registers a player, by creating a new one-block progressions.
	 * 
	 * @param player The player that owns the one block.
	 * 
	 * @return The created one-block progressions.
	 */
	public static OneBlockProgression regPlayerOneBlock(Player player) {
		OneBlockProgression progression = new OneBlockProgression(player);
		storage.put(player, progression);
		return progression;
	}
	
	/**
	 * Creates a new one-block progression instance for the given player.
	 * 
	 * @param player The player that owns the one block.
	 */
	private OneBlockProgression(Player player) {
		this.player = player;
		load();
		if(phaseNo > OnePhase.count()) {
			phaseNo = 1;
		}
		phase = OnePhase.get(phaseNo);
		if(levelNo > phase.levelCount()) {
			levelNo = 1;
		}
		level = phase.getLevel(levelNo);
		if(blockNo > level.getBlockAmount()) {
			blockNo = 1;
		}
	}
	
	/**
	 * Loads all progression values.
	 */
	private void load() {
		phaseNo = PlayerOneBlockConfigurator.getPhase(player);
		levelNo = PlayerOneBlockConfigurator.getLevel(player);
		blockNo = PlayerOneBlockConfigurator.getBlock(player);
		highestPhase = PlayerOneBlockConfigurator.getHighestPhase(player);
		totalBlocks = PlayerOneBlockConfigurator.getTotalBlocks(player);
	}
	
	/**
	 * Saves all progression values.
	 */
	public void save() {
		PlayerOneBlockConfigurator.setPhase(player, phaseNo);
		PlayerOneBlockConfigurator.setLevel(player, levelNo);
		PlayerOneBlockConfigurator.setBlock(player, blockNo);
		PlayerOneBlockConfigurator.setHighestPhase(player, highestPhase);
		PlayerOneBlockConfigurator.setTotalBlocks(player, totalBlocks);
	}
	
	/**
	 * Lets the player break the given one block and progress to the next one.
	 * Also handles spawning of mobs and chests.
	 * 
	 * @param blockToBreak The one block.
	 */
	public void progress(Block blockToBreak) {
		ItemStack equipmentItemStack = player.getEquipment().getItemInMainHand();
		if(++blockNo == level.getBlockAmount()) {
			OneChestType chestType = OneChestType.getRandomChestType();
			OneChest chest = phase.getChests().get(chestType);
			blockToBreak.breakNaturally(equipmentItemStack, true);
			chest.spawnRandomFilledChest(blockToBreak);
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
					if(phaseNo > highestPhase) {
						highestPhase = phaseNo;
					}
				}
				phase = OnePhase.get(phaseNo);
				level = phase.getLevel(levelNo);
				player.sendMessage(ChatColor.GREEN + "You advanced to [" + phase.getPhaseName() + " " + level.getLevelName() + "]");
			}
			blockToBreak.breakNaturally(equipmentItemStack, true);
			level.placeRandomBlock(blockToBreak);
			phase.tryToSpawnMob(blockToBreak.getLocation().clone().add(0.5, 1, 0.5));
		}
		DurabilityCalculator.increaseDamageOnOneBlock(equipmentItemStack, player);
		if(++totalBlocks % 750 == 0) {
			WauzRewards.earnOneBlockToken(player);
		}
	}

}
