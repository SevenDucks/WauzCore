package eu.wauz.wauzcore.mobs.citizens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.MineSkinFetcher;
import net.jitse.npclib.api.state.NPCSlot;
import net.jitse.npclib.api.state.NPCState;

/**
 * A class to handle spawning / despawning, aswell as rendering of citizen npcs.
 * 
 * @author Wauzmons
 * 
 * @see WauzCitizen
 */
public class WauzCitizenSpawner {
	
	/**
	 * Access to the NPCLib API.
	 */
	private static final NPCLib npcLib = new NPCLib(WauzCore.getInstance());
	
	/**
	 * A map of npcs, indexed by citizen.
	 */
	private static Map<WauzCitizen, NPC> citizenNpcMap = new HashMap<>(); 
	
	/**
	 * A map of citizens, indexed by npc.
	 */
	private static Map<NPC, WauzCitizen> npcCitizenMap = new HashMap<>();
	
	/**
	 * Creates a citizen npc using the npc lib.
	 * The npc won't be visible, till it is shown to a player.
	 * 
	 * @param citizen The citizen, that should be created.
	 * 
	 * @see WauzCitizenSpawner#registerNpc(WauzCitizen, NPC)
	 */
	public static void createNpc(WauzCitizen citizen) {
        MineSkinFetcher.fetchSkinFromIdAsync(citizen.getSkinId(), skin -> {
            NPC npc = npcLib.createNPC(citizen.getNameLines());
            npc.setLocation(citizen.getLocation());
            npc.setSkin(skin);
            if(citizen.isInvisible()) {
            	npc.toggleState(NPCState.INVISIBLE);
            }
            if(citizen.isBurning()) {
            	npc.toggleState(NPCState.ON_FIRE);
            }
            npc.setItem(NPCSlot.MAINHAND, citizen.getMainhandItemStack());
            npc.setItem(NPCSlot.OFFHAND, citizen.getOffhandItemStack());
            npc.setItem(NPCSlot.HELMET, citizen.getHelmetItemStack());
            npc.setItem(NPCSlot.CHESTPLATE, citizen.getChestplateItemStack());
            npc.setItem(NPCSlot.LEGGINGS, citizen.getLeggingsItemStack());
            npc.setItem(NPCSlot.BOOTS, citizen.getBootsItemStack());
            npc.create();
            Bukkit.getScheduler().runTask(WauzCore.getInstance(), () -> registerNpc(citizen, npc));
        });
	}
	
	/**
	 * Registers the given citizen npc, to retrieve it when needed.
	 * 
	 * @param citizen The citizen to register.
	 * @param npc The npc object bound to the citzen.
	 * 
	 * @see WauzCitizenSpawner#getCitizen(NPC)
	 */
	public static void registerNpc(WauzCitizen citizen, NPC npc) {
		citizenNpcMap.put(citizen, npc);
		npcCitizenMap.put(npc, citizen);
	}
	
	/**
	 * Unregisters the given citizen and destroy its npc object.
	 * 
	 * @param citizen The citizen to unregister.
	 */
	public static void unregisterNpc(WauzCitizen citizen) {
		NPC npc = citizenNpcMap.get(citizen);
		citizenNpcMap.remove(citizen);
		npcCitizenMap.remove(npc);
		npc.destroy();
	}
	
	/**
	 * Retrieves a registered citizen from the npc map.
	 * 
	 * @param npc The npc object bound to the citzen.
	 * 
	 * @return The requested citizen.
	 * 
	 * @see WauzCitizenSpawner#registerNpc(WauzCitizen, NPC)
	 */
	public static WauzCitizen getCitizen(NPC npc) {
		return npcCitizenMap.get(npc);
	}
	
	/**
	 * Shows all npcs near the given player to them. 
	 * 
	 * @param player The player to show the npcs to.
	 */
	public static void showNpcsNearPlayer(Player player) {
		List<WauzCitizen> citizens = WauzCitizen.getCitizensNearPlayer(player);
		WauzDebugger.log(player, "Rendered " + citizens.size() + " Citizens");
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
			npc = npc.create();
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
