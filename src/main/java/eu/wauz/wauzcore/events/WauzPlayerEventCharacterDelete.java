package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * An event for deleting the character data file of a player.
 * Also deletes additional files like the quest datas.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventCharacterDelete implements WauzPlayerEvent {

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 */
	@Override
	public boolean execute(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String characterSlot = playerData.getSelectedCharacterSlot();
		
		try {
			CharacterManager.deleteCharacter(player, characterSlot);
			player.sendMessage(ChatColor.DARK_PURPLE + "Character succesfully deleted!");
			player.closeInventory();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while deleting your character!");
			player.closeInventory();
			return false;
		}
	}

}
