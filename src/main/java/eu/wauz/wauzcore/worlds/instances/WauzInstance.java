package eu.wauz.wauzcore.worlds.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.RankConfigurator;
import eu.wauz.wauzcore.mobs.MobSpawn;
import eu.wauz.wauzcore.mobs.citizens.WauzInstanceCitizen;

/**
 * An instance template, generated from an insctance config file.
 * 
 * @author Wauzmons
 */
public class WauzInstance extends WauzBaseInstance {
	
	/**
	 * A map of instances, indexed by name.
	 */
	private static Map<String, WauzInstance> instanceMap = new HashMap<>();
	
	/**
	 * Initializes all instances from the configs and fills the internal instance map.
	 * 
	 * @see RankConfigurator#getAllRankKeys()
	 */
	public static void init() {
		for(String instanceName : InstanceConfigurator.getInstanceNameList()) {
			instanceMap.put(instanceName, new WauzInstance(instanceName));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + instanceMap.size() + " Instances!");
	}
	
	/**
	 * @param instanceName An instance name.
	 * 
	 * @return The instance with that name.
	 */
	public static WauzInstance getInstance(String instanceName) {
		return instanceMap.get(instanceName);
	}
	
	/**
	 * Gets the list of all instance names.
	 * 
	 * @return The list of all instance names.
	 */
	public static List<String> getAllInstanceNames() {
		return new ArrayList<>(instanceMap.keySet());
	}
	
	/**
	 * The name of the world template of the instance.
	 */
	private String instanceWorldTemplateName;
	
	/**
	 * The list of mythic mobs to spawn in the instance.
	 */
	private List<MobSpawn> mobs = new ArrayList<>();
	
	/**
	 * The list of citizen npcs to spawn in the instance.
	 */
	private List<WauzInstanceCitizen> citizens = new ArrayList<>();
	
	/**
	 * Constructs an instance template, based on the instance file in the /WauzCore/InstanceData folder.
	 * 
	 * @param instanceName The name of the instance.
	 */
	public WauzInstance(String instanceName) {
		setInstanceName(instanceName);
		this.instanceWorldTemplateName = InstanceConfigurator.getWorldTemplateName(instanceName);
		WauzInstanceType type = InstanceConfigurator.getInstanceType(instanceName);
		setType(type);
		List<Float> spawnCoords = new ArrayList<>();
		String spawnCoordsString = InstanceConfigurator.getPlayerSpawnCoords(instanceName);
		if(StringUtils.isNotBlank(spawnCoordsString)) {
			for(String spawnCoord : spawnCoordsString.split(" ")) {
				spawnCoords.add(Float.parseFloat(spawnCoord));
			}
		}
		setSpawnCoords(spawnCoords);
		setMaxPlayers(InstanceConfigurator.getMaximumPlayers(instanceName));
		setMaxDeaths(InstanceConfigurator.getMaximumDeaths(instanceName));
		setDisplayTitle(InstanceConfigurator.getDisplayTitle(instanceName));
		setDisplaySubtitle(InstanceConfigurator.getDisplaySubtitle(instanceName));
		setSoundtrackName(InstanceConfigurator.getSoundtrack(instanceName));
		
		for(String mobString : InstanceConfigurator.getMobSpawns(instanceName)) {
			mobs.add(new MobSpawn(instanceName, mobString));
		}
		for(String citizenString : InstanceConfigurator.getCitizenSpawns(instanceName)) {
			citizens.add(new WauzInstanceCitizen(citizenString));
		}
		
		if(type.equals(WauzInstanceType.KEYS)) {
			setKeyIds(InstanceConfigurator.getKeyNameList(instanceName));
		}
		else if(type.equals(WauzInstanceType.ARENA)) {
			setMobArena(new InstanceMobArena(instanceName));
		}
	}
	
	/**
	 * @return The name of the world template of the instance.
	 */
	public String getInstanceWorldTemplateName() {
		return instanceWorldTemplateName;
	}
	
	/**
	 * @return The list of mythic mobs to spawn in the instance.
	 */
	public List<MobSpawn> getMobs() {
		return mobs;
	}
	
	/**
	 * @return The list of citizen npcs to spawn in the instance.
	 */
	public List<WauzInstanceCitizen> getCitizens() {
		return citizens;
	}

}
