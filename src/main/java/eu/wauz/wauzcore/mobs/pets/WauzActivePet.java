package eu.wauz.wauzcore.mobs.pets;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.PetEggUtils;

/**
 * A cached pet, currently summoned by a player.
 * 
 * @author Wauzmons
 */
public class WauzActivePet {
	
	/**
	 * A map of pet stat values, indexed by corresponding stat objects.
	 */
	private Map<WauzPetStat, Integer> petStatMap = new HashMap<>();
	
	/**
	 * Creates a new active pet data.
	 * 
	 * @param eggItemStack The pet egg item stack.
	 */
	public WauzActivePet(ItemStack eggItemStack) {
		for(WauzPetStat stat : WauzPetStat.getAllPetStats()) {
			petStatMap.put(stat, PetEggUtils.getPetStat(eggItemStack, stat));
		}
	}
	
	/**
	 * Gets a stat of the pet.
	 * 
	 * @param stat The stat to get.
	 * 
	 * @return The stat value of the pet.
	 */
	public Integer getPetStat(WauzPetStat stat) {
		return petStatMap.get(stat);
	}

}
