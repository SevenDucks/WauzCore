package eu.wauz.wauzcore.skills.passive;

import java.util.List;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.mobs.pets.WauzPetBreedingLevel;
import eu.wauz.wauzcore.system.annotations.PassiveSkill;

/**
 * A passive skill, that unlocks pet rarities and abilities, leveled through breeding pets.
 * 
 * @author Wauzmons
 */
@PassiveSkill
public class PassiveBreeding extends AbstractPassiveSkill {
	
	/**
	 * The static name of the passive skill.
	 */
	public static final String PASSIVE_NAME = "Breeding";
	
	/**
	 * The static list of experience milestones.
	 */
	private static final List<Long> MILESTONES = WauzPetBreedingLevel.getExperienceMilestones();
	
	/**
	 * Creates an empty instance of this passive skill.
	 */
	public PassiveBreeding() {
		super();
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public PassiveBreeding(long exp) {
		super(exp);
	}
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 * 
	 * @return The created instance.
	 */
	@Override
	public AbstractPassiveSkill getInstance(long exp) {
		return new PassiveBreeding(exp);
	}
	
	/**
	 * Gets the name of the passive skill.
	 * 
	 * @return The name of the skill.
	 */
	@Override
	public String getPassiveName() {
		return PASSIVE_NAME;
	}

	/**
	 * Gets all experience milestones, marking where new levels are reached.
	 * 
	 * @return A list of experience milestones.
	 */
	@Override
	protected List<Long> getMilestones() {
		return MILESTONES;
	}
	
	/**
	 * Method that gets called when a new milestone has been reached.
	 * 
	 * @param player The player who reached the milestone.
	 */
	@Override
	protected void onLevelUp(Player player) {
		
	}
	
	/**
	 * Gets the breeding level for this skill.
	 * 
	 * @return The breeding level.
	 */
	public WauzPetBreedingLevel getBreedingLevel() {
		return WauzPetBreedingLevel.getBreedingLevel(getLevel());
	}

}
