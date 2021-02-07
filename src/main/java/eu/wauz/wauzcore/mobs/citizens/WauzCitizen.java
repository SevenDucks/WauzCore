package eu.wauz.wauzcore.mobs.citizens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.CitizenConfigurator;
import eu.wauz.wauzcore.system.util.ChunkKeyMap;

/**
 * A citizen, generated from a citizen config file.
 * 
 * @author Wauzmons
 * 
 * @see WauzCitizenSpawner
 */
public class WauzCitizen {
	
	/**
	 * A map with lists of citizens, indexed by chunk keys.
	 */
	private static ChunkKeyMap<List<WauzCitizen>> chunkCitizensMap = new ChunkKeyMap<>();
	
	/**
	 * A map of citizens that aren't assigned to a world, indexed by name.
	 */
	private static Map<String, WauzCitizen> unassignedCitizenMap = new HashMap<>();
	
	/**
	 * The number of all registred citizens.
	 */
	private static int citizenCount;
	
	/**
	 * Initializes all citizen configs and fills the internal citizen maps.
	 * 
	 * @see CitizenConfigurator#getCitizenNameList()
	 * @see WauzCitizen#addToChunkMap(WauzCitizen, Chunk)
	 */
	public static void init() {
		for(String citizenName : CitizenConfigurator.getCitizenNameList()) {
			WauzCitizen citizen = new WauzCitizen(citizenName);
			
			Location location = citizen.getLocation();
			if(location != null) {
				Chunk chunk = location.getChunk();
				addToChunkMap(citizen, chunk);
			}
			else {
				unassignedCitizenMap.put(citizenName, citizen);
			}
			citizenCount++;
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + citizenCount + " Citizens!");
	}
	
	/**
	 * Adds a citizen to the chunk map, so they can spawn in that chunk.
	 * 
	 * @param citizen The citizen to add.
	 * @param chunk The chunk of the citizen.
	 * 
	 * @see WauzCitizenSpawner#createNpc(WauzCitizen)
	 */
	public static void addToChunkMap(WauzCitizen citizen, Chunk chunk) {
		if(chunkCitizensMap.get(chunk) == null) {
			chunkCitizensMap.put(chunk, new ArrayList<>());
		}
		chunkCitizensMap.get(chunk).add(citizen);
		WauzCitizenSpawner.createNpc(citizen);
	}
	
	/**
	 * Removes a citizen from the chunk map, so they can no longer spawn in that chunk.
	 * 
	 * @param citizen The citizen to remove.
	 * @param chunk The chunk of the citizen.
	 * 
	 * @see WauzCitizenSpawner#unregisterNpc(WauzCitizen)
	 */
	public static void removeFromChunkMap(WauzCitizen citizen, Chunk chunk) {
		List<WauzCitizen> chunkCitizens = chunkCitizensMap.get(chunk);
		chunkCitizens.remove(citizen);
		if(chunkCitizens.isEmpty()) {
			chunkCitizensMap.remove(chunk);
		}
		WauzCitizenSpawner.unregisterNpc(citizen);
	}
	
	/**
	 * Gets a citizen that isn't assigned to a world, with the given name.
	 * 
	 * @param citizenName The name of the citizen.
	 * 
	 * @return The found citizen or null.
	 */
	public static WauzCitizen getUnassignedCitizen(String citizenName) {
		return unassignedCitizenMap.get(citizenName);
	}
	
	/**
	 * Finds all the citzens, near the given player.
	 * 
	 * @param player The player to get the citizens for.
	 * @param radius The radius in chunks, in which citizens should be found.
	 * 
	 * @return A list of all citizens, the player should see.
	 */
	public static List<WauzCitizen> getCitizensNearPlayer(Player player, int radius) {
		List<WauzCitizen> citizens = new ArrayList<>();
		for(List<WauzCitizen> chunkCitizens : chunkCitizensMap.getInRadius(player.getChunk(), radius)) {
			citizens.addAll(chunkCitizens);
		}
		return citizens;
	}
	
	/**
	 * The canonical name of the citizen.
	 */
	private String citizenName;
	
	/**
	 * The name of the citizen, as shown in chat.
	 */
	private String displayName;
	
	/**
	 * The lines of text above the citizen's head.
	 */
	private List<String> nameLines;
	
	/**
	 * The location where the npc should be spawned.
	 */
	private Location location;
	
	/**
	 * The identifier of a skin from <a href="https://mineskin.org">mineskin.org</a>
	 */
	private int skinId;
	
	/**
	 * If the citizen is invisible.
	 */
	private boolean invisible;
	
	/**
	 * If the citizen is burning.
	 */
	private boolean burning;
	
	/**
	 * The equipped mainhand item.
	 */
	private ItemStack mainhandItemStack;
	
	/**
	 * The equipped offhand item.
	 */
	private ItemStack offhandItemStack;
	
	/**
	 * The equipped helmet item.
	 */
	private ItemStack helmetItemStack;
	
	/**
	 * The equipped chestplate item.
	 */
	private ItemStack chestplateItemStack;
	
	/**
	 * The equipped leggings item.
	 */
	private ItemStack leggingsItemStack;
	
	/**
	 * The equipped boots item.
	 */
	private ItemStack bootsItemStack;
	
	/**
	 * The interaction options of the citizen.
	 */
	private WauzCitizenInteractions interactions;
	
	/**
	 * Constructs a citizen, based on the citizen file name in the /WauzCore/CitizenData folder.
	 * 
	 * @param citizenName The canonical name of the citizen.
	 */
	public WauzCitizen(String citizenName) {
		this.citizenName = citizenName;
		
		displayName = CitizenConfigurator.getDisplayName(citizenName);
		nameLines = CitizenConfigurator.getNameLines(citizenName);
		location = CitizenConfigurator.getLocation(citizenName);
		skinId = CitizenConfigurator.getSkinId(citizenName);
		invisible = CitizenConfigurator.isInvisible(citizenName);
		burning = CitizenConfigurator.isBurning(citizenName);
		
		mainhandItemStack = CitizenConfigurator.getEquippedMainhandItem(citizenName);
		offhandItemStack = CitizenConfigurator.getEquippedOffhandItem(citizenName);
		helmetItemStack = CitizenConfigurator.getEquippedHelmetItem(citizenName);
		chestplateItemStack = CitizenConfigurator.getEquippedChestplateItem(citizenName);
		leggingsItemStack = CitizenConfigurator.getEquippedLeggingsItem(citizenName);
		bootsItemStack = CitizenConfigurator.getEquippedBootsItem(citizenName);
		
		interactions = new WauzCitizenInteractions(citizenName, displayName);
	}
	
	/**
	 * Constructs a citizen, that is an exact copy of another citizen.
	 * 
	 * @param citizen The citizen to clone.
	 */
	public WauzCitizen(WauzCitizen citizen) {
		citizenName = citizen.getCitizenName();
		
		displayName = citizen.getDisplayName();
		nameLines = citizen.getNameLines();
		location = citizen.getLocation();
		skinId = citizen.getSkinId();
		invisible = citizen.isInvisible();
		burning = citizen.isBurning();
		
		mainhandItemStack = citizen.getMainhandItemStack();
		offhandItemStack = citizen.getOffhandItemStack();
		helmetItemStack = citizen.getHelmetItemStack();
		chestplateItemStack = citizen.getChestplateItemStack();
		leggingsItemStack = citizen.getLeggingsItemStack();
		bootsItemStack = citizen.getBootsItemStack();
		
		interactions = citizen.getInteractions();
	}

	/**
	 * @return The canonical name of the citizen.
	 */
	public String getCitizenName() {
		return citizenName;
	}
	
	/**
	 * @return The name of the citizen, as shown in chat.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return The lines of text above the citizen's head.
	 */
	public List<String> getNameLines() {
		return nameLines;
	}

	/**
	 * @return The location where the npc should be spawned.
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * @param location The new location where the npc should be spawned.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return The identifier of a skin from <a href="https://mineskin.org">mineskin.org</a>
	 */
	public int getSkinId() {
		return skinId;
	}

	/**
	 * @return If the citizen is invisible.
	 */
	public boolean isInvisible() {
		return invisible;
	}

	/**
	 * @return If the citizen is burning.
	 */
	public boolean isBurning() {
		return burning;
	}

	/**
	 * @return The equipped mainhand item.
	 */
	public ItemStack getMainhandItemStack() {
		return mainhandItemStack;
	}

	/**
	 * @return The equipped offhand item.
	 */
	public ItemStack getOffhandItemStack() {
		return offhandItemStack;
	}

	/**
	 * @return The equipped helmet item.
	 */
	public ItemStack getHelmetItemStack() {
		return helmetItemStack;
	}

	/**
	 * @return The equipped chestplate item.
	 */
	public ItemStack getChestplateItemStack() {
		return chestplateItemStack;
	}

	/**
	 * @return The equipped leggings item.
	 */
	public ItemStack getLeggingsItemStack() {
		return leggingsItemStack;
	}

	/**
	 * @return The equipped boots item.
	 */
	public ItemStack getBootsItemStack() {
		return bootsItemStack;
	}

	/**
	 * @return The interaction options of the citizen.
	 */
	public WauzCitizenInteractions getInteractions() {
		return interactions;
	}

}
