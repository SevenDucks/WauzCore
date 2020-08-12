package eu.wauz.wauzcore.system.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.RankConfigurator;
import eu.wauz.wauzcore.mobs.MobSpawn;

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
	 * The name of the world template of the instance.
	 */
	private String instanceWorldTemplateName;
	
	/**
	 * The list of mythic mobs to spawn in the instance.
	 */
	private List<MobSpawn> mobs = new ArrayList<>();
	
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
		
		for(String citizenNpcString : InstanceConfigurator.getCitizenSpawns(instanceName)) {
			
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

}
