package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPetsConfigurator;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;

/**
 * An event to let a player delete a pet permanently.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventPetDelete implements WauzPlayerEvent {
	
	/**
	 * The slot number of the pet to delete.
	 */
	private int petSlot;

	/**
	 * Creates an event to delete the given pet.
	 * 
	 * @param petSlot The slot number of the pet to delete.
	 */
	public WauzPlayerEventPetDelete(int petSlot) {
		this.petSlot = petSlot;
	}
	
	/**
	 * Executes the event for the given player.
	 * Unsummons the pet if it is currently active.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see PlayerConfigurator#setCharacterPetType(Player, int, String)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			String petType = PlayerPetsConfigurator.getCharacterPetType(player, petSlot);
			PlayerPetsConfigurator.setCharacterPetType(player, petSlot, "none");	
			
			Integer activePetSlot = PlayerPetsConfigurator.getCharacterActivePetSlot(player);
			if(activePetSlot != -1 && activePetSlot == petSlot) {
				PetOverviewMenu.unsummon(player);
			}
			player.sendMessage(ChatColor.DARK_PURPLE + petType + " was released to the Wildness!");
			player.closeInventory();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while releasing your pet!");
			player.closeInventory();
			return false;
		}
	}

}
