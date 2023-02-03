package eu.wauz.wauzcore.data;

import java.util.ArrayList;
import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the CommandChains.yml.
 * 
 * @author Wauzmons
 */
public class CommandChainConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	/**
	 * @return The keys of all command chains.
	 */
	public static List<String> getAllCommandChainKeys() {
		return new ArrayList<>(mainConfigGetKeys("CommandChains", null));
	}
	
	/**
	 * @param commandChain The key of the command chain.
	 * 
	 * @return The timeout seconds of the waypoint.
	 */
	public static int getTimeout(String commandChain) {
		return mainConfigGetInt("CommandChains", commandChain + ".timeout");
	}
	
	/**
	 * @param commandChain The key of the waypoint.
	 * 
	 * @return The commands of the command chain.
	 */
	public static List<String> getCommands(String commandChain) {
		return mainConfigGetStringList("CommandChains", commandChain + ".commands");
	}

}
