package eu.wauz.wauzcore.events;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerEventPetDelete implements WauzPlayerEvent {
	
	private int petSlot;

	public WauzPlayerEventPetDelete(int petSlot) {
		this.petSlot = petSlot;
	}
	
	@Override
	public boolean execute(Player player) {
		try {
			String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
			PlayerConfigurator.setCharacterPetType(player, petSlot, "none");	
			
			Integer activePetSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
			if(activePetSlot != -1 && activePetSlot == petSlot) {
				PetOverviewMenu.unsummon(player);
			}
			player.sendMessage(ChatColor.DARK_PURPLE + petType + " was released to the Wildness!");
			player.closeInventory();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while releasing your pet!");
			player.closeInventory();
			return false;
		}
	}

}
