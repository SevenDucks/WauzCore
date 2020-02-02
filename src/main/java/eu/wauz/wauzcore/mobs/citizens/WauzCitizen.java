package eu.wauz.wauzcore.mobs.citizens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;

import eu.wauz.wauzcore.data.CitizenConfigurator;

/**
 * A citizen, generated from a citizen config file.
 * 
 * @author Wauzmons
 * 
 * @see CitizensSpawner
 */
public class WauzCitizen {
	
	/**
	 * A map with lists of citizens, indexed by chunks.
	 */
	private static Map<Chunk, List<WauzCitizen>> chunkCitizensMap;
	
	/**
	 * A map of citizens, indexed by name.
	 */
	private static Map<String, WauzCitizen> citizenMap = new HashMap<>();
	
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
		}
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
	 * The location of the citizen.
	 */
	private Location location;
	
	/**
	 * The id of the citizen's skin.
	 */
	private int skinId;
	
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
	 * @return The location of the citizen.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return The id of the citizen's skin.
	 */
	public int getSkinId() {
		return skinId;
	}

}
