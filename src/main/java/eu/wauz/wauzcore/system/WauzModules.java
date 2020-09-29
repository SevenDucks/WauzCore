package eu.wauz.wauzcore.system;

import java.util.List;

import eu.wauz.wauzcore.data.ServerConfigurator;

/**
 * A class to manage the modules that are activated on the server.
 * 
 * @author Wauzmons
 */
public class WauzModules {
	
	/**
	 * All modules that are activated on the server.
	 */
	public static final List<String> MODULES = ServerConfigurator.getModules();
	
	/**
	 * @return If the main module is active.
	 */
	public boolean isMainModuleActive() {
		return isModuleActive("main");
	}
	
	/**
	 * @return If the pets module is active.
	 */
	public boolean isPetsModuleActive() {
		return isModuleActive("pets");
	}
	
	/**
	 * @return If the pets module is active, without the main module.
	 */
	public boolean isPetsModuleStandalone() {
		return !isMainModuleActive() && isPetsModuleActive();
	}
	
	/**
	 * Checks if the given module is active.
	 * 
	 * @param moduleName The name of the module.
	 * 
	 * @return If the module is active.
	 */
	public boolean isModuleActive(String moduleName) {
		return MODULES.contains(moduleName);
	}

}
