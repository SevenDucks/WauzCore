package eu.wauz.wauzcore.skills.passive;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzDebugger;

/**
 * An abstract passive skill, that can be leveled.
 * 
 * @author Wauzmons
 */
public abstract class AbstractPassiveSkill {
	
	/**
	 * The current level of this skill.
	 */
	private int level;
	
	/**
	 * The current experience in this skill.
	 */
	private long exp;
	
	/**
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 */
	public AbstractPassiveSkill(long exp) {
		this.exp = exp;
		level = 0;
		for(long milestone : getMilestones()) {
			if(exp >= milestone) {
				level++;
			}
			else {
				break;
			}
		}
	}
	
	/**
	 * Adds experience to the passive and levels it up, if a new milestone was reached.
	 * 
	 * @param player The player that should see level up messages.
	 * @param earned The amount of experience that has been earned.
	 */
	public void grantExperience(Player player, long earned) {
		exp += earned;
		WauzDebugger.log(player, "You earned " + earned + " " + getPassiveName() + " experience!");
		while(hasReachedMilestone()) {
			level++;
			player.sendTitle(ChatColor.YELLOW + getPassiveName() + " Passive Up!", "Your skill reached " + level + "!", 10, 70, 20);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			onLevelUp(player);
		}
	}
	
	/**
	 * @return The current level of this skill.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * @return The current experience in this skill.
	 */
	public long getExp() {
		return exp;
	}
	
	/**
	 * @return If the following milestone has been reached.
	 */
	protected boolean hasReachedMilestone() {
		Long milestone = getNextMilestone();
		return milestone != null && exp >= milestone;
	}
	
	/**
	 * @return The following milestone.
	 */
	protected Long getNextMilestone() {
		return level >= getMilestones().size() ? null : getMilestones().get(level);
	}
	
	/**
	 * Gets the name of the passive skill.
	 * 
	 * @return The name of the skill.
	 */
	protected abstract String getPassiveName();
	
	/**
	 * Gets all experience milestones, marking where new levels are reached.
	 * 
	 * @return A list of experience milestones.
	 */
	protected abstract List<Long> getMilestones();
	
	/**
	 * Method that gets called when a new milestone has been reached.
	 * 
	 * @param player The player who reached the milestone.
	 */
	protected abstract void onLevelUp(Player player);

}
