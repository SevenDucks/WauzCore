package eu.wauz.wauzcore.worlds;

import org.bukkit.WorldCreator;

/**
 * Represents various types of options that may be used to create an iris world.
 * 
 * @author Wauzmons
 */
public class IrisWorldCreator extends WorldCreator {

	/**
	 * Creates an iris world creator for the given world name.
	 * 
	 * @param name The name of the world that will be created.
	 */
	public IrisWorldCreator(String name) {
		super(name);
		generator("Iris:overworld");
	}

}
