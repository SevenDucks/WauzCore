package eu.wauz.wauzcore.skills.execution;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

/**
 * A skill, that can be executed by a player.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkillExecutor
 */
public interface WauzPlayerSkill { 
	
	/**
	 * @return The id of the skill.
	 */
	public String getSkillId();
	
	/**
	 * @return The description of the skill.
	 */
	default public String getSkillDescription() {
		return
			"[" + ChatColor.RED + getSkillDescriptionType().toString() + ChatColor.WHITE + "] " +
			"[" + ChatColor.GRAY + getSkillDescriptionEffect() + ChatColor.WHITE + "]";
	}
	
	/**
	 * @return The type of the skill shown in the default description.
	 */
	public WauzPlayerSkillType getSkillDescriptionType();
	
	/**
	 * @return The effect of the skill shown in the default description.
	 */
	public String getSkillDescriptionEffect();
	
	/**
	 * @return The condition stats (mana, cooldown etc.) of the skill.
	 */
	default public String getSkillStats() {
		return
			"[" + ChatColor.GRAY + "CD: " + ChatColor.YELLOW + getCooldownSeconds() + ChatColor.GRAY + "s" + ChatColor.WHITE + "] " +
			"[" + ChatColor.GRAY + "MP: " + ChatColor.BLUE + getManaCost() + ChatColor.WHITE + "]";
	}
	
	/**
	 * @return The cooldown of the skill in seconds.
	 */
	public int getCooldownSeconds();
	
	/**
	 * @return The mana cost of the skill.
	 */
	public int getManaCost();
	
	/**
	 * Executes the skill for the given player.
	 * 
	 * @param player The player who executes the skill.
	 * @param weapon The weapon that player uses for it.
	 * 
	 * @return If the skill hit something.
	 */
	public boolean executeSkill(Player player, ItemStack weapon);

}
