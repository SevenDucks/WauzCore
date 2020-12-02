package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.util.Chance;

/**
 * A class for handling the usage of pet abilities.
 * 
 * @author Wauzmons
 *
 * @see WauzPetAbility
 */
public class WauzPetAbilities {
	
	/**
	 * A list of all abilities.
	 */
	private static List<WauzPetAbility> abilities = new ArrayList<>();
	
	/**
	 * A map of all abilities, indexed by ability name.
	 */
	private static Map<String, WauzPetAbility> abilityMap = new HashMap<>();
	
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
	public static List<WauzPetAbility> getAbilitiesForLevel(WauzPetBreedingLevel breedingLevel) {
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
	public static WauzPetAbility getAbilityForLevel(WauzPetBreedingLevel breedingLevel) {
		List<WauzPetAbility> abilities = getAbilitiesForLevel(breedingLevel);
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
	public static WauzPetAbility getAbility(String abilityName) {
		return abilityMap.get(abilityName);
	}
	
	/**
	 * Registers an ability.
	 * 
	 * @param ability The ability to register.
	 */
	public static void registerAbility(WauzPetAbility ability) {
		abilityMap.put(ability.getAbilityName(), ability);
		abilities.add(ability);
		abilities.sort((ability1, ability2) -> Integer.compare(ability1.getAbilityLevel(), ability2.getAbilityLevel()));
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
		if(petEntity == null || !player.getWorld().equals(petEntity.getWorld())) {
			return false;
		}
		WauzPetAbility petAbility = pet.getPetAbility();
		if(petAbility == null) {
			return false;
		}
		player.playSound(petEntity.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 1, 1);
		petAbility.use(player, petEntity);
		return true;
	}

}
