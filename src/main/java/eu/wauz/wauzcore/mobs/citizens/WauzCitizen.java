package eu.wauz.wauzcore.mobs.citizens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.CitizenConfigurator;

/**
 * A citizen, generated from a citizen config file.
 * 
 * @author Wauzmons
 * 
 * @see WauzCitizensSpawner
 */
public class WauzCitizen {
	
	/**
	 * A map with lists of citizens, indexed by chunks.
	 */
	private static Map<Chunk, List<WauzCitizen>> chunkCitizensMap = new HashMap<>();
	
	/**
	 * A map of citizens, indexed by name.
	 */
	private static Map<String, WauzCitizen> citizenMap = new HashMap<>();
	
	/**
	 * The radius in chunks, in which citizens should be rendered.
	 */
	private static final int RENDER_RADIUS = 4;
	
	/**
	 * Initializes all citizen configs and fills the internal citizen maps.
	 * 
	 * @see CitizenConfigurator#getCitizenNameList()
	 */
	public static void init() {
		for(String citizenName : CitizenConfigurator.getCitizenNameList()) {
			WauzCitizen citizen = new WauzCitizen(citizenName);
			citizenMap.put(citizenName, citizen);
			
			Chunk chunk = citizen.getLocation().getChunk();
			if(chunkCitizensMap.get(chunk) == null) {
				chunkCitizensMap.put(chunk, new ArrayList<>());
			}
			chunkCitizensMap.get(chunk).add(citizen);
			WauzCitizensSpawner.createNpc(citizen);
		}
	}
	
	/**
	 * Finds all the citzens, the player should be able to see.
	 * 
	 * @param player The player to get the citizens for.
	 * 
	 * @return A list of all citizens, the player should see.
	 */
	public static List<WauzCitizen> getCitizensNearPlayer(Player player) {
		List<WauzCitizen> citizens = new ArrayList<>();
		int chunkX = player.getChunk().getX();
		int chunkZ = player.getChunk().getZ();
		World world = player.getWorld();
		for(int x = chunkX - RENDER_RADIUS; x <= chunkX + RENDER_RADIUS; x++) {
			for(int z = chunkZ - RENDER_RADIUS; z <= chunkZ + RENDER_RADIUS; z++) {
				Chunk chunk = world.getChunkAt(x, z);
				List<WauzCitizen> chunkCitizens = chunkCitizensMap.get(chunk);
				if(chunkCitizens != null) {
					citizens.addAll(chunkCitizens);
				}
			}
		}
		return citizens;
	}
	
	/**
	 * The canonical name of the citizen.
	 */
	private String citizenName;
	
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
		
		interactions = new WauzCitizenInteractions(citizenName);
	}

	/**
	 * @return The canonical name of the citizen.
	 */
	public String getCitizenName() {
		return citizenName;
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
