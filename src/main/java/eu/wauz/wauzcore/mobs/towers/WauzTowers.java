package eu.wauz.wauzcore.mobs.towers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.SkillUtils;

/**
 * A class for handling the construction of defense towers.
 * 
 * @author Wauzmons
 *
 * @see DefenseTower
 */
public class WauzTowers {
	
	/**
	 * A map of all towers, indexed by tower name.
	 */
	private static Map<String, DefenseTower> towerMap = new HashMap<>();
	
	/**
	 * Gets a tower for the given name from the map.
	 * 
	 * @param towerName The name of the tower.
	 * 
	 * @return The tower or null, if not found.
	 */
	public static DefenseTower getTower(String towerName) {
		return towerMap.get(towerName);
	}
	
	/**
	 * Registers a tower.
	 * 
	 * @param tower The tower to register.
	 */
	public static void registerTower(DefenseTower tower) {
		towerMap.put(tower.getTowerName(), tower);
	}
	
	/**
	 * Lets a player try to construct a defense tower.
	 * The totem stays active for 7,5 minutes.
	 * 
	 * @param player The player who tries to construct a tower.
	 * @param towerName The name of the tower to construct.
	 * 
	 * @return If the construction was successful.
	 * 
	 * @see SkillUtils#
	 */
	public static boolean tryToConstruct(Player player, String towerName) {
		DefenseTower tower = towerMap.get(towerName);
		if(tower == null) {
			player.sendMessage(ChatColor.RED + "This tower blueprint is invalid!");
			return false;
		}
		
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1.5f);
		ItemStack headItemStack = tower.getHeadItemStack();
		ItemStack bodyItemStack = tower.getBodyItemStack();
		int interval = tower.getInterval();
		int ticks = 9000 / interval;
		SkillUtils.spawnTower(player, headItemStack, bodyItemStack, tower, ticks, interval);
		return true;
	}

}
