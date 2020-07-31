package eu.wauz.wauzcore.players.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A template for a class, that can be chosen by a player.
 * 
 * @author Wauzmons
 */
public abstract class BaseClass implements WauzPlayerClass {
	
	/**
	 * A map of the classes' subclasses / masteries, indexed by name.
	 */
	private Map<String, WauzPlayerSubclass> subclassMap = new HashMap<>();
	
	/**
	 * An ordered list of all the classes' subclasses.
	 */
	private List<WauzPlayerSubclass> subclasses = new ArrayList<>();
	
	/**
	 * Registers a subclass of the class.
	 * 
	 * @param subclass The subclass to register.
	 */
	protected void registerSubclass(WauzPlayerSubclass subclass) {
		subclasses.add(subclass);
		subclassMap.put(subclass.getSubclassName(), subclass);
	}
	
	/**
	 * @return All subclasses of the class.
	 */
	@Override
	public List<WauzPlayerSubclass> getSubclasses() {
		return new ArrayList<>(subclasses);
	}

	/**
	 * @param subclass The name of a subclass.
	 * 
	 * @return The subclass with that name or null.
	 */
	@Override
	public WauzPlayerSubclass getSubclass(String subclass) {
		return subclassMap.get(subclass);
	}

}
