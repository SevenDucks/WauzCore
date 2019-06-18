package eu.wauz.wauzcore.events;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzQuest;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerEventQuestCancel implements WauzPlayerEvent {
	
	private String questName;
	
	public WauzPlayerEventQuestCancel(String questName) {
		this.questName = questName;
	}

	@Override
	public boolean execute(Player player) {
		try {
			WauzQuest quest = WauzQuest.getQuest(questName);
			
			String questSlot = null;
			String type = quest.getType();
			
			String slotm = PlayerConfigurator.getCharacterRunningMainQuest(player);
			String cmpn1 = PlayerConfigurator.getCharacterRunningCampaignQuest1(player);
			String cmpn2 = PlayerConfigurator.getCharacterRunningCampaignQuest2(player);
			String slot1 = PlayerConfigurator.getCharacterRunningDailyQuest1(player);
			String slot2 = PlayerConfigurator.getCharacterRunningDailyQuest2(player);
			String slot3 = PlayerConfigurator.getCharacterRunningDailyQuest3(player);
			
			if(type.equals("main")) {
				if(slotm.equals(questName))
					questSlot = "quest.running.main";
			}
			
			else if(type.equals("campaign")) {
				if(cmpn1.equals(questName))
					questSlot = "quest.running.campaign1";
				else if(cmpn2.equals(questName))
					questSlot = "quest.running.campaign2";
			}
			
			else if(type.equals("daily")) {
				if(slot1.equals(questName))
					questSlot = "quest.running.daily1";
				else if(slot2.equals(questName))
					questSlot = "quest.running.daily2";
				else if(slot3.equals(questName))
					questSlot = "quest.running.daily3";
			}
			
			PlayerConfigurator.setCharacterQuestSlot(player, questSlot, "none");
			PlayerConfigurator.setCharacterQuestPhase(player, questName, 0);
			
			WauzPlayerScoreboard.scheduleScoreboard(player);
			player.sendMessage(ChatColor.DARK_PURPLE + "[" + quest.getDisplayName() + "] was canceled!");
			player.closeInventory();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while canceling your quest!");
			player.closeInventory();
			return false;
		}
	}

}
