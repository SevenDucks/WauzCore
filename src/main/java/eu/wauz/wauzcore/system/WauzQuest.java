package eu.wauz.wauzcore.system;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.QuestConfigurator;

public class WauzQuest {
	
	private static Map<Integer, List<WauzQuest>> levelQuestsMap = new HashMap<>();
	
	private static Map<String, WauzQuest> questMap = new HashMap<>();
	
	public static void init() {
		for(int level = 1; level <= WauzCore.MAX_PLAYER_LEVEL; level++) {
			levelQuestsMap.put(level, new ArrayList<>());
		}
		
		for(String questName : QuestConfigurator.getQuestNameList()) {
			WauzQuest quest = new WauzQuest(questName);
			levelQuestsMap.get(quest.getLevel()).add(quest);
			questMap.put(questName, quest);
		}
	}
	
	public static List<WauzQuest> getQuestsForLevel(int level) {
		return levelQuestsMap.get(level);
	}
	
	public static WauzQuest getQuest(String questName) {
		return questMap.get(questName);
	}
	
	public static int getQuestCount() {
		return questMap.size();
	}
	
	private String questName;
	
	private String displayName;
	
	private String coordinates;
	
	private String type;
	
	private int level;
	
	private int phaseAmount;
	
	private List<String> completedDialog;
	
	private List<Phase> phases = new ArrayList<>();
	
	public WauzQuest(String questName) {
		this.questName = questName;
		
		displayName = QuestConfigurator.getDisplayName(questName);
		coordinates = QuestConfigurator.getCoordinates(questName);
		type = QuestConfigurator.getType(questName);
		level = QuestConfigurator.getLevel(questName);
		phaseAmount = QuestConfigurator.getPhaseAmount(questName);
		completedDialog = QuestConfigurator.getCompletedDialog(questName);
		
		for(int phase = 1; phase <= phaseAmount; phase++) {
			phases.add(new Phase(questName, phase));
		}
	}

	public String getQuestName() {
		return questName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public String getType() {
		return type;
	}

	public int getLevel() {
		return level;
	}

	public int getPhaseAmount() {
		return phaseAmount;
	}

	public List<String> getCompletedDialog() {
		return completedDialog;
	}
	
	public Point2D getQuestPoint() {
		String[] splitCoordinated = coordinates.split(" ");
		double x = Double.parseDouble(splitCoordinated[0]);
		double z = Double.parseDouble(splitCoordinated[2]);
		return new Point2D.Double(x, z);
	}
	
	private Phase getPhase(int phase) {
		return phases.get(phase - 1);
	}
	
	public List<String> getPhaseDialog(int phase) {
		return getPhase(phase).getPhaseDialog();
	}
	
	public String getUncompletedMessage(int phase) {
		return getPhase(phase).getUncompleteMessage();
	}
	
	public int getRequirementAmount(int phase) {
		return getPhase(phase).getRequirementAmount();
	}
	
	public int getRequirementNeededItemAmount(int phase, int requirement) {
		return getPhase(phase).getRequirement(requirement).getNeededItemAmount();
	}
	
	public String getRequirementNeededItemName(int phase, int requirement) {
		return getPhase(phase).getRequirement(requirement).getNeededItemName();
	}
	
	public String getRequirementNeededItemCoordinates(int phase, int requirement) {
		return getPhase(phase).getRequirement(requirement).getNeededItemCoordinates();
	}
	
	private static class Phase {
		
		private List<String> phaseDialog;
		
		private String uncompleteMessage;
		
		private int requirementAmount;
		
		private List<Requirement> requirements = new ArrayList<>();
		
		public Phase(String questName, int phase) {
			phaseDialog = QuestConfigurator.getPhaseDialog(questName, phase);
			uncompleteMessage = QuestConfigurator.getUncompletedMessage(questName, phase);
			requirementAmount = QuestConfigurator.getRequirementAmount(questName, phase);
			
			for(int requirement = 1; requirement <= requirementAmount; requirement++) {
				requirements.add(new Requirement(questName, phase, requirement));
			}
		}

		public List<String> getPhaseDialog() {
			return phaseDialog;
		}

		public String getUncompleteMessage() {
			return uncompleteMessage;
		}

		public int getRequirementAmount() {
			return requirementAmount;
		}
		
		public Requirement getRequirement(int requirement) {
			return requirements.get(requirement - 1);
		}
		
	}
	
	private static class Requirement {
		
		int neededItemAmount;
		
		String neededItemName;
		
		String neededItemCoordinates;
		
		public Requirement(String questName, int phase, int requirement) {
			neededItemAmount = QuestConfigurator.getRequirementNeededItemAmount(questName, phase, requirement);
			neededItemName = QuestConfigurator.getRequirementNeededItemName(questName, phase, requirement);
			neededItemCoordinates = QuestConfigurator.getRequirementNeededItemCoordinates(questName, phase, requirement);
		}

		public int getNeededItemAmount() {
			return neededItemAmount;
		}

		public String getNeededItemName() {
			return neededItemName;
		}

		public String getNeededItemCoordinates() {
			return neededItemCoordinates;
		}
		
	}

}
