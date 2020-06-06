package eu.wauz.wauzcore.oneblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.OneBlockConfigurator;

/**
 * A phase of the one-block gamemode.
 * 
 * @author Wauzmons
 * 
 * @see OnePhaseLevel
 * @see OneChest
 */
public class OnePhase {
	
	/**
	 * A map of phases, indexed by their unique key.
	 */
	private static Map<String, OnePhase> phaseMap = new HashMap<>();
	
	/**
	 * Initializes all one-block phases from the config and fills the internal phase map.
	 * 
	 * @see OneBlockConfigurator#getAllPhaseKeys()
	 */
	public static void init() {
		for(String phaseKey : OneBlockConfigurator.getAllPhaseKeys()) {
			phaseMap.put(phaseKey, new OnePhase(phaseKey));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + phaseMap.size() + " OneBlock Phases!");
	}
	
	/**
	 * The key of the phase.
	 */
	private String phaseKey;
	
	/**
	 * The display name of the phase.
	 */
	private String phaseName;
	
	/**
	 * The chance that a mob spawns, when a block is destroyed.
	 */
	private double mobSpawnChance;
	
	/**
	 * The chance of it being hostile, when a mob spawns.
	 */
	private double mobHostileChance;
	
	/**
	 * The list of passive mobs, spawning in the phase.
	 */
	private List<EntityType> passiveMobs;
	
	/**
	 * The list of hostile mobs, spawning in the phase.
	 */
	private List<EntityType> hostileMobs;
	
	/**
	 * The levels of the phase.
	 */
	private List<OnePhaseLevel> phaseLevels;
	
	/**
	 * The chests that can spawn in this phase, indexed by type.
	 */
	private Map<OneChestType, OneChest> chests = new HashMap<>();
	
	/**
	 * Constructs a phase, based on the one-block file in the /WauzCore folder.
	 * 
	 * @param phaseKey The key of the phase.
	 */
	public OnePhase(String phaseKey) {
		this.phaseKey = phaseKey;
		phaseName = OneBlockConfigurator.getPhaseName(phaseKey);
		mobSpawnChance = OneBlockConfigurator.getMobSpawnChance(phaseKey);
		mobHostileChance = OneBlockConfigurator.getMobHostileChance(phaseKey);
		passiveMobs = OneBlockConfigurator.getMobs(phaseKey, false);
		hostileMobs = OneBlockConfigurator.getMobs(phaseKey, true);
		
		phaseLevels = new ArrayList<>();
		for(String levelKey : OneBlockConfigurator.getPhaseLevelKeys(phaseKey)) {
			phaseLevels.add(new OnePhaseLevel(this, levelKey));
		}
		
		for(OneChestType chestType : OneChestType.values()) {
			chests.put(chestType, new OneChest(this, chestType));
		}
	}

	/**
	 * @return The key of the phase.
	 */
	public String getPhaseKey() {
		return phaseKey;
	}

	/**
	 * @return The display name of the phase.
	 */
	public String getPhaseName() {
		return phaseName;
	}

	/**
	 * @return The chance that a mob spawns, when a block is destroyed.
	 */
	public double getMobSpawnChance() {
		return mobSpawnChance;
	}

	/**
	 * @return The chance of it being hostile, when a mob spawns.
	 */
	public double getMobHostileChance() {
		return mobHostileChance;
	}

	/**
	 * @return The list of passive mobs, spawning in the phase.
	 */
	public List<EntityType> getPassiveMobs() {
		return passiveMobs;
	}

	/**
	 * @return The list of hostile mobs, spawning in the phase.
	 */
	public List<EntityType> getHostileMobs() {
		return hostileMobs;
	}

	/**
	 * @return The levels of the phase.
	 */
	public List<OnePhaseLevel> getPhaseLevels() {
		return phaseLevels;
	}

	/**
	 * @return The chests that can spawn in this phase, indexed by type.
	 */
	public Map<OneChestType, OneChest> getChests() {
		return chests;
	}

}
