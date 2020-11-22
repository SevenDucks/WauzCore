package eu.wauz.wauzcore.mobs.pets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * A pet ability to help the pet's owner, used every few seconds.
 * 
 * @author Wauzmons
 * 
 * @see WauzPetAbilities
 */
public interface PetAbility {
	
	/**
	 * @return The name of the ability.
	 */
	public String getAbilityName();
	
	/**
	 * @return The description of the ability.
	 */
	public String getAbilityDescription();
	
	/**
	 * @return The needed breeding level for the ability.
	 */
	public int getAbilityLevel();
	
	/**
	 * Executes this ability for the given player and pet.
	 * 
	 * @param player The player bound to this runnable.
	 * @param pet The pet bound to this runnable.
	 */
	public void use(Player player, Entity pet);

}
