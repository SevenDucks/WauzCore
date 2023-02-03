package eu.wauz.wauzcore.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.CommandChainConfigurator;

/**
 * A chain of commands, generated from a command chain config file.
 * 
 * @author Wauzmons
 */
public class WauzCommandChain {
	
	/**
	 * A map of command chains, indexed by name.
	 */
	private static Map<String, WauzCommandChain> commandChainMap = new HashMap<>();
	
	/**
	 * Initializes all command chains from the config and fills the internal waypoint map.
	 * 
	 * @see CommandChainConfigurator#getAllCommandChainKeys()
	 */
	public static void init() {
		for(String commandChainKey : CommandChainConfigurator.getAllCommandChainKeys()) {
			commandChainMap.put(commandChainKey, new WauzCommandChain(commandChainKey));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + commandChainMap.size() + " Command Chains!");
	}
	
	/**
	 * @param commandChainKey A command chain key.
	 * 
	 * @return The command chain with that key or null.
	 */
	public static WauzCommandChain getCommandChain(String commandChainKey) {
		return commandChainMap.get(commandChainKey);
	}
	
	/**
	 * @return A list of all command chain keys.
	 */
	public static List<String> getAllCommandChainKeys() {
		return new ArrayList<>(commandChainMap.keySet());
	}
	
	/**
	 * The key of the command chain.
	 */
	private String commandChainKey;
	
	/**
	 * The timeout seconds of the waypoint.
	 */
	private int timeout;
	
	/**
	 * The commands of the command chain.
	 */
	private List<String> commands;
	
	/**
	 * Constructs a command chain, based on the command chain file in the /WauzCore folder.
	 * 
	 * @param commandChainKey The key of the command chain.
	 */
	public WauzCommandChain(String commandChainKey) {
		this.commandChainKey = commandChainKey;
		timeout = CommandChainConfigurator.getTimeout(commandChainKey);
		commands = CommandChainConfigurator.getCommands(commandChainKey);
	}

	/**
	 * @return The key of the command chain.
	 */
	public String getCommandChainKey() {
		return commandChainKey;
	}

	/**
	 * @return The timeout seconds of the waypoint.
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return The commands of the command chain.
	 */
	public List<String> getCommands() {
		return new ArrayList<>(commands);
	}
	
}
