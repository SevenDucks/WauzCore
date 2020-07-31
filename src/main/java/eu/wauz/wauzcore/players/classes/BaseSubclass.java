package eu.wauz.wauzcore.players.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * A template for a subclass / mastery, that belongs to a player class.
 * 
 * @author Wauzmons
 */
public abstract class BaseSubclass implements WauzPlayerSubclass {
	
	/**
	 * A list of all learnables from the mastery.
	 */
	private List<Learnable> learnables = new ArrayList<>();
	
	/**
	 * Registers a learnable for the mastery.
	 * 
	 * @param learnable The learnable to register.
	 */
	protected void registerLearnable(Learnable learnable) {
		learnables.add(learnable);
	}

	/**
	 * @return A list of all learnables from the mastery.
	 */
	@Override
	public List<Learnable> getLearnables() {
		return new ArrayList<>(learnables);
	}
	
	/**
	 * Gets all learned learnables at the given mastery level.
	 * 
	 * @param masteryLevel The level of the mastery.
	 * 
	 * @return The learned learnables.
	 */
	@Override
	public List<Learnable> getLearned(int masteryLevel) {
		List<Learnable> learned = new ArrayList<>();
		for(Learnable learnable : learnables) {
			if(learnable.getLevel() <= masteryLevel) {
				learned.add(learnable);
			}
		}
		return learned;
	}
	
	/**
	 * Determines the next learnable to learn in the mastery.
	 * 
	 * @param masteryLevel The level of the mastery.
	 * 
	 * @return The next learnable to learn.
	 */
	@Override
	public Learnable getNextLearnable(int masteryLevel) {
		for(Learnable learnable : learnables) {
			if(learnable.getLevel() > masteryLevel) {
				return learnable;
			}
		}
		return null;
	}

}
