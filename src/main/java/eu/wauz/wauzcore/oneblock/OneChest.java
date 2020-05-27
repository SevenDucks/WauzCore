package eu.wauz.wauzcore.oneblock;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.OneBlockConfigurator;

/**
 * A reward chest of a phase of the one-block gamemode.
 * 
 * @author Wauzmons
 *
 * @see OnePhase
 * @see OneChestItem
 */
public class OneChest {

	/**
	 * The phase that the chest is part of.
	 */
	private OnePhase phase;
	
	/**
	 * The type of the chest.
	 */
	private OneChestType type;
	
	/**
	 * How many item stacks are contained in the chest.
	 */
	private int stackCount;
	
	/**
	 * The possible item stacks that can be found in the chest.
	 */
	private List<OneChestItem> contentItemStacks;
	
	/**
	 * Constructs a reward chest of a phase, based on the one-block file in the /WauzCore folder.
	 * 
	 * @param phase The phase that the chest is part of.
	 * @param type The type of the chest.
	 */
	public OneChest(OnePhase phase, OneChestType type) {
		this.phase = phase;
		this.type = type;
		stackCount = OneBlockConfigurator.getChestStackCount(phase.getPhaseKey(), type);
		
		contentItemStacks = new ArrayList<>();
		for(String itemString : OneBlockConfigurator.getChestContentStrings(phase.getPhaseKey(), type)) {
			OneChestItem.create(this, itemString);
		}
	}

	/**
	 * @return The phase that the chest is part of.
	 */
	public OnePhase getPhase() {
		return phase;
	}

	/**
	 * @return The type of the chest.
	 */
	public OneChestType getType() {
		return type;
	}

	/**
	 * @return How many item stacks are contained in the chest.
	 */
	public int getStackCount() {
		return stackCount;
	}

	/**
	 * @return The possible item stacks that can be found in the chest.
	 */
	public List<OneChestItem> getContentItemStacks() {
		return contentItemStacks;
	}
	
}
