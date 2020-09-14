package eu.wauz.wauzcore.system.quests;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.QuestConfigurator;

/**
 * A phase of a quest.
 * 
 * @author Wauzmons
 */
public class WauzQuestPhase {
	
	/**
	 * The list of messages in the phase dialog.
	 */
	private List<String> phaseDialog;
	
	/**
	 * The list of messages in the uncompleted phase dialog.
	 */
	private String uncompleteMessage;
	
	/**
	 * The type of the completion requirements of the phase.
	 */
	private String requirementType;
	
	/**
	 * The amount of requirements to complete the phase.
	 */
	private int requirementAmount;
	
	/**
	 * The list of questphase completion requirements.
	 */
	private List<WauzQuestRequirement> requirements = new ArrayList<>();
	
	/**
	 * Constructs a quest phase, based on the quest file name in the /WauzCore/QuestData folder.
	 * 
	 * @param questName The canonical name of the quest.
	 * @param phase The number of the phase.
	 */
	public WauzQuestPhase(String questName, int phase) {
		phaseDialog = QuestConfigurator.getPhaseDialog(questName, phase);
		uncompleteMessage = QuestConfigurator.getUncompletedMessage(questName, phase);
		requirementType = QuestConfigurator.getRequirementType(questName, phase);
		requirementAmount = QuestConfigurator.getRequirementAmount(questName, phase);
		
		for(int requirement = 1; requirement <= requirementAmount; requirement++) {
			requirements.add(new WauzQuestRequirement(questName, phase, requirement));
		}
	}

	/**
	 * @return The list of messages in the phase dialog.
	 */
	public List<String> getPhaseDialog() {
		return phaseDialog;
	}

	/**
	 * @return The list of messages in the uncompleted phase dialog.
	 */
	public String getUncompleteMessage() {
		return uncompleteMessage;
	}

	/**
	 * @return The type of the completion requirements of the phase.
	 */
	public String getRequirementType() {
		return requirementType;
	}

	/**
	 * @return The amount of requirements to complete the phase.
	 */
	public int getRequirementAmount() {
		return requirementAmount;
	}
	
	/**
	 * @param requirement The number of the requirement.
	 * 
	 * @return The requested requirement.
	 */
	public WauzQuestRequirement getRequirement(int requirement) {
		return requirements.get(requirement - 1);
	}
	
}