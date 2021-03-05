package eu.wauz.wauzcore.players.effects;

/**
 * The source of a temporary status effect on a player.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerEffect
 */
public enum WauzPlayerEffectSource {
	
	/**
	 * Effect caused by items.
	 */
	ITEM("Item"),
	
	/**
	 * Effect caused by skills.
	 */
	SKILL("Skill"),
	
	/**
	 * Effect caused by pets, minions or totems.
	 */
	SUMMON("Summon");
	
	/**
	 * The name of the effect source.
	 */
	private String name;
	
	/**
	 * Creates a new effect source with given name.
	 * 
	 * @param name The name of the effect source.
	 */
	WauzPlayerEffectSource(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the effect source in a title friendly format.
	 * 
	 * @return The name of the effect source.
	 */
	@Override
	public String toString() {
		return name;
	}

}
