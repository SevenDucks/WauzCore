package eu.wauz.wauzcore.mobs.towers;

import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.SkillUtils.TotemRunnable;

/**
 * A defense tower to help players to defend themselves from enemies.
 * 
 * @author Wauzmons
 * 
 * @see WauzTowers
 */
public interface DefenseTower extends TotemRunnable {
	
	/**
	 * @return The name of the tower.
	 */
	public String getTowerName();
	
	/**
	 * @return The item stack to use as the tower's head.
	 */
	public ItemStack getHeadItemStack();
	
	/**
	 * @return The item stack to use as the tower's body.
	 */
	public ItemStack getBodyItemStack();
	
	/**
	 * @return The server ticks between each execution of the tower's effect.
	 */
	public int getInterval();
	
}
