package eu.wauz.wauzcore.mobs.citizens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.util.Chance;
import net.jitse.npclib.api.NPC;

/**
 * A cache of citizens, used for repeating citizen actions.
 * 
 * @author Wauzmons
 * 
 * @see WauzCitizenSpawner
 */
public class WauzCitizenCache {
	
	/**
	 * A map of players viewing citizens.
	 */
	private static Map<NPC, Set<Player>> citizenViewersMap = new HashMap<>();
	
	/**
	 * Updates the look directions of all cached citizens.
	 * Has a one in 12 chance per citizen to let them say a random message. 
	 * 
	 * @see WauzCitizenCache#showRandomMessage(WauzCitizen, List)
	 */
	public static void updateCitizenLookDirections() {
		for(NPC npc : citizenViewersMap.keySet()) {
			List<Player> nearbyPlayers = new ArrayList<>(npc.getLocation().getNearbyPlayers(4));
			if(!nearbyPlayers.isEmpty()) {
				if(Chance.oneIn(12)) {
					showRandomMessage(WauzCitizenSpawner.getCitizen(npc), nearbyPlayers);
				}
				npc.lookAt(nearbyPlayers.get(Chance.randomInt(nearbyPlayers.size())).getLocation());
			}
		}
	}
	
	/**
	 * Shows a random citizen message to the given players, if possible.
	 * 
	 * @param citizen The citizen to send the message.
	 * @param player The players who should receive the message.
	 */
	public static void showRandomMessage(WauzCitizen citizen, List<Player> players) {
		if(citizen == null) {
			return;
		}
		String randomMessage = citizen.getRandomMessage();
		if(randomMessage == null) {
			return;
		}
		String msg = ChatColor.WHITE + "[" + ChatColor.YELLOW + citizen.getDisplayName() + ChatColor.WHITE + " (" +
				ChatColor.AQUA  + "NPC" + ChatColor.WHITE + ")] " +
				ChatColor.GRAY + randomMessage;
		
		for(Player player : players) {
			if(WauzPlayerDataPool.getPlayer(player).getSelections().getActiveConversations() == 0) {
				player.sendMessage(msg);
			}
		}
	}
	
	/**
	 * Marks the given citizen as show for a specific player.
	 * 
	 * @param npc The citizen npc to set as shown in the cache.
	 * @param player The player viewing the citizen.
	 */
	public static void markAsShown(NPC npc, Player player) {
		Set<Player> viewers = citizenViewersMap.get(npc);
		if(viewers == null) {
			viewers = new HashSet<>();
			citizenViewersMap.put(npc, viewers);
		}
		viewers.add(player);
	}
	
	/**
	 * Marks the given citizen as show for a specific player.
	 * 
	 * @param npc The citizen npc to set as hidden in the cache.
	 * @param player  The player not viewing the citizen anymore.
	 */
	public static void markAsHidden(NPC npc, Player player) {
		Set<Player> viewers = citizenViewersMap.get(npc);
		if(viewers != null) {
			viewers.remove(player);
			if(viewers.isEmpty()) {
				citizenViewersMap.remove(npc);
			}
		}
	}

}
