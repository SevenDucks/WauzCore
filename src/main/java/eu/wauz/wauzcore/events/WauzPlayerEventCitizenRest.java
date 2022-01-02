package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An event that lets a player get healed and fed by a citizen.
 * 
 * @author Jassuuu
 */
public class WauzPlayerEventCitizenRest implements WauzPlayerEvent {
	
	/**
	 * The name of the citizen to interact with.
	 */
	private String citizenName;
	
	/**
	 * Creates an event to get healed by the given citizen.
	 * 
	 * @param citizenName The name of the citizen to interact with.
	 */
	public WauzPlayerEventCitizenRest(String citizenName) {
		this.citizenName = citizenName;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see DamageCalculator#setHealth(Player, int)
	 */
	@Override
	public boolean execute(Player player) {
		try {	
			if(WauzMode.isMMORPG(player)) {
				DamageCalculator.setHealth(player, WauzPlayerDataPool.getPlayer(player).getStats().getMaxHealth());
			}
			else {
				player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			}
			player.setFoodLevel(20);
			player.setSaturation(20);
			player.sendMessage(ChatColor.GREEN + "You are now fully rested!");
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while interacting with " + citizenName + "!");
			player.closeInventory();
			return false;
		}
	}

}
