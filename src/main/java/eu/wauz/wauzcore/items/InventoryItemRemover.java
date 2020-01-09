package eu.wauz.wauzcore.items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.ItemUtils;

/**
 * A class to count and remove specific items from a player inventory.
 * 
 * @author Wauzmons
 */
public class InventoryItemRemover {

	/**
	 * The inventory, to count and remove items from.
	 */
	private Inventory inventory;
	
	/**
	 * A map of the amounts to remove, indexed by item name.
	 */
	private Map<String, Integer> itemNameAmountMap = new HashMap<>();
	
	/**
	 * Creates a new inventory item remover for the given inventory.
	 * 
	 * @param inventory The inventory, to count and remove items from.
	 */
	public InventoryItemRemover(Inventory inventory) {
		this.inventory = inventory;
	}
	
	/**
	 * Adds an item to remove from the inventory.
	 * 
	 * @param itemName The name of the item.
	 * @param itemAmount The amount to remove.
	 */
	public void addItemNameToRemove(String itemName, Integer itemAmount) {
		itemNameAmountMap.put(itemName, itemAmount);
	}
	
	/**
	 * Removes all the registered items from the inventory.
	 * 
	 * @return The cleaned up inventory.
	 * 
	 * @see InventoryItemRemover#findAndRemoveItems(String, Integer)
	 */
	public Inventory execute() {
		for(String itemName : itemNameAmountMap.keySet()) {
			findAndRemoveItems(itemName, itemNameAmountMap.get(itemName));
		}
		return inventory;
	}
	
	/**
	 * Removes the given amount of the given item from the inventory.
	 * 
	 * @param itemName The name of the item.
	 * @param itemAmount The amount to remove.
	 * 
	 * @see InventoryItemRemover#removeItemsGetRemaining(ItemStack, Integer)
	 */
	private void findAndRemoveItems(String itemName, Integer itemAmount) {
		for(ItemStack itemStack : inventory.getContents()) {			
			if(ItemUtils.isSpecificItem(itemStack, itemName)) {	
				itemAmount = removeItemsGetRemaining(itemStack, itemAmount);
				if(itemAmount == 0) {
					return;
				}
			}		
		}
	}
	
	/**
	 * Removes items from a stack, till the stack is empty or the given amount is reached.
	 * 
	 * @param itemStack The item stack to remove from.
	 * @param itemAmount The amount to remove.
	 * 
	 * @return The amount left to remove from another stack.
	 */
	private Integer removeItemsGetRemaining(ItemStack itemStack, Integer itemAmount) {
		while(itemStack.getAmount() != 0 && itemAmount != 0) {
			itemStack.setAmount(itemStack.getAmount() - 1);
			itemAmount--;
		}
		return itemAmount;
	}
}
