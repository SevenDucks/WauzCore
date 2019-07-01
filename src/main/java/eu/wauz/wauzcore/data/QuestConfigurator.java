package eu.wauz.wauzcore.data;

import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

public class QuestConfigurator extends GlobalConfigurationUtils {
	
// Quest Files
	
	public static List<String> getQuestNameList() {
		return GlobalConfigurationUtils.getQuestNameList();
	}

// General Parameters
	
	public static String getDisplayName(String questName) {
		return questConfigGetString(questName, "name");
	}
	
	public static String getCoordinates(String questName) {
		return questConfigGetString(questName, "coords");
	}
	
	public static String getType(String questName) {
		return questConfigGetString(questName, "type");
	}
	
	public static int getLevel(String questName) {
		return questConfigGetInt(questName, "level");
	}
	
	public static int getPhaseAmount(String questName) {
		return questConfigGetInt(questName, "amount");
	}
	
// Dialog and Lore
	
	public static List<String> getPhaseDialog(String questName, int phase) {
		return questConfigGetStringList(questName, "phases." + phase + ".lores");
	}
	
	public static List<String> getCompletedDialog(String questName) {
		return questConfigGetStringList(questName, "completed");
	}
	
	public static String getUncompletedMessage(String questName, int phase) {
		return questConfigGetString(questName, "phases." + phase + ".uncomplete");
	}
	
// Requirements
	
	public static int getRequirementAmount(String questName, int phase) {
		return questConfigGetInt(questName, "phases." + phase + ".requirements.amount");
	}
	
	public static int getRequirementNeededItemAmount(String questName, int phase, int requirement) {
		return questConfigGetInt(questName, "phases." + phase + ".requirements." + requirement + ".amount");
	}
	
	public static String getRequirementNeededItemName(String questName, int phase, int requirement) {
		return questConfigGetString(questName, "phases." + phase + ".requirements." + requirement + ".item");
	}
	
	public static String getRequirementNeededItemCoordinates(String questName, int phase, int requirement) {
		return questConfigGetString(questName, "phases." + phase + ".requirements." + requirement + ".coords");
	}
	
}
