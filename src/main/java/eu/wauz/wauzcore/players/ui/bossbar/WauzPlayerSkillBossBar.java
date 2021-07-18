package eu.wauz.wauzcore.players.ui.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * An UI class to show name and progress of a passive skill to the player.
 * 
 * @author Wauzmons
 */
public class WauzPlayerSkillBossBar extends WauzPlayerBossBar {
	
	/**
	 * The passive skill, this bar belongs to.
	 */
	private AbstractPassiveSkill passive;
	
	/**
	 * The seconds that passed since the bar was first shown;
	 */
	private int passedSeconds = 0;
	
	/**
	 * Creates a boss bar for the given passive skill.
	 * Also schedules a task to check if the passive and assigned players are still valid. 
	 * 
	 * @param passive The passive skill, this bar belongs to.
	 * 
	 * @see WauzPlayerBossBar#doPlayerChecks()
	 */
	public WauzPlayerSkillBossBar(AbstractPassiveSkill passive) {
		this.passive = passive;
		
		Long thisMilestone = passive.getThisMilestone();
		Long nextMilestone = passive.getNextMilestone();
		String progressString = ChatColor.YELLOW + getName();
		
		if(nextMilestone != null) {
			long current = passive.getExp() - thisMilestone;
			long goal = nextMilestone - thisMilestone;
			
			progressString += " " + ChatColor.GOLD + passive.getLevel();
			String currentHealth = ChatColor.AQUA + Formatters.INT.format(current);
			String maximumHealth = Formatters.INT.format(goal) + " " + UnicodeUtils.ICON_HEART;
			progressString += " " + ChatColor.GRAY + "[ " + currentHealth + " / " + maximumHealth + ChatColor.GRAY + " ]";
			progressString += " " + ChatColor.GOLD + (passive.getLevel() + 1);
			bossBar = Bukkit.createBossBar(progressString, BarColor.GREEN, BarStyle.SEGMENTED_6);
			bossBar.setProgress(current / goal);
		}
		else {
			progressString += " " + ChatColor.GOLD + "MAX";
			String currentValue = ChatColor.AQUA + Formatters.INT.format(passive.getExp());
			progressString += " " + ChatColor.GRAY + "[ " + currentValue + ChatColor.GRAY + " ]";
			bossBar = Bukkit.createBossBar(progressString, BarColor.GREEN, BarStyle.SEGMENTED_6);
			bossBar.setProgress(1);
		}
		
		doPlayerChecks();
	}
	
	/**
	 * Schedules a task to check if the object and assigned players are still valid.
	 * If not, they get removed, else the task is scheduled again for the next second.
	 * 
	 * @see WauzPlayerBossBar#destroy()
	 */
	@Override
	protected void doPlayerChecks() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        @Override
			public void run() {
	        	try {
	        		passedSeconds++;
        			for(Player player : bossBar.getPlayers()) {
        				if(passedSeconds >= 15 || !isPlayerValid(player)) {
        					destroy();
        					return;
        				}
        			}
        			doPlayerChecks();
	        	}
	        	catch (Exception e) {
	        		e.printStackTrace();
	        		destroy();
	        	}
	        }
	        
		}, 20);
	}
	
	/**
	 * @return The name of the object, this bar belongs to.
	 */
	@Override
	protected String getName() {
		return passive.getPassiveName();
	}
	
	/**
	 * @return The health of the object, this bar belongs to.
	 */
	@Override
	protected double getHealth() {
		return 100;
	}

}
