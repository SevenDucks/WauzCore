package eu.wauz.wauzcore.system.quests;

import eu.wauz.wauzcore.data.QuestConfigurator;

/**
 * A completion requirement of a phase of a quest.
 * 
 * @author Wauzmons
 */
class WauzQuestRequirement {
	
	/**
	 * The amount of the needed items, to fulfill the requirement.
	 */
	int neededItemAmount;
	
	/**
	 * The name of the needed items, to fulfill the requirement.
	 */
	String neededItemName;
	
	/**
	 * The coordinates of the needed items, to fulfill the requirement.
	 */
	String neededItemCoordinates;
	
	/**
	 * Constructs a quest phase completion requirement, based on the quest file name in the /WauzCore/QuestData folder.
	 * 
	 * @param questName The canonical name of the quest.
	 * @param phase The number of the phase.
	 * @param requirement The number of the requirement.
	 */
	public WauzQuestRequirement(String questName, int phase, int requirement) {
		neededItemAmount = QuestConfigurator.getRequirementNeededItemAmount(questName, phase, requirement);
		neededItemName = QuestConfigurator.getRequirementNeededItemName(questName, phase, requirement);
		neededItemCoordinates = QuestConfigurator.getRequirementNeededItemCoordinates(questName, phase, requirement);
	}

	/**
	 * @return The amount of the needed items, to fulfill the requirement.
	 */
	public int getNeededItemAmount() {
		return neededItemAmount;
	}

	/**
	 * @return The name of the needed items, to fulfill the requirement.
	 */
	public String getNeededItemName() {
		return neededItemName;
	}

	/**
	 * @return The coordinates of the needed items, to fulfill the requirement.
	 */
	public String getNeededItemCoordinates() {
		return neededItemCoordinates;
	}
	
}