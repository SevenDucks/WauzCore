package eu.wauz.wauzcore.events;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.util.WauzFileUtils;

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
		String basePath = core.getDataFolder().getAbsolutePath() + "/PlayerData/" + player.getUniqueId() + "/" + characterSlot;
		
		try {
			new File(basePath + ".yml").delete();
			WauzFileUtils.removeFilesRecursive(new File(basePath + "-quests"));
			WauzFileUtils.removeFilesRecursive(new File(basePath + "-relations"));
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
