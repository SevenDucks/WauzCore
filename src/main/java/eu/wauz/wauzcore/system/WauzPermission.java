package eu.wauz.wauzcore.system;

/**
 * Permissions that can be granted to players.
 * 
 * @author Wauzmons
 */
public enum WauzPermission {
	
	/**
	 * Gives access to system commands. Default: op.
	 */
	SYSTEM("wauz.system"),
	
	/**
	 * Permission assigned to new players. Default: true.
	 */
	NORMAL("wauz.normal"),
	
	/**
	 * Shows the personalized debug log in chat. Default: false.
	 */
	DEBUG("wauz.debug"),
	
	/**
	 * Infinite mana and 1 second skill cooldowns. Default: false.
	 */
	DEBUG_MAGIC("wauz.debug.magic"),
	
	/**
	 * Unlock all recipes, craftable without cost. Default: false.
	 */
	DEBUG_CRAFTING("wauz.debug.crafting"),
	
	/**
	 * Enables building in all areas. Default: false.
	 */
	DEBUG_BUILDING("wauz.debug.building"),
	
	/**
	 * Allows to fly very fast in all modes.
	 */
	DEBUG_FLYING("wauz.debug.flying"),
	
	/**
	 * Increases damage output drastically.
	 */
	DEBUG_ATTACK("wauz.debug.attack"),
	
	/**
	 * Reduces damage taken to zero.
	 */
	DEBUG_DEFENSE("wauz.debug.defense"),
	
	/**
	 * Enables world edit commands 1/4.
	 */
	FAWE_WORLDEDIT_1("worldedit.*"),
	
	/**
	 * Enables world edit commands 2/4.
	 */
	FAWE_WORLDEDIT_2("fawe.admin"),
	
	/**
	 * Enables world edit commands 3/4.
	 */
	FAWE_WORLDEDIT_3("fawe.plotsquared"),
	
	/**
	 * Enables world edit commands 4/4.
	 */
	FAWE_WORLDEDIT_4("fawe.permpack.basic"),
	
	/**
	 * Enables voxel sniper commands.
	 */
	FAWE_VOXELSNIPER("fawe.voxelbrush");
	
	/**
	 * The name of the permission.
	 */
	private String name;
	
	/**
	 * Creates a new permission with given name.
	 * 
	 * @param name The name of the permission.
	 */
	private WauzPermission(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the permission in a plugin friendly format.
	 * 
	 * @return The name of the permission.
	 */
	@Override
	public String toString() {
		return name;
	}

}
