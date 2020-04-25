package eu.wauz.wauzcore.events;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.collection.PetOptionsMenu;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;

/**
 * An event to let a player move a pet to the breeding station.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventPetBreed implements WauzPlayerEvent {
	
	/**
	 * The slot number of the pet to breed.
	 */
	int petSlot;

	/**
	 * Creates an event to breed the given pet.
	 * 
	 * @param petSlot The slot number of the pet to breed.
	 */
	public WauzPlayerEventPetBreed(int petSlot) {
		this.petSlot = petSlot;
	}
	
	/**
	 * Executes the event for the given player.
	 * Moves the pet to the first free breeding slot.
	 * If both slots are filled now, the hatch timer of the offspring will start.
	 * The hatch timer takes a random value between 1 and 3 hours.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see PetOptionsMenu#move(Player, int, int, boolean)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
			int breedingSlot = PlayerConfigurator.getCharacterPetBreedingFreeSlot(player);
			PetOptionsMenu.move(player, petSlot, breedingSlot, false);
			
			if(PlayerConfigurator.getCharacterPetBreedingFreeSlot(player) == -1) {
				long hatchTime = (2 + new Random().nextInt(5)) * 1800000;
				PlayerConfigurator.setCharacterPetBreedingHatchTime(player, hatchTime + System.currentTimeMillis());
			}
			player.sendMessage(ChatColor.GREEN + petType + " was moved to the Breeding Station!");
			PetOverviewMenu.open(player, -1);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while breeding your pet!");
			player.closeInventory();
			return false;
		}
	}

}
