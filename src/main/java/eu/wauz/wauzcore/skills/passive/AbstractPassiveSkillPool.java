package eu.wauz.wauzcore.skills.passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * This class is used to store all abstract passive skills.
 * 
 * @author Wauzmons
 *
 * @see AbstractPassiveSkill
 */
public class AbstractPassiveSkillPool {
	
	/**
	 * A list of all registered passives.
	 */
	private static List<AbstractPassiveSkill> passives = new ArrayList<>();
	
	/**
	 * Gets a passive by name for the given player.
	 * 
	 * @param player The player to get the passive for.
	 * @param skillName The name of the passive.
	 * 
	 * @return The passive.
	 */
	public static AbstractPassiveSkill getPassive(Player player, String skillName) {
		return WauzPlayerDataPool.getPlayer(player).getSkills().getCachedPassive(skillName);
	}
	
	/**
	 * Gets a list of all registered passives.
	 * 
	 * @return All registered passives.
	 */
	public static List<AbstractPassiveSkill> getPassives() {
		return new ArrayList<>(passives);
	}
	
	/**
	 * Registers a passive.
	 * 
	 * @param passive The passive to register.
	 */
	public static void registerPassive(AbstractPassiveSkill passive) {
		passives.add(passive);
	}

}
