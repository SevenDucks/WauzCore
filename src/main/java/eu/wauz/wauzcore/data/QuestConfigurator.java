package eu.wauz.wauzcore.data;

import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Shop.yml files.
 * 
 * @author Wauzmons
 */
public class QuestConfigurator extends GlobalConfigurationUtils {
	
// Quest Files
	
	/**
	 * @return The list of all quest names.
	 */
	public static List<String> getQuestNameList() {
		return GlobalConfigurationUtils.getQuestNameList();
	}

// General Parameters
	
	/**
	 * @param questName The name of the quest.
	 * 
	 * @return The display name of the quest.
	 */
	public static String getDisplayName(String questName) {
		return questConfigGetString(questName, "name");
	}
	
	/**
	 * @param questName The name of the quest.
	 * 
	 * @return The coordinates of the quest as string.
	 */
	public static String getCoordinates(String questName) {
		return questConfigGetString(questName, "coords");
	}
	
	/**
	 * @param questName The name of the quest.
	 * 
	 * @return The type of the quest.
	 */
	public static String getType(String questName) {
		return questConfigGetString(questName, "type");
	}
	
	/**
	 * @param questName The name of the quest.
	 * 
	 * @return The level of the quest.
	 */
	public static int getLevel(String questName) {
		return questConfigGetInt(questName, "level");
	}
	
	/**
	 * @param questName The name of the quest.
	 * 
	 * @return The amount of phases of the quest.
	 */
	public static int getPhaseAmount(String questName) {
		return questConfigGetInt(questName, "amount");
	}
	
// Dialog and Lore
	
	/**
	 * @param questName The name of the quest.
	 * @param phase The number of the quest phase.
	 * 
	 * @return The dialog for the quest phase.
	 */
	public static List<String> getPhaseDialog(String questName, int phase) {
		return questConfigGetStringList(questName, "phases." + phase + ".lores");
	}
	
	/**
	 * @param questName The name of the quest.
	 * 
	 * @return The dialog for the quest completion.
	 */
	public static List<String> getCompletedDialog(String questName) {
		return questConfigGetStringList(questName, "completed");
	}
	
	/**
	 * @param questName The name of the quest.
	 * @param phase The number of the quest phase.
	 * 
	 * @return The dialog for the uncompleted quest phase.
	 */
	public static String getUncompletedMessage(String questName, int phase) {
		return questConfigGetString(questName, "phases." + phase + ".uncomplete");
	}
	
// Requirements
	
	/**
	 * @param questName The name of the quest.
	 * @param phase The number of the quest phase.
	 * 
	 * @return The amount of completion requirements of the phase.
	 */
	public static int getRequirementAmount(String questName, int phase) {
		return questConfigGetInt(questName, "phases." + phase + ".requirements.amount");
	}
	
	/**
	 * @param questName The name of the quest.
	 * @param phase The number of the quest phase.
	 * @param requirement The number of the quest phase completion requirement.
	 * 
	 * @return The amount of items needed to fulfill the requirement.
	 */
	public static int getRequirementNeededItemAmount(String questName, int phase, int requirement) {
		return questConfigGetInt(questName, "phases." + phase + ".requirements." + requirement + ".amount");
	}
	
	/**
	 * @param questName The name of the quest.
	 * @param phase The number of the quest phase.
	 * @param requirement The number of the quest phase completion requirement.
	 * 
	 * @return The name of the items needed to fulfill the requirement.
	 */
	public static String getRequirementNeededItemName(String questName, int phase, int requirement) {
		return questConfigGetString(questName, "phases." + phase + ".requirements." + requirement + ".item");
	}
	
	/**
	 * @param questName The name of the quest.
	 * @param phase The number of the quest phase.
	 * @param requirement The number of the quest phase completion requirement.
	 * 
	 * @return The coordinates of the items needed to fulfill the requirement.
	 */
	public static String getRequirementNeededItemCoordinates(String questName, int phase, int requirement) {
		return questConfigGetString(questName, "phases." + phase + ".requirements." + requirement + ".coords");
	}
	
}
