package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.PetConfigurator;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * A pet rarity, generated from the pet config file.
 * 
 * @author Wauzmons
 */
public class WauzPetRarity {
	
	/**
	 * A list of all pet rarities.
	 */
	private static List<WauzPetRarity> petRarities = new ArrayList<>();
	
	/**
	 * A map of all pet rarities, indexed by key.
	 */
	private static Map<String, WauzPetRarity> petRarityMap = new HashMap<>();
	
	/**
	 * The tier number of the next rarity.
	 */
	private static int nextRarity = 1;
	
	/**
	 * Initializes all pet rarities and fills the internal pet rarity map.
	 * 
	 * @see PetConfigurator#getPetRarityKeys()
	 */
	public static void init() {
		for(String key : PetConfigurator.getPetRarityKeys()) {
			WauzPetRarity rarity = new WauzPetRarity(key);
			petRarities.add(rarity);
			petRarityMap.put(key, rarity);
		}
	}
	
	/**
	 * Determines the pet rarity of the given item stack's material. 
	 * 
	 * @param itemStack The item stack to get the rarity of.
	 * 
	 * @return The rarity. Returns the lowest rarity when the material is unknown. 
	 */
	public static WauzPetRarity determineRarity(ItemStack itemStack) {
		Material material = itemStack.getType();
		for(WauzPetRarity rarity : petRarities) {
			if(rarity.getMaterial().equals(material)) {
				return rarity;
			}
		}
		return petRarities.get(0);
	}
	
	/**
	 * @param petRarityKey A pet rarity key.
	 * 
	 * @return The pet rarity with that key.
	 */
	public static WauzPetRarity getPetRarity(String petRarityKey) {
		return petRarityMap.get(petRarityKey);
	}
	
	/**
	 * @return A list of all pet rarity keys.
	 */
	public static List<String> getAllPetRariryKeys() {
		return new ArrayList<>(petRarityMap.keySet());
	}
	
	/**
	 * @return A list of all pet rarities.
	 */
	public static List<WauzPetRarity> getAllPetRarities() {
		return new ArrayList<>(petRarities);
	}
	
	/**
	 * @return The count of all pet rarities.
	 */
	public static int getPetRarityCount() {
		return petRarities.size();
	}
	
	/**
	 * The key of the pet rarity.
	 */
	private final String key;
	
	/**
	 * The name of the pet rarity.
	 */
	private final String name;
	
	/**
	 * The max stat multiplier of the pet rarity. Also acts as star count.
	 */
	private final int multiplier;
	
	/**
	 * The stars of the pet rarity.
	 */
	private final String rarityStars;
	
	/**
	 * The color of the pet rarity.
	 */
	private final ChatColor color;
	
	/**
	 * The spawn egg material of the pet rarity.
	 */
	private final Material material;
	
	/**
	 * Constructor for a new pet rarity.
	 * 
	 * @param key The key of the pet rarity.
	 */
	WauzPetRarity(String key) {
		this.key = key;
		this.name = PetConfigurator.getPetRarityName(key);
		this.multiplier = nextRarity++;
		String rarityStars = "";
		for(int index = 0; index < 5; index++) {
			rarityStars += UnicodeUtils.ICON_DIAMOND;
			if(index == multiplier - 1) {
				rarityStars += ChatColor.GRAY;
			}
		}
		this.rarityStars = rarityStars;
		this.color = PetConfigurator.getPetRarityColor(key);
		this.material = PetConfigurator.getPetRarityMaterial(key);
	}

	/**
	 * @return The key of the pet rarity.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return The name of the pet rarity.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The max stat multiplier of the pet rarity. Also acts as star count.
	 */
	public int getMultiplier() {
		return multiplier;
	}
	
	/**
	 * @return The stars of the pet rarity.
	 */
	public String getStars() {
		return rarityStars;
	}


	/**
	 * @return The color of the pet rarity.
	 */
	public ChatColor getColor() {
		return color;
	}

	/**
	 * @return The spawn egg material of the pet rarity.
	 */
	public Material getMaterial() {
		return material;
	}

}
