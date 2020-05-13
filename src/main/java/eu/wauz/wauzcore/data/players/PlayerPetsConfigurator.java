package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerConfigurationUtils;

/**
 * Configurator to fetch or modify pet data from the Player.yml files.
 * 
 * @author Wauzmons
 */
public class PlayerPetsConfigurator extends PlayerConfigurationUtils {

// Pets - General

	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The active pet slot.
	 */
	public static int getCharacterActivePetSlot(Player player) {
		return playerConfigGetInt(player, "pets.active.slot", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The new active pet slot.
	 */
	public static void setCharacterActivePetSlot(Player player, int petSlot) {
		playerConfigSet(player, "pets.active.slot", petSlot, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The active pet uuid.
	 */
	public static String getCharacterActivePetId(Player player) {
		return playerConfigGetString(player, "pets.active.id", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petId The new active pet uuid.
	 */
	public static void setCharacterActivePetId(Player player, String petId) {
		playerConfigSet(player, "pets.active.id", petId, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The type of the pet.
	 */
	public static String getCharacterPetType(Player player, int petSlot) {
		return playerConfigGetString(player, "pets.slot" + petSlot + ".type", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The new slot of the pet.
	 * @param petType The new type of the pet.
	 */
	public static void setCharacterPetType(Player player, int petSlot, String petType) {
		if(petType.equals("none")) {
			playerConfigSet(player, "pets.slot" + petSlot, "", true);
		}
		playerConfigSet(player, "pets.slot" + petSlot + ".type", petType, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return How many pet slots are used.
	 */
	public static int getCharacterUsedPetSlots(Player player) {
		int usedPetSlots = 0;
		for(int petSlot = 0; petSlot < 5; petSlot++) {
			String petType = getCharacterPetType(player, petSlot);
			if(!petType.equals("none")) {
				usedPetSlots++;
			}
		}
		return usedPetSlots;
	}
	
// Pets - Intelligence
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The intelligence points of the pet.
	 */
	public static int getCharacterPetIntelligence(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.int", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param intelligence The new intelligence points of the pet.
	 */
	public static void setCharacterPetIntelligence(Player player, int petSlot, int intelligence) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.int", intelligence, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return  The maximum intelligence points of the pet.
	 */
	public static int getCharacterPetIntelligenceMax(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.intmax", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param intelligenceMax The new maximum intelligence points of the pet.
	 */
	public static void setCharacterPetIntelligenceMax(Player player, int petSlot, int intelligenceMax) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.intmax", intelligenceMax, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return If the intelligence stat of the pet is maxed.
	 */
	public static boolean isCharacterPetIntelligenceMaxed(Player player, int petSlot) {
		return getCharacterPetIntelligence(player, petSlot) >= getCharacterPetIntelligenceMax(player, petSlot);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The needed experience to increase the pet's intelligence.
	 */
	public static int getCharacterPetIntelligenceExpNeeded(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.intexp", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param neededExp The new needed experience to increase the pet's intelligence.
	 */
	public static void setCharacterPetIntelligenceExpNeeded(Player player, int petSlot, int neededExp) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.intexp", neededExp, true);
	}
	
// Pets - Dexterity
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The dexterity points of the pet.
	 */
	public static int getCharacterPetDexterity(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.dex", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param dexterity The new dexterity points of the pet.
	 */
	public static void setCharacterPetDexterity(Player player, int petSlot, int dexterity) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.dex", dexterity, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The maximum dexterity points of the pet.
	 */
	public static int getCharacterPetDexterityMax(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.dexmax", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param dexterityMax The new maximum dexterity points of the pet.
	 */
	public static void setCharacterPetDexterityMax(Player player, int petSlot, int dexterityMax) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.dexmax", dexterityMax, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return If the dexterity stat of the pet is maxed.
	 */
	public static boolean isCharacterPetDexterityMaxed(Player player, int petSlot) {
		return getCharacterPetDexterity(player, petSlot) >= getCharacterPetDexterityMax(player, petSlot);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The needed experience to increase the pet's dexterity.
	 */
	public static int getCharacterPetDexterityExpNeeded(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.dexexp", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param neededExp The new needed experience to increase the pet's dexterity.
	 */
	public static void setCharacterPetDexterityExpNeeded(Player player, int petSlot, int neededExp) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.dexexp", neededExp, true);
	}
	
// Pets - Absorbtion
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The absorption points of the pet.
	 */
	public static int getCharacterPetAbsorption(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.abs", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param absorption The new absorption points of the pet.
	 */
	public static void setCharacterPetAbsorption(Player player, int petSlot, int absorption) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.abs", absorption, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The maximum absorption points of the pet.
	 */
	public static int getCharacterPetAbsorptionMax(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.absmax", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param absorptionMax The new maximum absorption points of the pet.
	 */
	public static void setCharacterPetAbsorptionMax(Player player, int petSlot, int absorptionMax) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.absmax", absorptionMax, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return If the absorption stat of the pet is maxed.
	 */
	public static boolean isCharacterPetAbsorptionMaxed(Player player, int petSlot) {
		return getCharacterPetAbsorption(player, petSlot) >= getCharacterPetAbsorptionMax(player, petSlot);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return The needed experience to increase the pet's absorption.
	 */
	public static int getCharacterPetAbsorptionExpNeeded(Player player, int petSlot) {
		return playerConfigGetInt(player, "pets.slot" + petSlot + ".stats.absexp", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * @param neededExp The new needed experience to increase the pet's absorption.
	 */
	public static void setCharacterPetAbsorptionExpNeeded(Player player, int petSlot, int neededExp) {
		playerConfigSet(player, "pets.slot" + petSlot + ".stats.absexp", neededExp, true);
	}
	
// Pets - Breeding
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The next free pet breeding slot or -1, if both are in use.
	 */
	public static int getCharacterPetBreedingFreeSlot(Player player) {
		if(getCharacterPetType(player, 6).equals("none")) {
			return 6;
		}
		if(getCharacterPetType(player, 8).equals("none")) {
			return 8;
		}
		return -1;
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param petSlot The slot of the pet.
	 * 
	 * @return Why pet breeding should be disallowed.
	 */
	public static String getCharcterPetBreedingDisallowString(Player player, int petSlot) {
		if(getCharacterPetBreedingFreeSlot(player) == -1) {
			return "Breeding Station is in use!";
		}
		if(!isCharacterPetIntelligenceMaxed(player, petSlot)
				&& !isCharacterPetDexterityMaxed(player, petSlot)
				&& !isCharacterPetAbsorptionMaxed(player, petSlot)) {
			
			return "Pet needs at least 1 maxed Stat!";
		}
		return null;
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The time till the pet egg hatches.
	 */
	public static long getCharacterPetBreedingHatchTime(Player player) {
		return playerConfigGetLong(player, "pets.egg.time", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @param hatchTime The new time till the pet egg hatches.
	 */
	public static void setCharacterPetBreedingHatchTime(Player player, long hatchTime) {
		playerConfigSet(player, "pets.egg.time", hatchTime, true);
	}
	
}
