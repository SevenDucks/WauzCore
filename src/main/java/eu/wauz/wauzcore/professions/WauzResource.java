package eu.wauz.wauzcore.professions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.ResourceConfigurator;
import eu.wauz.wauzcore.system.util.ChunkKeyMap;

/**
 * A gatherable resource, generated from a resource config file.
 * 
 * @author Wauzmons
 */
public class WauzResource {
	
	/**
	 * A map with lists of resource spawns, indexed by chunk keys.
	 */
	private static ChunkKeyMap<List<WauzResourceSpawn>> chunkResourcesMap = new ChunkKeyMap<>();
	
	/**
	 * A map with resource spawns, indexed by block.
	 */
	private static Map<Block, WauzResourceSpawn> blockResourceMap = new HashMap<>();
	
	/**
	 * The number of all different registred resources.
	 */
	private static int resourceCount;
	
	/**
	 * Initializes all resource configs and fills the internal resource maps.
	 * 
	 * @see ResourceConfigurator#getResourceNameList()
	 * @see WauzResource#addToChunkMap(WauzResource, List)
	 */
	public static void init() {
		for(String resourceName : ResourceConfigurator.getResourceNameList()) {
			WauzResource resource = new WauzResource(resourceName);
			addToChunkMap(resource, ResourceConfigurator.getResourceLocations(resourceName));
			resourceCount++;
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + resourceCount + " Resources!");
	}
	
	/**
	 * Adds a resource to the chunk map, so it can spawn in related chunks.
	 * 
	 * @param resource The resource to add.
	 * @param locations The spawn locations of the resource.
	 */
	public static void addToChunkMap(WauzResource resource, List<Location> locations) {
		for(Location location : locations) {
			Chunk chunk = location.getChunk();
			if(chunkResourcesMap.get(chunk) == null) {
				chunkResourcesMap.put(chunk, new ArrayList<>());
			}
			WauzResourceSpawn resourceSpawn = new WauzResourceSpawn(resource, location);
			chunkResourcesMap.get(chunk).add(resourceSpawn);
			blockResourceMap.put(location.getBlock(), resourceSpawn);
		}
	}
	
	/**
	 * Highlights all the resources, near the given player.
	 * 
	 * @param player The player to highlight the resources for.
	 */
	public static void highlightResourcesNearPlayer(Player player) {
		for(List<WauzResourceSpawn> resourceSpawns : chunkResourcesMap.getInRadius(player.getChunk(), 1)) {
			for(WauzResourceSpawn resourceSpawn : resourceSpawns) {
				resourceSpawn.tryToHighlightResource(player);
			}
		}
	}
	
	/**
	 * Lets the player interact with the resource block, if it is valid.
	 * 
	 * @param player The player interacting with the resource.
	 * @param block The block the resource is located at.
	 * 
	 * @return If the interaction was successful.
	 */
	public static boolean tryToInteractWithResource(Player player, Block block) {
		WauzResourceSpawn resourceSpawn = blockResourceMap.get(block);
		if(resourceSpawn == null) {
			return false;
		}
		switch (resourceSpawn.getResource().getType()) {
		case CONTAINER:
			resourceSpawn.tryToCollectResource(player);
			break;
		case NODE:
			resourceSpawn.tryToDamageResource(player);
			break;
		default:
			break;
		}
		return true;
	}
	
	/**
	 * The canonical name of the resource.
	 */
	private String resourceName;
	
	/**
	 * The type of the resource.
	 */
	private WauzResourceType type;
	
	/**
	 * The drop table of the resource.
	 */
	private String dropTable;
	
	/**
	 * The respawn minutes of the resource.
	 */
	private int respawnMins;
	
	/**
	 * The type of the resource node.
	 */
	private WauzResourceNodeType nodeType;
	
	/**
	 * The display name of the resource node.
	 */
	private String nodeName;
	
	/**
	 * The tier of the resource node.
	 */
	private int nodeTier;
	
	/**
	 * The maximum health of the resource node.
	 */
	private int nodeHealth;
	
	/**
	 * Constructs a resource, based on the resource file name in the /WauzCore/ResourceData folder.
	 * 
	 * @param resourceName The canonical name of the resource.
	 */
	public WauzResource(String resourceName) {
		this.resourceName = resourceName;
		
		type = ResourceConfigurator.getResourceType(resourceName);
		dropTable = ResourceConfigurator.getResourceDropTable(resourceName);
		respawnMins = ResourceConfigurator.getResourceRespawnMinutes(resourceName);
		
		if(type.equals(WauzResourceType.NODE)) {
			nodeType = ResourceConfigurator.getNodeType(resourceName);
			nodeName = ResourceConfigurator.getNodeName(resourceName);
			nodeTier = ResourceConfigurator.getNodeTier(resourceName);
			nodeHealth = ResourceConfigurator.getNodeHealth(resourceName);
		}
	}

	/**
	 * @return The canonical name of the resource.
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * @return The type of the resource.
	 */
	public WauzResourceType getType() {
		return type;
	}
	
	/**
	 * @return The drop table of the resource.
	 */
	public String getDropTable() {
		return dropTable;
	}

	/**
	 * @return The respawn minutes of the resource.
	 */
	public int getRespawnMins() {
		return respawnMins;
	}
	
	/**
	 * @return The type of the resource node.
	 */
	public WauzResourceNodeType getNodeType() {
		return nodeType;
	}

	/**
	 * @return The display name of the resource node.
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @return The tier of the resource node.
	 */
	public int getNodeTier() {
		return nodeTier;
	}

	/**
	 * @return The maximum health of the resource node.
	 */
	public int getNodeHealth() {
		return nodeHealth;
	}

}
