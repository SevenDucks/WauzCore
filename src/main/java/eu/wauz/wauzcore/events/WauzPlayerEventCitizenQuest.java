package eu.wauz.wauzcore.events;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.menu.QuestBuilder;
import net.md_5.bungee.api.ChatColor;

/**
 * An event that lets a player view a quest of a citizen.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventCitizenQuest implements WauzPlayerEvent {
	
	/**
	 * The name of the citizen to interact with.
	 */
	private String citizenName;
	
	/**
	 * The name of the quest to display.
	 */
	private String questName;
	
	/**
	 * Creates an event to view the quest of the given citizen.
	 * 
	 * @param citizenName The name of the citizen to interact with.
	 * @param questName The name of the quest to display.
	 */
	public WauzPlayerEventCitizenQuest(String citizenName, String questName) {
		this.citizenName = citizenName;
		this.questName = questName;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see QuestBuilder#accept(Player, String)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			QuestBuilder.accept(player, questName);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while interacting with " + citizenName + "!");
			player.closeInventory();
			return false;
		}
	}

}
