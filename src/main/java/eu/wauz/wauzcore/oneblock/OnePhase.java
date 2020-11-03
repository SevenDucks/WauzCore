package eu.wauz.wauzcore.oneblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.OneBlockConfigurator;
import eu.wauz.wauzcore.system.util.Chance;

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
	 * A list of all one-block phases.
	 */
	private static List<OnePhase> phases = new ArrayList<>();
	
	/**
	 * Initializes all one-block phases from the config and fills the internal phase map.
	 * 
	 * @see OneBlockConfigurator#getAllPhaseKeys()
	 */
	public static void init() {
		for(String phaseKey : OneBlockConfigurator.getAllPhaseKeys()) {
			phases.add(new OnePhase(phaseKey));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + phases.size() + " OneBlock Phases!");
	}
	
	/**
	 * Gets the requested one-block phase.
	 * If the phase is bigger than the phase count, the first phase is returned.
	 * 
	 * @param phase The number of the phase.
	 * 
	 * @return The requested phase.
	 */
	public static OnePhase get(int phase) {
		if(phase > phases.size()) {
			phase = 1;
		}
		return phases.get(phase - 1);
	}
	
	/**
	 * @return The count of all one-block phases.
	 */
	public static int count() {
		return phases.size();
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
	private Map<OneChestType, OneChest> chests;
	
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
		
		chests = new HashMap<>();
		for(OneChestType chestType : OneChestType.values()) {
			chests.put(chestType, new OneChest(this, chestType));
		}
	}
	
	/**
	 * Gets the requested one-block phase level.
	 * If the level is bigger than the level count, the first level is returned.
	 * 
	 * @param level The number of the level.
	 * 
	 * @return The requested level.
	 */
	public OnePhaseLevel getLevel(int level) {
		if(level > phaseLevels.size()) {
			level = 1;
		}
		return phaseLevels.get(level - 1);
	}
	
	/**
	 * Spawns a mob at the given location, depending on the phase's chances.
	 * 
	 * @param location The mob spawn location.
	 * 
	 * @return If a mob was spawned.
	 */
	public boolean tryToSpawnMob(Location location) {
		if(!Chance.percent(mobSpawnChance)) {
			return false;
		}
		List<EntityType> types = Chance.percent(mobHostileChance) ? hostileMobs : passiveMobs;
		EntityType type = types.get(Chance.randomInt(types.size()));
		location.getWorld().spawnEntity(location, type);
		return true;
	}
	
	/**
	 * @return The count of all one-block phase levels.
	 */
	public int levelCount() {
		return phaseLevels.size();
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
