package eu.wauz.wauzcore.system.quests;

/**
 * A slot to hold the accepted quests of a player.
 * 
 * @author Wauzmons
 * 
 * @see WauzQuest
 */
public enum QuestSlot {
	
	/**
	 * The slot for the main quest.
	 */
	MAIN(QuestType.MAIN, "quest.running.main"),
	
	/**
	 * The primary slot for campaign quests.
	 */
	CAMPAIGN1(QuestType.CAMPAIGN, "quest.running.campaign1"),
	
	/**
	 * The secondary slot for campaign quests. 
	 */
	CAMPAIGN2(QuestType.CAMPAIGN, "quest.running.campaign2"),
	
	/**
	 * The first slot for repeatable quests.
	 */
	DAILY1(QuestType.DAILY, "quest.running.daily1"),
	
	/**
	 * The second slot for repeatable quests. 
	 */
	DAILY2(QuestType.DAILY, "quest.running.daily2"),
	
	/**
	 * The third slot for repeatable quests. 
	 */
	DAILY3(QuestType.DAILY, "quest.running.daily3");
	
	/**
	 * The type of the quest slot.
	 */
	private final String questType;
	
	/**
	 * The name of the key in the config files.
	 */
	private final String configKey;
	
	/**
	 * Creates a new quest slot with given type and key.
	 * 
	 * @param questType The type of the quest slot.
	 * @param configKey The name of the key in the config files.
	 */
	private QuestSlot(String questType, String configKey) {
		this.questType = questType;
		this.configKey = configKey;
	}

	/**
	 * @return The type of the quest slot.
	 */
	public String getQuestType() {
		return questType;
	}

	/**
	 * @return The name of the key in the config files.
	 */
	public String getConfigKey() {
		return configKey;
	}

}
