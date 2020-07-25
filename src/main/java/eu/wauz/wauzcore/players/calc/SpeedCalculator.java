package eu.wauz.wauzcore.players.calc;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerPetsConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.util.WauzMode;

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
	 * 
	 * @see PlayerPetsConfigurator#getCharacterPetDexterity(Player, int)
	 */
	public static void resetWalkSpeed(Player player) {
		float bonusSpeed = 0;
		if(WauzMode.isMMORPG(player) && WauzPlayerDataPool.isCharacterSelected(player)) {
			int petSlot = PlayerPetsConfigurator.getCharacterActivePetSlot(player);
			if(petSlot >= 0) {
				bonusSpeed += PlayerPetsConfigurator.getCharacterPetDexterity(player, petSlot) * 0.02f;
			}
		}
		
		player.setWalkSpeed(0.2f + bonusSpeed);
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

}
