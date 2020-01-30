package eu.wauz.wauzcore.mobs.citizens;

import java.util.List;

import org.bukkit.Location;

import eu.wauz.wauzcore.WauzCore;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.MineSkinFetcher;

/**
 * A class to handle spawning / despawning, aswell as rendering of citizen npcs.
 * Events you may want to use are NPCShowEvent, NPCHideEvent and NPCInteractEvent.
 * 
 * @author Wauzmons
 */
public class CitizensSpawner {
	
	/**
	 * Access to the NPCLib API.
	 */
	private static final NPCLib npcLib = new NPCLib(WauzCore.getInstance());
	
	/**
	 * Spawns a citizen npc using the npc lib.
	 * The npc won't be visible, till it is shown to a player.
	 * 
	 * @param nameLines The lines of text above the npc's head.
	 * @param spawnLocation The location where the npc should be spawned.
	 * @param skinId The identifier of a skin from <a href="https://mineskin.org">mineskin.org</a>
	 */
	public static void spawnNpc(List<String> nameLines, Location spawnLocation, int skinId) {
        MineSkinFetcher.fetchSkinFromIdAsync(skinId, skin -> {
            NPC npc = npcLib.createNPC(nameLines);
            npc.setLocation(spawnLocation);
            npc.setSkin(skin);
//            npc.setItem(slot, item);
//            npc.toggleState(state);
            npc.create();
//            ids.add(npc.getId());
//            // The SkinFetcher fetches the skin async, you can only show the npc to the player sync.
//            Bukkit.getScheduler().runTask(WauzCore.getInstance(), () -> npc.show(event.getPlayer()));
        });
	}
	
//	private void destroyNpc(NPC npc) {
//		npc.destroy();
//	}
//	
//	private void showNpc(NPC npc, Player player) {
//		npc.show(player);
//	}
//	
//	private void hideNpc(NPC npc, Player player) {
//		npc.hide(player);
//	}

	/**
	 * @return Access to the NPCLib API.
	 */
	public static NPCLib getNpclib() {
		return npcLib;
	} 

}
