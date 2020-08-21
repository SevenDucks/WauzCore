package eu.wauz.wauzcore.mobs.bestiary;

/**
 * The category of a section in the bestiary.
 * 
 * @author Wauzmons
 */
public enum BestiaryCategory {
	
	/**
	 * The critter category for entries like farm animals or birds.
	 */
	CRITTER("Critter"),
	
	/**
	 * The beast category for entries like bears or giant spiders.
	 */
	BEAST("Beast"),
	
	/**
	 * The mythical category for entries like ogres or ents.
	 */
	MYTHICAL("Mythical"),
	
	/**
	 * The construct category for entries like golems or elementals.
	 */
	CONSTRUCT("Construct"),
	
	/**
	 * The human category for entries like thieves or cultists.
	 */
	HUMAN("Human"),
	
	/**
	 * The undead category for entries like zombies or skeletons.
	 */
	UNDEAD("Undead"),
	
	/**
	 * The demon category for entries like imps or archdemons.
	 */
	DEMON("Demon");
	
	/**
	 * The name of the bestiary category.
	 */
	private String name;
	
	/**
	 * Creates a new bestiary category with given name.
	 * 
	 * @param name The name of the bestiary category.
	 */
	BestiaryCategory(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the bestiary category in a description friendly format.
	 * 
	 * @return The name of the bestiary category.
	 */
	@Override
	public String toString() {
		return name;
	}

}
