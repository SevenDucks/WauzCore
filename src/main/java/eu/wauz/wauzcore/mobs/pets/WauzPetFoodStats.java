package eu.wauz.wauzcore.mobs.pets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.PetEggUtils;

/**
 * A data object, containing the stats of a pet food item.
 * 
 * @author Wauzmons
 */
public class WauzPetFoodStats {
	
	/**
	 * A map of pet food stat values, indexed by corresponding stat objects.
	 */
	private Map<WauzPetStat, Integer> petFoodStatMap = new HashMap<>();
	
	/**
	 * Creates a food stats instance, based on the given item stack.
	 * 
	 * @param foodItemStack The pet food item stack, tp get the stats from.
	 */
	public WauzPetFoodStats(ItemStack foodItemStack) {
		for(WauzPetStat stat : WauzPetStat.getAllPetStats()) {
			int value = PetEggUtils.getPetFoodStat(foodItemStack, stat);
			if(value > 0) {
				petFoodStatMap.put(stat, value);
			}
		}
	}
	
	/**
	 * Gets a stat of the pet food.
	 * 
	 * @param stat The stat to get.
	 * 
	 * @return The stat value of the pet food.
	 */
	public int getPetFoodStat(WauzPetStat stat) {
		return petFoodStatMap.get(stat);
	}
	
	/**
	 * Tries to apply the stats to a pet egg.
	 * 
	 * @param eggItemStack The pet egg item stat.
	 * 
	 * @return If any stats were applied.
	 */
	public boolean tryToApply(ItemStack eggItemStack) {
		boolean result = false;
		for(Entry<WauzPetStat, Integer> entry : petFoodStatMap.entrySet()) {
			WauzPetStat stat = entry.getKey();
			int currentValue = PetEggUtils.getPetStat(eggItemStack, stat);
			int maximumValue = PetEggUtils.getMaxPetStat(eggItemStack, stat);
			if(currentValue < maximumValue) {
				currentValue = currentValue + entry.getValue() <= maximumValue ? currentValue + entry.getValue() : maximumValue;
				PetEggUtils.setPetStat(eggItemStack, stat, currentValue, maximumValue);
				return true;
			}
		}
		return result;
	}

}
