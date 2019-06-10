package eu.wauz.wauzcore.events;

import java.util.Random;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.menu.PetOptionsMenu;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerEventPetBreed implements WauzPlayerEvent {
	
	int petSlot;

	public WauzPlayerEventPetBreed(int petSlot) {
		this.petSlot = petSlot;
	}
	
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
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while breeding your pet!");
			player.closeInventory();
			return false;
		}
	}

}
