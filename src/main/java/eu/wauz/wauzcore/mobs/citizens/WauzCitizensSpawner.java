package eu.wauz.wauzcore.mobs.citizens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.MineSkinFetcher;
import net.jitse.npclib.api.state.NPCState;

/**
 * A class to handle spawning / despawning, aswell as rendering of citizen npcs.
 * Events you may want to use are NPCShowEvent, NPCHideEvent and NPCInteractEvent.
 * 
 * @author Wauzmons
 * 
 * @see WauzCitizen
 */
public class WauzCitizensSpawner {
	
	/**
	 * Access to the NPCLib API.
	 */
	private static final NPCLib npcLib = new NPCLib(WauzCore.getInstance());
	
	/**
	 * A map of npcs, indexed by citizen.
	 */
	private static Map<WauzCitizen, NPC> citizenNpcMap = new HashMap<>(); 
	
	/**
	 * Creates a citizen npc using the npc lib.
	 * The npc won't be visible, till it is shown to a player.
	 * 
	 * @param citizen The citizen, that should be created.
	 */
	public static void createNpc(WauzCitizen citizen) {
        MineSkinFetcher.fetchSkinFromIdAsync(citizen.getSkinId(), skin -> {
            NPC npc = npcLib.createNPC(citizen.getNameLines());
            npc.setLocation(citizen.getLocation());
            npc.setSkin(skin);
//            npc.setItem(slot, item);
            if(citizen.isCrouched()) {
            	npc.toggleState(NPCState.CROUCHED);
            }
            if(citizen.isInvisible()) {
            	npc.toggleState(NPCState.INVISIBLE);
            }
            if(citizen.isBurning()) {
            	npc.toggleState(NPCState.ON_FIRE);
            }
            npc.create();
            Bukkit.getScheduler().runTask(WauzCore.getInstance(), () -> citizenNpcMap.put(citizen, npc));
        });
	}
	/**
	 * Shows all npcs near the given player to them. 
	 * 
	 * @param player The player to show the npcs to.
	 */
	public static void showNpcsNearPlayer(Player player) {
		List<WauzCitizen> citizens = WauzCitizen.getCitizensNearPlayer(player);
		for(WauzCitizen citizen : citizens) {
			NPC npc = citizenNpcMap.get(citizen);
			showNpc(npc, player);
		}
			
	}
	
	/**
	 * Destroys the given npc, i.e. remove it from the registry.
	 * 
	 * @param npc The npc to destroy.
	 */
	public static void destroyNpc(NPC npc) {
		npc.destroy();
	}
	
	/**
	 * Show the npc to the given player.
	 * 
	 * @param npc The npc to show.
	 * @param player The player to show the npc to.
	 */
	public static void showNpc(NPC npc, Player player) {
		if(!npc.isShown(player)) {
			npc.show(player);
		}
	}
	
	/**
	 * Hides the npc from the given player.
	 * 
	 * @param npc The npc to hide.
	 * @param player The player to hide the npc from.
	 */
	public static void hideNpc(NPC npc, Player player) {
		if(npc.isShown(player)) {
			npc.hide(player);
		}
	}

	/**
	 * @return Access to the NPCLib API.
	 */
	public static NPCLib getNpclib() {
		return npcLib;
	} 

}
