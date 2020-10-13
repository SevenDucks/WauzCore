package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A pet egg that can be used to interact with pets.
 * 
 * @author Wauzmons
 */
public class WauzPetEgg {
	
	/**
	 * Generates an egg item stack for the given pet.
	 * 
	 * @param pet The pet to get an egg item of.
	 * 
	 * @return The generated egg item stack.
	 */
	public ItemStack getEggItem(WauzPet pet) {
		WauzPetRarity rarity = pet.getRarity();
		ItemStack itemStack = new ItemStack(rarity.getMaterial());
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(rarity.getColor() + pet.getKey());
		
		List<String> lores = new ArrayList<>();
		lores.add(ChatColor.WHITE + rarity.getName() + " Pet Egg " + ChatColor.LIGHT_PURPLE + rarity.getStars());
		lores.add("");
		lores.add("Category:" + ChatColor.GREEN + " " + pet.getCategory());
		int maxStat = 2 * rarity.getMultiplier();
		for(WauzPetStat stat : WauzPetStat.getAllPetStats()) {
			lores.add(stat.getName() + ":" + ChatColor.GREEN + " " + 0 + " / " + maxStat);
		}
		lores.add("");
		lores.add(ChatColor.GRAY + "Use while Sneaking to open Menu");
		lores.add(ChatColor.GRAY + "Right Click to summon Pet");
		
		itemMeta.setLore(lores);	
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

}
