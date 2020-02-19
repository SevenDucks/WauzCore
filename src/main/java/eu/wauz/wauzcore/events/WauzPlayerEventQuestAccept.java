package eu.wauz.wauzcore.events;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzQuest;
import net.md_5.bungee.api.ChatColor;

/**
 * An event that lets a player accept a new quest.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventQuestAccept implements WauzPlayerEvent {
	
	/**
	 * The quest to accept.
	 */
	private WauzQuest quest;
	
	/**
	 * The slot this quest will be saved to.
	 */
	private String questSlot;
	
	/**
	 * Tne chat display name of the quest giver.
	 */
	private String questGiver;
	
	/**
	 * Creates an event to accept the given quest.
	 * 
	 * @param quest The quest to accept.
	 * @param questSlot The slot this quest will be saved to.
	 * @param questGiver Tne chat display name of the quest giver.
	 */
	public WauzPlayerEventQuestAccept(WauzQuest quest, String questSlot, String questGiver) {
		this.quest = quest;
		this.questSlot = questSlot;
		this.questGiver = questGiver;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see PlayerQuestConfigurator#setQuestPhase(Player, String, int)
	 * @see PlayerConfigurator#setCharacterQuestSlot(Player, String, String)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			String questName = quest.getQuestName();
			PlayerQuestConfigurator.setQuestPhase(player, questName, 1);
			PlayerConfigurator.setCharacterQuestSlot(player, questSlot, questName);
			
			player.sendMessage(ChatColor.GREEN + "You accepted the " + quest.getType() + "-quest [" + quest.getDisplayName() + "]");
			new WauzPlayerEventCitizenTalk(questGiver, quest.getPhaseDialog(1)).execute(player);
			WauzPlayerScoreboard.scheduleScoreboard(player);
			player.closeInventory();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while accepting the quest!");
			player.closeInventory();
			return false;
		}
	}

}
