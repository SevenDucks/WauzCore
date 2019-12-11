package eu.wauz.wauzcore.items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.ItemUtils;

public class InventoryItemRemover {

	private Inventory inventory;
	
	private Map<String, Integer> itemNameAmountMap = new HashMap<>();
	
	public InventoryItemRemover(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public void addItemNameToRemove(String itemName, Integer itemAmount) {
		itemNameAmountMap.put(itemName, itemAmount);
	}
	
	public Inventory execute() {
		for(String itemName : itemNameAmountMap.keySet()) {
			findAndRemoveItems(itemName, itemNameAmountMap.get(itemName));
		}
		return inventory;
	}
	
	private void findAndRemoveItems(String itemName, Integer itemAmount) {
		for(ItemStack itemStack : inventory.getContents()) {			
			if(ItemUtils.isSpecificItem(itemStack, itemName)) {	
				itemAmount = removeItemsGetRemaining(itemStack, itemAmount);
				if(itemAmount == 0)
					return;
			}		
		}
	}
	
	private Integer removeItemsGetRemaining(ItemStack itemStack, Integer itemAmount) {
		while(itemStack.getAmount() != 0 && itemAmount != 0) {
			itemStack.setAmount(itemStack.getAmount() - 1);
			itemAmount--;
		}
		return itemAmount;
	}
}
