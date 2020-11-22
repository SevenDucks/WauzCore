package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.util.Chance;

/**
 * A class for handling the usage of pet abilities.
 * 
 * @author Wauzmons
 *
 * @see PetAbility
 */
public class WauzPetAbilities {
	
	/**
	 * A list of all abilities.
	 */
	private static List<PetAbility> abilities = new ArrayList<>();
	
	/**
	 * A map of all abilities, indexed by ability name.
	 */
	private static Map<String, PetAbility> abilityMap = new HashMap<>();
	
	/**
	 * @return A list of all ability keys.
	 */
	public static List<String> getAllAbilityKeys() {
		return new ArrayList<>(abilityMap.keySet());
	}
	
	/**
	 * Gets a list of all abilities obtainable at the given level.
	 * 
	 * @param breedingLevel The breeding level.
	 * 
	 * @return The list of abilities.
	 */
	public static List<PetAbility> getAbilitiesForLevel(WauzPetBreedingLevel breedingLevel) {
		return abilities.stream()
				.filter(ability -> ability.getAbilityLevel() <= breedingLevel.getLevel())
				.collect(Collectors.toList());
	}
	
	/**
	 * Gets a random ability obtainable at the given level.
	 * 
	 * @param breedingLevel The breeding level.
	 * 
	 * @return The ability or null, if no matches.
	 */
	public static PetAbility getAbilityForLevel(WauzPetBreedingLevel breedingLevel) {
		List<PetAbility> abilities = getAbilitiesForLevel(breedingLevel);
		if(abilities.isEmpty()) {
			return null;
		}
		return abilities.get(Chance.randomInt(abilities.size()));
	}
	
	/**
	 * Gets an ability for the given name from the map.
	 * 
	 * @param abilityName The name of the ability.
	 * 
	 * @return The ability or null, if not found.
	 */
	public static PetAbility getAbility(String abilityName) {
		return abilityMap.get(abilityName);
	}
	
	/**
	 * Registers an ability.
	 * 
	 * @param ability The ability to register.
	 */
	public static void registerAbility(PetAbility ability) {
		abilities.add(ability);
		abilityMap.put(ability.getAbilityName(), ability);
	}
	
	/**
	 * Lets a player try to activate their pet's ability.
	 * Only works when a pet with valid ability is active.
	 * 
	 * @param player The player who tries to activate the ability.
	 * 
	 * @return If the ability usage was successful.
	 */
	public static boolean tryToUse(Player player) {
		WauzActivePet pet = WauzActivePet.getPet(player);
		if(pet == null) {
			return false;
		}
		Entity petEntity = pet.getPetEntity();
		if(petEntity == null) {
			return false;
		}
		PetAbility petAbility = pet.getPetAbility();
		if(petAbility == null) {
			return false;
		}
		petAbility.use(player, petEntity);
		return true;
	}

}
