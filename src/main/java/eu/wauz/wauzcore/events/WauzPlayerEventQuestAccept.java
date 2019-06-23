package eu.wauz.wauzcore.events;

import java.util.List;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerQuestConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzQuest;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerEventQuestAccept implements WauzPlayerEvent {
	
	private WauzQuest quest;
	
	private String questSlot;
	
	private String questGiver;
	
	public WauzPlayerEventQuestAccept(WauzQuest quest, String questSlot, String questGiver) {
		this.quest = quest;
		this.questSlot = questSlot;
		this.questGiver = questGiver;
	}

	@Override
	public boolean execute(Player player) {
		try {
			String questName = quest.getQuestName();
			PlayerQuestConfigurator.setQuestPhase(player, questName, 1);
			PlayerConfigurator.setCharacterQuestSlot(player, questSlot, questName);
			
			List<String> lores = quest.getPhaseDialog(1);
			for(String lore : lores) {
				player.sendMessage((questGiver + lore).replaceAll("player", player.getName()));
			}
			
			WauzPlayerScoreboard.scheduleScoreboard(player);
			player.sendMessage(ChatColor.GREEN + "You accepted the " + quest.getType() + "-quest [" + quest.getDisplayName() + "]");
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
