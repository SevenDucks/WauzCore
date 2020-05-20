package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.quests.WauzQuest;

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
	 * Creates an event to accept the given quest.
	 * 
	 * @param quest The quest to accept.
	 * @param questSlot The slot this quest will be saved to.
	 */
	public WauzPlayerEventQuestAccept(WauzQuest quest, String questSlot) {
		this.quest = quest;
		this.questSlot = questSlot;
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
			new WauzPlayerEventCitizenTalk(quest.getQuestGiver(), quest.getPhaseDialog(1)).execute(player);
			WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
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
