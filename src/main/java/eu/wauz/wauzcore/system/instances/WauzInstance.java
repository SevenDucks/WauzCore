package eu.wauz.wauzcore.system.instances;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.RankConfigurator;

/**
 * An instance template, generated from an inctance config file.
 * 
 * @author Wauzmons
 */
public class WauzInstance {
	
	/**
	 * The status name of an ubobtained key.
	 */
	public static final String KEY_STATUS_UNOBTAINED = ChatColor.RED + "Unobtained";
	
	/**
	 * The status name of an obtained key.
	 */
	public static final String KEY_STATUS_OBTAINED = ChatColor.YELLOW + "Obtained";
	
	/**
	 * The status name of an used key.
	 */
	public static final String KEY_STATUS_USED = ChatColor.GREEN + "Used";
	
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
	 * The name of the instance.
	 */
	private String instanceName;
	
	/**
	 * The name of the world template of the instance.
	 */
	private String instanceWorldTemplateName;
	
	/**
	 * The maximum players of the instance.
	 */
	private int maxPlayers;
	
	/**
	 * The maximum deaths per player of the instance.
	 */
	private int maxDeaths;
	
	/**
	 * Constructs an instance template, based on the instance file in the /WauzCore/InstanceData folder.
	 * 
	 * @param instanceName The name of the instance.
	 */
	public WauzInstance(String instanceName) {
		this.instanceName = instanceName;
		this.instanceWorldTemplateName = InstanceConfigurator.getWorldTemplateName(instanceName);
		this.maxPlayers = InstanceConfigurator.getMaximumPlayers(instanceName);
		this.maxDeaths = InstanceConfigurator.getMaximumDeaths(instanceName);
	}

	/**
	 * @return The name of the instance.
	 */
	public String getInstanceName() {
		return instanceName;
	}
	
	/**
	 * @return The name of the world template of the instance.
	 */
	public String getInstanceWorldTemplateName() {
		return instanceWorldTemplateName;
	}

	/**
	 * @return The maximum players of the instance.
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * @return The maximum deaths per player of the instance.
	 */
	public int getMaxDeaths() {
		return maxDeaths;
	}

}
