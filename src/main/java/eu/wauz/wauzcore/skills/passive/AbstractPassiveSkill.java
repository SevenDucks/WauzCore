package eu.wauz.wauzcore.skills.passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

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
	 * Creates an empty instance of this passive skill.
	 */
	public AbstractPassiveSkill() {
		this.exp = -1;
		level = -1;
	}
	
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
	 * Creates a new instance of this passive skill.
	 * 
	 * @param exp The current experience in this skill.
	 * 
	 * @return The created instance.
	 */
	public abstract AbstractPassiveSkill getInstance(long exp);
	
	/**
	 * Gets the name of the passive skill.
	 * 
	 * @return The name of the skill.
	 */
	public abstract String getPassiveName();
	
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
	 * Adds experience to the passive and levels it up, if a new milestone was reached.
	 * 
	 * @param player The player that should see level up messages.
	 * @param earned The amount of experience that has been earned.
	 */
	public void grantExperience(Player player, long earned) {
		exp += earned;
		WauzDebugger.log(player, "You earned " + earned + " " + getPassiveName() + " experience!");
		onExperienceGain(player);
		while(hasReachedMilestone()) {
			level++;
			player.sendMessage(ChatColor.DARK_AQUA + getPassiveName() + " Up! Your skill reached " + level + "!");
			Components.title(player, ChatColor.YELLOW + getPassiveName() + " Up!", "Your skill reached " + level + "!");
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			onLevelUp(player);
		}
	}
	
	/**
	 * Generates a list of lores to display the progress of this skill.
	 * 
	 * @param color The color of the progress bar.
	 * 
	 * @return The list of progress lores.
	 */
	public List<String> getProgressLores(ChatColor color) {
		List<String> lores = new ArrayList<>();
		long progress = getExp();
		String progressString = Formatters.INT.format(progress);
		Long nextMilestone = getNextMilestone();
		if(nextMilestone != null) {
			String nextGoalString = Formatters.INT.format(nextMilestone);
			lores.add(ChatColor.WHITE + "Progress: " + progressString + " / " + nextGoalString);
			lores.add(UnicodeUtils.createProgressBar(progress, nextMilestone, 50, color));
		}
		else {
			lores.add(ChatColor.WHITE + "Progress: " + progressString);
		}
		return lores;
	}
	
	/**
	 * @return The current milestone or 0.
	 */
	public Long getThisMilestone() {
		return level <= 0 ? 0 : getMilestones().get(level - 1);
	}
	
	/**
	 * @return The following milestone or null.
	 */
	public Long getNextMilestone() {
		return level >= getMilestones().size() ? null : getMilestones().get(level);
	}
	
	/**
	 * @return If the following milestone has been reached.
	 */
	protected boolean hasReachedMilestone() {
		Long milestone = getNextMilestone();
		return milestone != null && exp >= milestone;
	}
	
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
	protected void onLevelUp(Player player) {
		// No default implementation.
	}
	
	/**
	 * Method that gets called when skill experience is gained.
	 * 
	 * @param player The player who earned the experience.
	 */
	protected void onExperienceGain(Player player) {
		// No default implementation.
	}

}
