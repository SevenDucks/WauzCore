package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerConfigurationUtils;

public class PlayerQuestConfigurator extends PlayerConfigurationUtils {
	
// General Parameters
	
	public static int getQuestPhase(Player player, String quest) {
		return playerQuestConfigGetInt(player, quest, "phase");
	}
	
	public static void setQuestPhase(Player player, String quest, int phase) {
		playerQuestConfigSet(player, quest, "phase", phase);
	}
	
	public static int getQuestCompletions(Player player, String quest) {
		return playerQuestConfigGetInt(player, quest, "completions");
	}
	
	public static void addQuestCompletions(Player player, String quest) {
		playerQuestConfigSet(player, quest, "completions", getQuestCompletions(player, quest) + 1);
		PlayerConfigurator.addCharacterCompletedQuests(player);
	}
	
	public static boolean isQuestCompleted(Player player, String quest) {
		return getQuestCompletions(player, quest) > 0;
	}
	
	public static long getQuestCooldown(Player player, String quest) {
		return playerQuestConfigGetLong(player, quest, "cooldown");
	}
	
	public static void setQuestCooldown(Player player, String quest) {
		playerQuestConfigSet(player, quest, "cooldown", System.currentTimeMillis());
	}

}
