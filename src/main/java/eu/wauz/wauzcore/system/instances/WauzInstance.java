package eu.wauz.wauzcore.system.instances;

import java.util.HashMap;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.RankConfigurator;

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
	 * Constructs an instance template, based on the instance file in the /WauzCore/InstanceData folder.
	 * 
	 * @param instanceName The name of the instance.
	 */
	public WauzInstance(String instanceName) {
		setInstanceName(instanceName);
		this.instanceWorldTemplateName = InstanceConfigurator.getWorldTemplateName(instanceName);
		setMaxPlayers(InstanceConfigurator.getMaximumPlayers(instanceName));
		setMaxDeaths(InstanceConfigurator.getMaximumDeaths(instanceName));
	}
	
	/**
	 * @return The name of the world template of the instance.
	 */
	public String getInstanceWorldTemplateName() {
		return instanceWorldTemplateName;
	}

}
