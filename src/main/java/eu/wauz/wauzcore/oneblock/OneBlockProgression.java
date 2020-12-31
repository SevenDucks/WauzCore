package eu.wauz.wauzcore.oneblock;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
		OneBlockProgression progression = storage.get(player);
		return progression != null ? progression : regPlayerOneBlock(player);
	}

	/**
	 * Registers a player, by creating a new one-block progressions.
	 * 
	 * @param player The player that owns the one block.
	 * 
	 * @return The created one-block progressions.
	 */
	private static OneBlockProgression regPlayerOneBlock(Player player) {
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
	}
	
	/**
	 * Loads all progression values.
	 * 
	 * @param playerDataConfig The player's save file.
	 */
	public void load(FileConfiguration playerDataConfig) {
		phaseNo = playerDataConfig.getInt("oneblock.phase");
		levelNo = playerDataConfig.getInt("oneblock.level");
		blockNo = playerDataConfig.getInt("oneblock.block");
		highestPhase = playerDataConfig.getInt("oneblock.maxphase");
		totalBlocks = playerDataConfig.getInt("oneblock.blocks");
		
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
	 * Saves all progression values.
	 * 
	 * @param playerDataConfig The player's save file.
	 */
	public void save(FileConfiguration playerDataConfig) {
		playerDataConfig.set("oneblock.phase", phaseNo);
		playerDataConfig.set("oneblock.level", levelNo);
		playerDataConfig.set("oneblock.block", blockNo);
		playerDataConfig.set("oneblock.maxphase", highestPhase);
		playerDataConfig.set("oneblock.blocks", totalBlocks);
	}
	
	/**
	 * Lets the player break the given one block and progress to the next one.
	 * Also handles spawning of mobs and chests.
	 * 
	 * @param blockToBreak The one block.
	 */
	public void progress(Block blockToBreak) {
		ItemStack equipmentItemStack = player.getEquipment().getItemInMainHand();
		if(++blockNo % 100 == 0) {
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
		if(++totalBlocks % 300 == 0) {
			WauzRewards.earnOneBlockToken(player);
		}
	}

	/**
	 * @return The one-block phase of the player.
	 */
	public OnePhase getPhase() {
		return phase;
	}

	/**
	 * @return The one-block level of the player.
	 */
	public OnePhaseLevel getLevel() {
		return level;
	}
	
	/**
	 * @return The one-block current block number of the player.
	 */
	public int getBlockNo() {
		return blockNo;
	}

	/**
	 * @return The one-block total blocks destroyed of the player.
	 */
	public int getTotalBlocks() {
		return totalBlocks;
	}

}
