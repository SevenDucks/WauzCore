package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.entity.Horse;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.PetConfigurator;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * A pet, generated from the pet config file.
 * 
 * @author Wauzmons
 */
public class WauzPet {
	
	/**
	 * A map of all pets, indexed by key.
	 */
	private static Map<String, WauzPet> petMap = new HashMap<>();
	
	/**
	 * Initializes all pets and fills the internal pet map.
	 * 
	 * @see PetConfigurator#getPetKeys()
	 */
	public static void init() {
		WauzPetStat.init();
		WauzPetRarity.init();
		WauzPetBreedingLevel.init();
		
		for(String key : PetConfigurator.getPetKeys()) {
			petMap.put(key, new WauzPet(key));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + petMap.size() + " Pets!");
	}
	
	/**
	 * @param petKey A pet key.
	 * 
	 * @return The pet with that key.
	 */
	public static WauzPet getPet(String petKey) {
		return petMap.get(petKey);
	}
	
	/**
	 * @param rarity The pet rarity of the parents.
	 * @param category The pet category.
	 * 
	 * @return The pets matching the filter criteria.
	 */
	public static List<WauzPet> getMatchingPets(WauzPetRarity rarity, String category) {
		return getAllPets().stream()
			.filter(pet -> pet.getRarity().equals(rarity))
			.filter(pet -> pet.getCategory().equals(category))
			.collect(Collectors.toList());
	}
	
	/**
	 * @return A list of all pets.
	 */
	public static List<WauzPet> getAllPets() {
		return new ArrayList<>(petMap.values());
	}
	
	/**
	 * @return A list of all pet keys.
	 */
	public static List<String> getAllPetKeys() {
		return new ArrayList<>(petMap.keySet());
	}
	
	/**
	 * Gets a random new pet, based off its parent's rarity and category.
	 * 30% chance for a higher rarity, 60% for the same, 10% for a lower one.
	 * 
	 * @param rarity The pet rarity of the parents.
	 * @param category The pet category.
	 * 
	 * @return The offspring pet.
	 */
	public static WauzPet getOffspring(WauzPetRarity rarity, String category) {
		int randomizer = Chance.randomInt(100);
		if(randomizer < 30) {
			return getHigherRarityOffspring(rarity, category);
		}
		else if(randomizer < 90) {
			return getSameRarityOffspring(rarity, category);
		}
		else {
			return getLowerRarityOffspring(rarity, category);
		}
	}
	
	/**
	 * Gets a new random pet, with a higher rarity than its parents.
	 * If that is not possible, a same rarity pet is returned.
	 * 
	 * @param rarity The pet rarity of the parents.
	 * @param category The pet category.
	 * 
	 * @return The offspring pet.
	 */
	private static WauzPet getHigherRarityOffspring(WauzPetRarity rarity, String category) {
		int oldRarityNumber = rarity.getMultiplier();
		if(oldRarityNumber >= WauzPetRarity.getPetRarityCount()) {
			return getSameRarityOffspring(rarity, category);
		}
		
		WauzPetRarity newRarity = WauzPetRarity.getAllPetRarities().get(oldRarityNumber);
		List<WauzPet> pets = getMatchingPets(newRarity, category);
		if(pets.isEmpty()) {
			return getSameRarityOffspring(rarity, category);
		}
		return pets.get(Chance.randomInt(pets.size()));
	}
	
	/**
	 * Gets a new random pet, with a lower rarity than its parents.
	 * If that is not possible, a same rarity pet is returned.
	 * 
	 * @param rarity The pet rarity of the parents.
	 * @param category The pet category.
	 * 
	 * @return The offspring pet.
	 */
	private static WauzPet getLowerRarityOffspring(WauzPetRarity rarity, String category) {
		int oldRarityNumber = rarity.getMultiplier();
		if(oldRarityNumber <= 1) {
			return getSameRarityOffspring(rarity, category);
		}
		
		WauzPetRarity newRarity = WauzPetRarity.getAllPetRarities().get(oldRarityNumber - 2);
		List<WauzPet> pets = getMatchingPets(newRarity, category);
		if(pets.isEmpty()) {
			return getSameRarityOffspring(rarity, category);
		}
		return pets.get(Chance.randomInt(pets.size()));
	}
	
	/**
	 * Gets a new random pet, with the same rarity as its parents.
	 * 
	 * @param rarity The pet rarity of the parents.
	 * @param category The pet category.
	 * 
	 * @return The offspring pet.
	 */
	private static WauzPet getSameRarityOffspring(WauzPetRarity rarity, String category) {
		List<WauzPet> pets = getMatchingPets(rarity, category);
		if(pets.isEmpty()) {
			return null;
		}
		return pets.get(Chance.randomInt(pets.size()));
	}
	
	/**
	 * The key of the pet.
	 */
	private String key;
	
	/**
	 * The name of the pet.
	 */
	private String name;
	
	/**
	 * The category of the pet.
	 */
	private String category;
	
	/**
	 * The rarity of the pet.
	 */
	private WauzPetRarity rarity;
	
	/**
	 * The random messages of the pet.
	 */
	private List<String> messages;
	
	/**
	 * The horse color of the pet. Null if not a horse.
	 */
	private Horse.Color horseColor;
	
	/**
	 * Constructor for a new pet.
	 * 
	 * @param key The key of the pet.
	 */
	private WauzPet(String key) {
		this.key = key;
		this.name = PetConfigurator.getName(key);
		this.category = PetConfigurator.getCategory(key);
		if(category.equals("Horse")) {
			horseColor = Horse.Color.valueOf(name.toUpperCase());
		}
		this.rarity = PetConfigurator.getRarity(key);
		this.messages = PetConfigurator.getMessages(key);
	}

	/**
	 * @return The key of the pet.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return The name of the pet.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The category of the pet.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return The rarity of the pet.
	 */
	public WauzPetRarity getRarity() {
		return rarity;
	}
	
	/**
	 * @return A random messages of the pet or null, if none defined.
	 */
	public String getRandomMessage() {
		if(messages == null || messages.isEmpty()) {
			return null;
		}
		return messages.get(Chance.randomInt(messages.size()));
	}
	
	/**
	 * @return The horse color of the pet. Null if not a horse.
	 */
	public Horse.Color getHorseColor() {
		return horseColor;
	}

	/**
	 * @return If the pet is a horse.
	 */
	public boolean isHorse() {
		return horseColor != null;
	}

}
