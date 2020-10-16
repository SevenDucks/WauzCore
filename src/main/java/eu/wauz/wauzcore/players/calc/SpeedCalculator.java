package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.mobs.pets.WauzPetStat;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;

/**
 * Used to calculate the movement and flying speeds of players.
 * 
 * @author Wauzmons
 */
public class SpeedCalculator {
	
	/**
	 * Resets the player's walk speed.
	 * Adds speed bonus from pet's dexterity stat.
	 * 
	 * @param player The player to change the speed of.
	 */
	public static void resetWalkSpeed(Player player) {
		float bonusSpeed = 0;
		int petDex = WauzActivePet.getPetStat(player, WauzPetStat.getPetStat("Dexterity"));
		bonusSpeed += (float) petDex * 0.02f;
		player.setWalkSpeed(0.2f + bonusSpeed);
	}
	
	/**
	 * Temporarily sets the player's walk speed.
	 * 
	 * @param player The player to change the speed of.
	 * @param speed The speed value form 1-10, where 2 is default.
	 */
	public static void setWalkSpeed(Player player, int speed) {
		speed = Math.min(10, Math.max(1, speed));
		player.setWalkSpeed(0.1f * speed);
		player.sendMessage(ChatColor.GREEN + "Set walk speed to " + speed + "!");
	}
	
	/**
	 * Resets the player's fly speed.
	 * Triples the speed, when in flying debug mode.
	 * 
	 * @param player The player to change the speed of.
	 * 
	 * @see WauzDebugger#toggleFlyingDebugMode(Player)
	 */
	public static void resetFlySpeed(Player player) {
		boolean debugSpeed = player.hasPermission(WauzPermission.DEBUG_FLYING.toString());
		player.setFlySpeed(debugSpeed ? 0.3f : 0.1f);
	}
	
	/**
	 * Temporarily sets the player's fly speed.
	 * 
	 * @param player The player to change the speed of.
	 * @param speed The speed value form 1-10, where 2 is default.
	 */
	public static void setFlySpeed(Player player, int speed) {
		speed = Math.min(10, Math.max(1, speed));
		player.setFlySpeed(0.05f * speed);
		player.sendMessage(ChatColor.GREEN + "Set fly speed to " + speed + "!");
	}

}
