package eu.wauz.wauzcore.system.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.RankConfigurator;
import eu.wauz.wauzcore.mobs.MobSpawn;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizen;
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
		setMaxPlayers(InstanceConfigurator.getMaximumPlayers(instanceName));
		setMaxDeaths(InstanceConfigurator.getMaximumDeaths(instanceName));
		setDisplayTitle(InstanceConfigurator.getDisplayTitle(instanceName));
		setDisplaySubtitle(InstanceConfigurator.getDisplaySubtitle(instanceName));
		setSoundtrackName(InstanceConfigurator.getSoundtrack(instanceName));
		
		for(String mobSpawnString : InstanceConfigurator.getMobSpawns(instanceName)) {
			String[] mobSpawnParams = mobSpawnString.split(" ");
			MobSpawn mobSpawn = new MobSpawn(mobSpawnParams[0]);
			float x = Float.parseFloat(mobSpawnParams[1]);
			float y = Float.parseFloat(mobSpawnParams[2]);
			float z = Float.parseFloat(mobSpawnParams[3]);
			mobSpawn.setCoordinates(x, y, z);
			mobs.add(mobSpawn);
		}
		
		for(String citizenSpawnString : InstanceConfigurator.getCitizenSpawns(instanceName)) {
			String[] citizenSpawnParams = citizenSpawnString.split(" ");
			WauzCitizen citizen = WauzCitizen.getUnassignedCitizen(citizenSpawnParams[0]);
			WauzInstanceCitizen citizenSpawn = new WauzInstanceCitizen(citizen);
			float x = Float.parseFloat(citizenSpawnParams[1]);
			float y = Float.parseFloat(citizenSpawnParams[2]);
			float z = Float.parseFloat(citizenSpawnParams[3]);
			float yaw = Float.parseFloat(citizenSpawnParams[4]);
			float pitch = Float.parseFloat(citizenSpawnParams[5]);
			citizenSpawn.setCoordinates(x, y, z, yaw, pitch);
			citizens.add(citizenSpawn);
		}
		
		if(type.equals(WauzInstanceType.KEYS)) {
			setKeyIds(InstanceConfigurator.getKeyNameList(instanceName));
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
