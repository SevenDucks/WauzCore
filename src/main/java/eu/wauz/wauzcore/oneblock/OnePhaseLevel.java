package eu.wauz.wauzcore.oneblock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import eu.wauz.wauzcore.data.OneBlockConfigurator;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * A level of a phase of the one-block gamemode.
 * 
 * @author Wauzmons
 * 
 * @see OnePhase
 */
public class OnePhaseLevel {
	
	/**
	 * The phase that the level is part of.
	 */
	private OnePhase phase;
	
	/**
	 * The key of the phase's level.
	 */
	private String levelKey;
	
	/**
	 * The display name of the level.
	 */
	private String levelName;
	
	/**
	 * How many blocks need to be mined, to proceed to the next level.
	 */
	private int blockAmount;
	
	/**
	 * All the possible blocks spawning in the level.
	 */
	private List<Material> blocks;
	
	/**
	 * Constructs a level of a phase, based on the one-block file in the /WauzCore folder.
	 * 
	 * @param phase The phase that the level is part of.
	 * @param levelKey The key of the phase's level.
	 */
	public OnePhaseLevel(OnePhase phase, String levelKey) {
		this.phase = phase;
		this.levelKey = levelKey;
		levelName = OneBlockConfigurator.getPhaseLevelName(phase.getPhaseKey(), levelKey);
		blockAmount = OneBlockConfigurator.getPhaseLevelBlockAmount(phase.getPhaseKey(), levelKey);
		
		blocks = new ArrayList<>();
		for(String blockString : OneBlockConfigurator.getPhaseLevelBlocks(phase.getPhaseKey(), levelKey)) {
			String[] blockStringParts = blockString.split(" ");
			Material material = Material.valueOf(blockStringParts[0]);
			int probability = Integer.parseInt(blockStringParts[1]);
			for(int count = 0; count < probability; count++) {
				blocks.add(material);
			}
		}
	}
	
	/**
	 * Replaces the given block with a random block of the level.
	 * 
	 * @param block The block to replace.
	 */
	public void placeRandomBlock(Block block) {
		Material material = blocks.get(Chance.randomInt(blocks.size()));
		block.setType(material);
	}

	/**
	 * @return The phase that the level is part of.
	 */
	public OnePhase getPhase() {
		return phase;
	}

	/**
	 * @return The key of the phase's level.
	 */
	public String getLevelKey() {
		return levelKey;
	}

	/**
	 * @return The display name of the level.
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * @return How many blocks need to be mined, to proceed to the next level.
	 */
	public int getBlockAmount() {
		return blockAmount;
	}

	/**
	 * @return All the possible blocks spawning in the level.
	 */
	public List<Material> getBlocks() {
		return blocks;
	}
	
	/**
	 * @return A random material for the next block to spawn.
	 */
	public Material getRandomBlockMaterial() {
		return blocks.get(Chance.randomInt(blocks.size()));
	}
	
}
